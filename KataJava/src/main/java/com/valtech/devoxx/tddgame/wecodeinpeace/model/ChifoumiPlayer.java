package com.valtech.devoxx.tddgame.wecodeinpeace.model;

public class ChifoumiPlayer {
	private int score;
	private Choice choice;
	
	public Choice choose(){
		return Choice.getRandom();
	}
	public void credit() {
		score++;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public ChifoumiPlayer() {
		super();
		this.score = 0;
	}
	public Choice getChoice() {
		return choice;
	}
	public void setChoice(Choice choice) {
		this.choice = choice;
	}
	public void reset() {
		score=0;	
	}
	
	
	
}