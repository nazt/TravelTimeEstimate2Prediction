package main;
import org.postgis.*;

import java.sql.*;

public class GPointCaculator {
	private DBConnector db;
	
	/**
	 * @return the db
	 */
	public DBConnector getDb() {
		return db;
	}

	/**
	 * @param db the db to set
	 */
	public void setDb(DBConnector db) {
		this.db = db;
	}

	public int getSnapLine(PGgeometry the_geom){
		String sql = new String();
		int gid = 0;
		sql =  "select gid , distance(transform(the_geom,32647),transform(\'"+ the_geom.toString() + "\',32647))";
		sql += "from roadmot where the_geom && st_expand(transform(\'"+ the_geom.toString() +"\',4326),0.001) and ";
		sql += "distance(transform(the_geom,32647),transform(\'"+ the_geom.toString() + "\',32647)) < 100 order by distance limit 1";
		ResultSet rs = db.getResultSet(sql);
		try{
			String strout = new String();
			while(rs.next()){
				strout += rs.getString("gid");
				gid = rs.getInt("gid");
			}
//		System.out.print(strout + "\r\n");
		}catch(Exception e){
			e.printStackTrace();
		}
		return gid;
	}
	
// return present for travelling
	
	public double getTravelLenght(String geom_start,String geom_end,int gid){
		String sql = new String();
		double st = 0.0;
		double ed = 0.0;
		sql = "select st_line_locate_point(the_geom,transform(\'"+ geom_start +"\',4326)) as start ,";
		sql += "st_line_locate_point(the_geom,transform(\'"+ geom_end +"\',4326)) as end from roadmot where gid = " + Integer.toString(gid);
		//System.out.println(sql);
		ResultSet rs = db.getResultSet(sql);
	    try{
			while(rs.next()){
				st = rs.getDouble("start");
			    ed = rs.getDouble("end");
			}
		}catch(Exception e){
			e.printStackTrace();
			return -1.0;
		}
		
		
		if(st == ed){
			return 0.0;
		}
		
		if(ed > st){
		    sql = "select st_length(st_line_substring(the_geom,"+Double.toString(st)+","+ Double.toString(ed) + "))/st_length(the_geom) as s from roadmot where gid = " + Integer.toString(gid);
		}else{
			sql = "select st_length(st_line_substring(the_geom,"+Double.toString(ed)+","+ Double.toString(st) + "))/st_length(the_geom) as s from roadmot where gid = " + Integer.toString(gid);	
		}
		rs = db.getResultSet(sql);
	    try{
	    	double ret = 0.0;
			while(rs.next()){
				ret = rs.getDouble("s");
			}
			return ret;
		}catch(Exception e){
			e.printStackTrace();
			return -1.0;
		}

	}

	
}
