package com.valtech.devoxx.tddgame.wecodeinpeace.ironarm;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IIronArmService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IJavaltechianService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IronArmService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.JavaltechianService;

public class WhoIsOverTheTop {
	private Javaltechian player1;
	private Javaltechian player2;
	IJavaltechianService service;
	IIronArmService ironArmService;
	
	public void setPlayer1(String id) {
		 player1 = new Javaltechian(id.split(" ")[0],id.split(" ")[1]);
	}
	
	public void setPlayer2(String id) {
		 player2 = new Javaltechian(id.split(" ")[0],id.split(" ")[1]);
	}
	
	public int strengthOfPlayer1(){
		service = new JavaltechianService();
		return service.getPowerByName(player1.getName());
	}
	
	public int strengthOfPlayer2(){
		service = new JavaltechianService();
		return service.getPowerByName(player2.getName());
	}
	
	public int nrOfFingersOfPlayer1(){
		service = new JavaltechianService();
		return service.getNumberOfDigitsByForename(player1.getForename());
	}
	
	public int nrOfFingersOfPlayer2(){
		service = new JavaltechianService();
		return service.getNumberOfDigitsByForename(player2.getForename());
	}
	
	public String andTheWinnerIs(){
		ironArmService = new IronArmService();
		return ironArmService.getWinnerOfTwoJavaltechians(player1,player2).toString();	
	}
}
