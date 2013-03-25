package com.valtech.devoxx.tddgame.wecodeinpeace.war.wardeclarer;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IWarService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.WarService;

public class GetTheDeclarer {
	
	private Javaltechian javaltechian = new Javaltechian();
	private Javaltechian cobolian = new Javaltechian();
	private IWarService service;
	
	public void setJavaltechianName(String javaltechianName) {
		javaltechian.setName(javaltechianName);
	}
	
	public void setCobolianName(String cobolianName) {
		cobolian.setName(cobolianName);
	}
	
	public void setJavaltechianAge(int javaltechianAge) {
		javaltechian.setAge(javaltechianAge);
	}
	
	public void setCobolianAge(int cobolianAge) {
		cobolian.setAge(cobolianAge);
	}
	
	public void setJavaltechianPower(int javaltechianPower) {
		javaltechian.setPower(javaltechianPower);
	}
	
	public void setCobolianPower(int cobolianPower) {
		cobolian.setPower(cobolianPower);
	}
		
	public String whoIsTheDeclarer(){
		service = new WarService();
		return service.getWarDeclarerBetweenTwoJavaltechians(javaltechian, cobolian).toString();
	}
}
