package com.db.jdbc.conn.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.db.jdbc.conn.entities.User;
import com.db.jdbc.conn.factory.MySqlDataSourceFactory;

public class UserManager {
	
	/*
	 * Create a new user
	 */
	public static void createUser(User newUser) {
		
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Insert a new User
		String insertUserQuery = "INSERT INTO User(username, password, firstName, lastName, email, dateOfBirth) VALUES(?, ?, ?, ?, ?, ?)";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertUserQuery);
			Date userDOB = new Date(newUser.getDateOfBirth().getTime());
			
			statement.setString(1, newUser.getUsername());
			statement.setString(2, newUser.getPassword());
			statement.setString(3, newUser.getFirstName());
			statement.setString(4, newUser.getLastName());
			statement.setString(5, newUser.getEmail());
			statement.setDate(6, userDOB);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}

	/*
	 * Read all the users
	 */
	public static List<User> readAllUsers() {
		
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = null;
		List<User> users = new ArrayList<User>();
		
		//Select all the users
		String selectAllUsersQuery = "SELECT * from User";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(selectAllUsersQuery);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
				user.setFirstName(resultSet.getString("firstName"));
				user.setLastName(resultSet.getString("lastName"));
				user.setEmail(resultSet.getString("email"));
				user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
				user.setComments(CommentManager.readAllCommentsForUsername(user
						.getUsername()));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return users;
	}

	/*
	 * Read user with given username
	 */
	public static User readUser(String username) {
		
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = null;
		
		//Select user with given username
		String selectSpecificUserQuery = "SELECT * from User WHERE username = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(selectSpecificUserQuery);
			statement.setString(1, username);
			resultSet = statement.executeQuery();
			
			if (resultSet.next()) {
				user = new User();
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
				user.setFirstName(resultSet.getString("firstName"));
				user.setLastName(resultSet.getString("lastName"));
				user.setEmail(resultSet.getString("email"));
				user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
				user.setComments(CommentManager.readAllCommentsForUsername(user.getUsername()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return user;
	}

	/*
	 * Update user with given username to new user 
	 */
	public static void updateUser(String username, User newUser) {
		
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		String query = "UPDATE User SET username = ?, password = ?, firstName = ?, lastName = ?, email = ?, dateOfBirth = ? WHERE username = ?";
		try {
			Date userDOB = new Date(newUser.getDateOfBirth().getTime());
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			
			statement.setString(1, newUser.getUsername());
			statement.setString(2, newUser.getPassword());
			statement.setString(3, newUser.getFirstName());
			statement.setString(4, newUser.getLastName());
			statement.setString(5, newUser.getEmail());
			statement.setDate(6, userDOB);
			statement.setString(7, username);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}

	/*
	 * Delete user with given username
	 * Assuming no delete cascade is there we also delete associated comments from the user
	 */
	public static void deleteUser(String username) {
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		String deleteUserAllComments = "DELETE FROM Comment WHERE username = ?";
		String deleteUser = "DELETE FROM User WHERE username = ?";

		try {
			connection = dataSource.getConnection();
			
			statement = connection.prepareStatement(deleteUserAllComments);
			statement.setString(1, username);
			statement.executeUpdate();
			statement.close();
			
			statement = connection.prepareStatement(deleteUser);
			statement.setString(1, username);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Common method to close database constructs(connection, prepared statement, result set)
	 */
	public static void closeDatabaseConstructs(Connection connection, PreparedStatement statement, ResultSet resultSet) {
		try {
			
			if (statement != null) {
				statement.close();
			}
				
			if (connection != null) {
				connection.close();
			}
			
			if(resultSet != null) {
				resultSet.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
