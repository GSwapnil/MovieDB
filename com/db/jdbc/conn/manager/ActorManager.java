package com.db.jdbc.conn.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.db.jdbc.conn.entities.Actor;
import com.db.jdbc.conn.factory.MySqlDataSourceFactory;

public class ActorManager {
	
	/*
	 * Create a new actor
	 */
	public static void createActor(Actor actor)
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Insert querry to insert a single actor
		String createActorQuery = "INSERT INTO Actor(firstName, lastName, dateOfBirth) VALUES(?, ?, ?, ?)";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(createActorQuery);
			Date actorDOB = new Date(actor.getDateOfBirth().getTime());
			
			statement.setString(1, actor.getFirstName());
			statement.setString(2, actor.getLastName());
			statement.setDate(3, actorDOB);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Read all the actors
	 */
	public static List<Actor> readAllActors()
	{
		//Initialize DB constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Actor actor = null;
		List<Actor> actors = new ArrayList<Actor>();
		
		//Select query to get all the actors
		String selectAllActorsQuery = "SELECT * from Actor";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(selectAllActorsQuery);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				actor = new Actor();
				actor.setId(resultSet.getInt("id"));
				actor.setFirstName(resultSet.getString("firstName"));
				actor.setLastName(resultSet.getString("lastName"));
				actor.setDateOfBirth(resultSet.getDate("dateOfBirth"));
				actor.setCasts(CastManager.readAllCastForActor(actor.getId()));
				actors.add(actor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return actors;
	}
	
	/*
	 * Read actor with given actorId
	 */
	public static Actor readActor(int actorId)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Actor actor = null;
		
		//Select a single actor
		String selectSpecificActorQuery = "SELECT * from Actor WHERE id = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(selectSpecificActorQuery);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				actor = new Actor();
				actor.setId(resultSet.getInt("id"));
				actor.setFirstName(resultSet.getString("firstName"));
				actor.setLastName(resultSet.getString("lastName"));
				actor.setDateOfBirth(resultSet.getDate("dateOfBirth"));
				actor.setCasts(CastManager.readAllCastForActor(actor.getId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return actor;
	}
	
	/*
	 * Update actor with actorId to the new actor
	 */
	public static void updateActor(int actorId, Actor newActor)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Update the given actor with new Actor info
		String updateActorQuery = "UPDATE Actor SET firstName = ?, lastName = ?, dateOfBirth = ? WHERE id = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateActorQuery);
			Date actorDOB = new Date(newActor.getDateOfBirth().getTime());
			
			statement.setString(1, newActor.getFirstName());
			statement.setString(2, newActor.getLastName());
			statement.setDate(3, actorDOB);
			statement.setInt(4, actorId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Delete actor with given actorId
	 */
	public static void deleteActor(int actorId)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Assuming cascade is not implemented
		String deleteActorAllCasts = "DELETE FROM Cast WHERE actorId = ?";
		String deleteActor = "DELETE FROM Actor WHERE id = ?";

		try {
			connection = dataSource.getConnection();
			
			statement = connection.prepareStatement(deleteActorAllCasts);
			statement.setInt(1, actorId);
			statement.executeUpdate();
			statement.close();
			
			statement = connection.prepareStatement(deleteActor);
			statement.setInt(1, actorId);
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
