package com.northwind.rest.status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.sql.*;
import com.northwind.dao.*;


@Path("/v1/status")
public class V1_status {
	
	private static final String API_VERSION = "00.01.00";
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(){
		return "<p>Java Restful API Web Services</p>";
	}
	
	@Path("/version")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion(){
		return "<p>Northwind API Version: " + API_VERSION + "</p>";
	}
	
	@Path("/database")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnDatabaseStatus() throws Exception {
		PreparedStatement query = null;
		String myString = null;
		String returnString = null;
		Connection conn = null;
		
		try{
			conn = MySQLDB.MySQLDBConn().getConnection();
/*			query = conn.prepareStatement(" SELECT to_char(sysdate, 'YYY-MM-DD HH24:MI:SS') DATETIME " +
											"FROM sys.dual");*/
			query = conn.prepareStatement("SELECT CURTIME()");
			//query = conn.prepareStatement("select DATE_FORMAT(NOW(),'%W, %M %e, %Y @ %h:%i %p')");
			ResultSet rs = query.executeQuery();
			while(rs.next()){
				//myString = rs.getString("DATETIME");
				myString = rs.getTime(1).toString();
			}
			query.close();
			returnString = "<p>Database CST Time return: <b>" + myString + "</b></p>";
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.close();
		}
		
		return returnString;
	}
	
	
	
	
	
	
	
	
	

}
