package com.valtech.devoxx.tddgame.wecodeinpeace.service;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;


public class IronArmService implements IIronArmService {
	JavaltechianService service = new JavaltechianService();
	
	public Javaltechian getWinnerOfTwoJavaltechians(Javaltechian player1, Javaltechian player2) {
		double hypotPlayer1 = Math.hypot(service.getPowerByName(player1.getName()), service.getNumberOfDigitsByForename(player1.getForename()));
		double hypotPlayer2 = Math.hypot(service.getPowerByName(player2.getName()), service.getNumberOfDigitsByForename(player2.getForename()));	
		if (hypotPlayer1 >= hypotPlayer2)
			return player1;
		else 
			return player2;
	}
}
