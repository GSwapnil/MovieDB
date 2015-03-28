package com.db.jdbc.conn.entities;

public class Cast {

	private int id;							//Auto generated by database			
	private Actor actor;
	private Movie movie;
	private String characterName;
	/**
	 * 
	 */
	public Cast() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param id
	 * @param actor
	 * @param movie
	 * @param characterName
	 */
	public Cast(int id, Actor actor, Movie movie, String characterName) {
		super();
		this.id = id;
		this.actor = actor;
		this.movie = movie;
		this.characterName = characterName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Actor getActor() {
		return actor;
	}
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public String getCharacterName() {
		return characterName;
	}
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}
}