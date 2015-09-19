package com.northwind.dao;

import javax.naming.*;
import javax.sql.*;
import java.sql.Connection;

public class MySQLDB {
	
	private static DataSource MySQLDataSource = null;
	private static Context initContext = null;
	private static Context envContext = null;
	
	public static DataSource MySQLDBConn() throws Exception {
		if(MySQLDataSource != null){
			return MySQLDataSource;
		}
		try {
			if(envContext == null){
				initContext = new InitialContext();
				envContext = (Context) initContext.lookup("java:comp/env");
			}
			MySQLDataSource = (DataSource)envContext.lookup("jdbc/NorthwindDB");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MySQLDataSource;
	}
	
	protected static Connection mySQlPCPartsConnection() throws Exception{
		Connection conn = null;
		try{
			conn = MySQLDB.MySQLDBConn().getConnection();
			return conn;
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
		
	}

}
