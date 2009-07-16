package main;

import java.sql.*;

import org.postgis.*;

import java.io.*;




public class Main {
	
	// state of program
	private java.util.HashMap< Integer, CarProfile> carlist;
	// program state 
	private int currect_car = 0;
	private GPoint startpoint = null;
	private GPoint pastpoint  = null;
	private GPoint currectpoint = null;
	
	// program engine
	private DBConnector db; // Control Database
	private GPointCaculator matcher;// caculation snap point
	
	
	private void loadCarProfile(int vid,GPoint gp){
		try{
		if(carlist == null || !(carlist.containsKey(vid))){ // if car list not existing, create new car profile
			startpoint = gp;
			pastpoint = gp;
			
		}else{// load history form carlist
			CarProfile car = carlist.get(vid);
		    startpoint = car.getStartpoint();
			pastpoint = car.getPastpoint();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void saveCarProfile(int vid){
		CarProfile car = new CarProfile(pastpoint,startpoint);
		
		if(carlist == null){
			carlist = new java.util.HashMap<Integer, CarProfile>();
		}
		carlist.put(vid, car);
	}
	private int getGidFromLatLong(String target,double lat,double llong)
	{
		// start connection to database
		db = new DBConnector();
		db.startDB();
		
		// create matcher object
		matcher = new GPointCaculator();
		matcher.setDb(db);		
		String sql_nat = "select * from gps_archive where rectime > "+ target + " and rectime < ("+ target +"::timestamp + interval '5 hour')  and lat = " + Double.toString(lat) + "and long = " + Double.toString(llong) +" order by rectime,vid ";
		ResultSet rs = db.getResultSet(sql_nat);
		int gid=0;
		try
		{
		  //start to calculate TT
			rs.next();
		//	while(rs.next()){
			// load currect profile
			  currectpoint =  new GPoint(rs.getDouble("direction"),rs.getDouble("lat"),rs.getDouble("long"),rs.getTimestamp("rectime"),rs.getDouble("speed"),rs.getInt("vid"));
			  PGgeometry geom = (PGgeometry) rs.getObject("the_geom");
			  currectpoint.setGeom(geom.toString());
			  gid = matcher.getSnapLine(geom);
			  currectpoint.setRefline(gid);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
		
		return gid;
		
	}	
	
	public void run(){
		
		// To Do loading history data (carlist)
		// historyfile
		carlist = null;
		loadCarProfile();
		
		
		// start connection to database
		db = new DBConnector();
		db.startDB();
		
		// create matcher object
		matcher = new GPointCaculator();
		matcher.setDb(db);
		
		// create sql to call all gps data
		String target =  "\'2009-05-01 00:00:00\'";
		String sql = "select * from gps_archive where rectime > "+ target + " and rectime < ("+ target +"::timestamp + interval '5 hour') and lat > 13 and long > 100 order by rectime,vid ";
		System.out.println(sql);
		//100.47565 13.76615
				ResultSet rs = db.getResultSet(sql);
		try{
		  //start to calculate TT
		  while(rs.next()){
			  
			// load currect profile
			  currectpoint =  new GPoint(rs.getDouble("direction"),rs.getDouble("lat"),rs.getDouble("long"),rs.getTimestamp("rectime"),rs.getDouble("speed"),rs.getInt("vid"));
			  PGgeometry geom = (PGgeometry) rs.getObject("the_geom");
//			  System.out.print(geom.toString()+","+rs.getString("gid2") + ",");
			  currectpoint.setGeom(geom.toString());
			  int gid = matcher.getSnapLine(geom);
			  currectpoint.setRefline(gid);
			  
			  
			  //check current car id
			  if(currect_car == 0){ // case 0: initial all state 
				  currect_car = rs.getInt("vid");
				  loadCarProfile(currect_car,currectpoint);
			  }else if(currect_car != rs.getInt("vid")){ // case change vid: save old car profile -> load new car profile
				  
				  saveCarProfile(currect_car);
				  currect_car = rs.getInt("vid");
				  loadCarProfile(currect_car,currectpoint);
			  }
			  
			  
			  if(currectpoint.getRefline() != pastpoint.getRefline() && !(pastpoint.getRectime().equals(startpoint.getRectime()))){
				  //caculate travel time here
				  Long tt = pastpoint.getRectime().getTime() - startpoint.getRectime().getTime();
				  System.out.print(startpoint.getVid() + " " + startpoint.getRectime().toString() + " " + pastpoint.getRectime().toString() +" ");
				  double s = matcher.getTravelLenght(startpoint.getGeom(),pastpoint.getGeom(), startpoint.getRefline());
				  System.out.print(" "+ Double.toString(s) +" ");
				  System.out.println(tt/1000);
				  startpoint = currectpoint;
				  pastpoint = currectpoint;
			  }else{
				  pastpoint = currectpoint;
			  }
			  
			  
		  }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		//Save Data
		
		saveCarProfile();
		
		db.stopDB();
		
	}
	

	@SuppressWarnings("unchecked")
	private void loadCarProfile(){
		try{
		  FileInputStream f_in = new FileInputStream("myobject.data");
		  ObjectInputStream obj_in = new ObjectInputStream (f_in);
		  carlist = (java.util.HashMap<Integer,CarProfile>) obj_in.readObject();
		}catch(Exception e){
			carlist = null;
			e.printStackTrace();
		}
	}
	
	private void saveCarProfile(){
		try{
		  FileOutputStream f_out = new  FileOutputStream("myobject.data");
		  ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
		  obj_out.writeObject ( carlist );
		}catch(Exception e){
			carlist = null;
			e.printStackTrace();
		}

	}
	
	public static void main(String[] argv){
		// estimate time
		java.util.Date timer = new java.util.Date(); 
		// get start time
		Long t1 = timer.getTime();
		
		Main main = new Main();

		main.run();
		System.out.println(main.getGidFromLatLong("\'2009-05-01 00:00:00\'",13.76615,100.47565));
//		main.getGidFromLatLong("\'2009-05-01 00:00:00\'");
		timer = new java.util.Date();
		// get finish time
		Long t2 = timer.getTime();
		System.out.print(t2-t1);
		System.out.print(" milisec\r\n");
		
	}
}
