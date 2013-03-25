package com.valtech.devoxx.tddgame.wecodeinpeace.model;

public class Javaltechian{
	private String name;
	private String forename;
	private double power;
	private int age;
	 
	public Javaltechian(String name, String forename) {
		super();
		this.name = name;
		this.forename = forename;
	}
	
	public Javaltechian(String name, String forename, int power, int age) {
		super();
		this.name = name;
		this.forename = forename;
		this.power = power;
		this.age = age;
	}
	
	public Javaltechian() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getForename() {
		return forename;
	}
	public void setForename(String forename) {
		this.forename = forename;
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		this.power = power;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String toString(){
		return name;
	}
}
