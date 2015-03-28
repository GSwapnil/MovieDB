package com.db.jdbc.conn.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.db.jdbc.conn.entities.Movie;
import com.db.jdbc.conn.factory.MySqlDataSourceFactory;

public class MovieManager {
	
	/*
	 * Create a new movie
	 */
	public static void createMovie(Movie movie)
	{
		//Initialize db constructs
		DataSource ds = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Insert new movie
		String insertMovieQuery = "INSERT INTO Movie(title, posterImage, releaseDate) VALUES(?, ?, ?)";
		
		try {
			Date movieReleaseDate = new Date(movie.getReleaseDate().getTime());
			connection = ds.getConnection();
			statement = connection.prepareStatement(insertMovieQuery);
			
			statement.setString(1, movie.getTitle());
			statement.setString(2, movie.getPosterImage());
			statement.setDate(3, movieReleaseDate);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Read all the movies
	 */
	public static List<Movie> readAllMovies()
	{
		//Initialize db constructs
		DataSource ds = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Movie movie = null;
		List<Movie> movies = new ArrayList<Movie>();
		
		//Select all the movies
		String selectMovieQuery = "SELECT * from Movie";
		
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(selectMovieQuery);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setTitle(resultSet.getString("title"));
				movie.setPosterImage(resultSet.getString("posterImage"));
				movie.setReleaseDate(resultSet.getDate("releaseDate"));
				movie.setCasts(CastManager.readAllCastForMovie(movie.getId()));
				movie.setComments(CommentManager.readAllCommentsForMovie(movie.getId()));
				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return movies;
	}
	
	/*
	 * Read movie with given movie id
	 */
	public static Movie readMovie(int movieId)
	{
		//Initialize db constructs
		DataSource ds = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Movie movie = null;
		
		//Select movie with given id
		String selectSpecificMovieQuery = "SELECT * from Movie";
		
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(selectSpecificMovieQuery);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setTitle(resultSet.getString("title"));
				movie.setPosterImage(resultSet.getString("posterImage"));
				movie.setReleaseDate(resultSet.getDate("releaseDate"));
				movie.setCasts(CastManager.readAllCastForMovie(movie.getId()));
				movie.setComments(CommentManager.readAllCommentsForMovie(movie.getId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultSet);
		}
		return movie;
	}
	
	/*
	 * Update movie with given movie id to new movie
	 */
	public static void updateMovie(int movieId, Movie movie)
	{
		//Initialize db constructs
		DataSource ds = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Update movie with given id
		String updateMovieQuery = "UPDATE Movie SET title = ?, posterImage = ?, releaseDate = ? WHERE id = ?";
		
		try {
			Date movieReleaseDate = new Date(movie.getReleaseDate().getTime());
			connection = ds.getConnection();
			statement = connection.prepareStatement(updateMovieQuery);
			
			statement.setString(1, movie.getTitle());
			statement.setString(2, movie.getPosterImage());
			statement.setDate(3, movieReleaseDate);
			statement.setInt(4, movieId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Delete movie with given id
	 * Assuming no delete cascade is in place,
	 *  Delete all the associated cast
	 *  Delete all the associated comments
	 */
	public static void deleteMovie(int movieId)
	{
		//Initialize db constructs
		DataSource ds = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Delete queries
		String deleteMovieAllCast = "DELETE FROM Cast WHERE movieId = ?";
		String deleteMovieAllComments = "DELETE FROM Comment WHERE movieId = ?";
		String deleteMovie = "DELETE FROM Movie WHERE id = ?";

		try {
			connection = ds.getConnection();
			
			statement = connection.prepareStatement(deleteMovieAllCast);
			statement.setInt(1, movieId);
			statement.executeUpdate();
			statement.close();
			
			statement = connection.prepareStatement(deleteMovieAllComments);
			statement.setInt(1, movieId);
			statement.executeUpdate();
			statement.close();
			
			statement = connection.prepareStatement(deleteMovie);
			statement.setInt(1, movieId);
			statement.executeUpdate();
			statement.close();
			
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
