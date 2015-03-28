package com.db.jdbc.conn.factory;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySqlDataSourceFactory {
	
	public static DataSource getMySqlDataSource()
	{
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/MovieDB");
		dataSource.setUser("root");
		dataSource.setPassword("root");
		return dataSource;
	}

}
