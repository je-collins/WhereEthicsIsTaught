package com.jordan.ethics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	
	private static JDBC jdbc = null;
	private Connection connection = null;
	private Statement update = null;
	
	private JDBC() throws SQLException {
		Debug.log("Connecting to database with URL \"%s\".", Constants.DB_URL);
		connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USERNAME, Constants.DB_PASSWORD);
		Debug.log("Connected.");
	}
	
	private synchronized Statement getUpdateStatement() throws SQLException {
		if (update == null) update = connection.createStatement();
		return update;
	}
	
	private synchronized Statement getQueryStatement() throws SQLException {
		Statement s = connection.createStatement();
		s.closeOnCompletion();
		return s;
	}
	
	private static synchronized JDBC getInstance() throws SQLException {
		if (jdbc == null) {
			Debug.log("Instance not found. Creating JDBC instance.");
			jdbc = new JDBC();
			setup();
		}
		return jdbc;
	}
	
	private static synchronized void setup() {
		Debug.log("Setting up schema and tables.");
		try {
			executeUpdate("if not exists (select * from sys.databases where name = '%1$s') exec('create database %1$s');", Constants.DB_DATABASE);
			executeUpdate("use %s;", Constants.DB_DATABASE);
			executeUpdate("if object_id('courses', 'U') is null create table courses(courseId varchar(20) not null, courseTitle varchar(100) default null, courseDescription varchar(1000) default null, department varchar(10) default null, flags int not null default 0, primary key (courseId));");
			executeUpdate("if object_id('years ', 'U') is null create table years (courseId varchar(20) not null, year int not null default 0, hours int not null default -1, primary key (courseId, year), foreign key (courseId) references courses(courseId));");
		} catch (SQLException e) {
			Debug.error(e);
			Debug.log("Shutting Down.");
			System.exit(1);
		}
		Debug.log("Completed setup.");
	}
	
	/////////////////////////////////////////////////////
	
	public static synchronized ResultSet executeQuery(String query, Object...args) throws SQLException {
		String sql = String.format(query, args);
		Debug.log("Executing query \"%s\".", sql);
		return getInstance().getQueryStatement().executeQuery(sql);
	}
	
	public static synchronized int executeUpdate(String update, Object...args) throws SQLException {
		String sql = String.format(update, args);
		Debug.log("Executing update \"%s\".", sql);
		return getInstance().getUpdateStatement().executeUpdate(sql);
	}
	
	/////////////////////////////////////////////////////
}
