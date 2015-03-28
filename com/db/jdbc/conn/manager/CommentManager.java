package com.db.jdbc.conn.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.db.jdbc.conn.entities.Comment;
import com.db.jdbc.conn.factory.MySqlDataSourceFactory;

public class CommentManager {
	
	/*
	 * Insert new comment
	 */
	public static void createComment(Comment newComment)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Insert new comment
		String insertCommentQuery = "INSERT INTO Comment(comment, commentDate, username, movieId) VALUES(?, ?, ?, ?)";
		
		try {
			Date commentDOB = new Date(newComment.getCommentDate().getTime());
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertCommentQuery);
			
			statement.setString(1, newComment.getComment());
			statement.setDate(2, commentDOB);
			statement.setString(3, newComment.getUser().getUsername());
			statement.setInt(4, newComment.getMovie().getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Read all the comments
	 */
	public static List<Comment> readAllComments()
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultStatement = null;
		Comment comment = null;
		List<Comment> comments = new ArrayList<Comment>();
		
		//Select query to read all comments
		String query = "SELECT * from Comment";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			resultStatement = statement.executeQuery();
			while (resultStatement.next()) {
				comment = new Comment();
				comment.setId(resultStatement.getInt("id"));
				comment.setComment(resultStatement.getString("comment"));
				comment.setCommentDate(resultStatement.getDate("commentDate"));
				comment.setUser(UserManager.readUser(resultStatement.getString("username")));
				comment.setMovie(MovieManager.readMovie(resultStatement.getInt("movieId")));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultStatement);
		}
		return comments;
	}

	/*
	 * Read all the comments for user with given username
	 */
	public static List<Comment> readAllCommentsForUsername(String username)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultStatement = null;
		Comment comment = null;
		List<Comment> comments = new ArrayList<Comment>();
		
		//Select all comments from a specific user
		String selectAllCommentsFromUserQuery = "SELECT * from Comment WHERE username = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(selectAllCommentsFromUserQuery);
			statement.setString(1, username);
			resultStatement = statement.executeQuery();
			while (resultStatement.next()) {
				comment = new Comment();
				comment.setId(resultStatement.getInt("id"));
				comment.setComment(resultStatement.getString("comment"));
				comment.setCommentDate(resultStatement.getDate("commentDate"));
				comment.setUser(UserManager.readUser(username));
				comment.setMovie(MovieManager.readMovie(resultStatement.getInt("movieId")));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultStatement);
		}
		return comments;
	}
	
	/*
	 * Read all the comments from a given movie
	 */
	public static List<Comment> readAllCommentsForMovie(int movieId)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultStatement = null;
		Comment comment = null;
		List<Comment> comments = new ArrayList<Comment>();
		
		//Select all the comments for the movie
		String selectMovieAllCommentsQuery = "SELECT * from Comment WHERE movieId = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(selectMovieAllCommentsQuery);
			statement.setInt(1, movieId);
			resultStatement = statement.executeQuery();
			while (resultStatement.next()) {
				comment = new Comment();
				comment.setId(resultStatement.getInt("id"));
				comment.setComment(resultStatement.getString("comment"));
				comment.setCommentDate(resultStatement.getDate("commentDate"));
				comment.setUser(UserManager.readUser(resultStatement.getString("username")));
				comment.setMovie(MovieManager.readMovie(movieId));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultStatement);
		}
		return comments;
	}
	
	/*
	 * Read comment with given id
	 */
	public static Comment readCommentForId(int commentId)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultStatement = null;
		Comment comment = null;
		
		//Select a single comment
		String query = "SELECT * from Comment WHERE id = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, commentId);
			resultStatement = statement.executeQuery();
			if (resultStatement.next()) {
				comment = new Comment();
				comment.setId(resultStatement.getInt("id"));
				comment.setComment(resultStatement.getString("comment"));
				comment.setCommentDate(resultStatement.getDate("commentDate"));
				comment.setUser(UserManager.readUser(resultStatement.getString("username")));
				comment.setMovie(MovieManager.readMovie(resultStatement.getInt("movieId")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, resultStatement);
		}
		return comment;
	}
	
	/*
	 * Update comment with given comment id to the given new comment
	 */
	public static void updateComment(int commentId, String newComment)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Update comment with given id
		String query = "UPDATE Comment SET comment = ?, commentDate = ? WHERE id = ?";
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, newComment);
			statement.setDate(2, new Date((new java.util.Date()).getTime()));
			statement.setInt(3, commentId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConstructs(connection, statement, null);
		}
	}
	
	/*
	 * Delete comment with given comment id
	 */
	public static void deleteComment(int commentId)
	{
		//Initialize db constructs
		DataSource dataSource = MySqlDataSourceFactory.getMySqlDataSource();
		Connection connection = null;
		PreparedStatement statement = null;
		
		//Delete given comment
		String query = "DELETE FROM Comment WHERE id = ?";

		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, commentId);
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
