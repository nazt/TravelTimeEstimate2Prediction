package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnector {
	private Connection con;
	
	public boolean startDB(){
	    try{
		     Class.forName("org.postgresql.Driver");
	         String url = "jdbc:postgresql://localhost:5433/lllt";
	         con = DriverManager.getConnection(url,"postgres","nimda");
	         return true;
	    }catch(Exception e){
	    	 e.printStackTrace();
	         return false;	
	    }
	    
	}
	
	public ResultSet getResultSet(String sql){
		try{
			Statement stm;
			ResultSet rs;		
			stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery(sql);
			return rs;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		}
	
	public int getUpdateDB(String sql){
			try{
				Statement stm;
				int rs;		
				stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = stm.executeUpdate(sql);
				return rs;
			}catch(Exception e){
				e.printStackTrace();
				return -1;
			}
		}
	public boolean stopDB(){
	    try{
		     con.close();
	         return true;
	    }catch(Exception e){
	    	 e.printStackTrace();
	         return false;	
	    }
	    
	}
}
