package com.db.jdbc.conn.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.db.jdbc.conn.entities.Cast;
import com.db.jdbc.conn.factory.MySqlDataSourceFactory;

public class CastManager {
	
	/*
	 * Create new cast member
	 */
	public static void createCast(Cast newCast)
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Create a new cast
		String createCastQuery = "INSERT INTO Cast(characterName, actorId, movieId) VALUES(?, ?, ?)";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(createCastQuery);
			statement.setString(1, newCast.getCharacterName());
			statement.setInt(2, newCast.getActor().getId());
			statement.setInt(3, newCast.getMovie().getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Read all the cast
	 */
	public static List<Cast> readAllCast()
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Cast cast = null;
		List<Cast> casts = new ArrayList<Cast>();
		
		//Select all the cast
		String query = "SELECT * from Cast";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				cast = new Cast();
				cast.setId(resultSet.getInt("id"));
				cast.setCharacterName(resultSet.getString("characterName"));
				cast.setActor(ActorManager.readActor(resultSet.getInt("actorId")));
				cast.setMovie(MovieManager.readMovie(resultSet.getInt("movieId")));
				casts.add(cast);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return casts;
	}

	/*
	 * Read all the cast for given actor(actor id)
	 */
	public static List<Cast> readAllCastForActor(int actorId)
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Cast cast = null;
		List<Cast> casts = new ArrayList<Cast>();
		
		//Select all the cast for given actor
		String query = "SELECT * from Cast where actorId = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, actorId);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				cast = new Cast();
				cast.setId(resultSet.getInt("id"));
				cast.setCharacterName(resultSet.getString("characterName"));
				cast.setActor(ActorManager.readActor(actorId));
				cast.setMovie(MovieManager.readMovie(resultSet.getInt("movieId")));
				casts.add(cast);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return casts;
	}
	
	/*
	 * Read all the cast for a given movie id
	 */
	public static List<Cast> readAllCastForMovie(int movieId)
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Cast cast = null;
		List<Cast> casts = new ArrayList<Cast>();
		
		//Select all cast for given movie id
		String query = "SELECT * from Cast where movieId = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, movieId);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				cast = new Cast();
				cast.setId(resultSet.getInt("id"));
				cast.setCharacterName(resultSet.getString("characterName"));
				cast.setActor(ActorManager.readActor(resultSet.getInt("actorId")));
				cast.setMovie(MovieManager.readMovie(movieId));
				casts.add(cast);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return casts;
	}
	
	/*
	 * Read cast with given id
	 */
	public static Cast readCastForId(int castId)
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Cast cast = null;
		
		//Select all the cast with given cast id
		String query = "SELECT * from Cast WHERE id = ?";
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, castId);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				cast = new Cast();
				cast.setId(resultSet.getInt("id"));
				cast.setCharacterName(resultSet.getString("characterName"));
				cast.setActor(ActorManager.readActor(resultSet.getInt("actorId")));
				cast.setMovie(MovieManager.readMovie(resultSet.getInt("movieId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return cast;
	}
	
	/*
	 * Update cast with given cast id to given new cast
	 */
	public static void updateCast(int castId, Cast newCast)
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Update cast with given cast id
		String query = "UPDATE Cast SET characterName = ?, movieId = ?, actorId = ? WHERE id = ?";
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, newCast.getCharacterName());
			statement.setInt(2, newCast.getMovie().getId());
			statement.setInt(3, newCast.getActor().getId());
			statement.setInt(4, castId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Delete cast with given cast id
	 */
	public static void deleteCast(int castId)
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Delete cast with given cast id
		String query = "DELETE FROM Cast WHERE id = ?";

		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, castId);
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
			
			if(resultSet != null) {
				resultSet.close();
			}
			
			if (statement != null) {
				statement.close();
			}
				
			if (connection != null) {
				connection.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
