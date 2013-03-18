package com.valtech.valtechquiz.model;

public class Score {
	private long id;
	private int score;
	private int time;
	private String email;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return String.valueOf(score);
	}
}
