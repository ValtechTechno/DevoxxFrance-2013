package com.valtech.devoxx.tddgame.wecodeinpeace.service;

import java.util.List;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;
import com.valtech.devoxx.tddgame.wecodeinpeace.model.SpaceShip;

public class WarService implements IWarService {

	ISpaceshipService spaceshipService = new SpaceshipService();
	IIronArmService ironArmService = new IronArmService();

	public Javaltechian getWarDeclarerBetweenTwoJavaltechians(
			Javaltechian javaltechianCaptain, Javaltechian cobolianCaptain) {
		double coeffJ = javaltechianCaptain.getPower()/javaltechianCaptain.getAge();
		double coeffC = cobolianCaptain.getPower()/cobolianCaptain.getAge();
		if (coeffC > coeffJ)
			return cobolianCaptain;
		else 
			return javaltechianCaptain;
	}

	public SpaceShip getBestPositionnedShip(SpaceShip javaltechianShip,
			SpaceShip cobolianShip, Javaltechian javaltechianCaptain,
			Javaltechian cobolianCaptain) {
		double discriminant1 = Math.pow(javaltechianCaptain.getAge(), 2) - 
				4 * 
				javaltechianShip.getPopulation().size() * 
				spaceshipService.getMaxCapacityByName(javaltechianShip.getName());
		double discriminant2 = Math.pow(cobolianCaptain.getAge(), 2) - 
				4 * 
				cobolianShip.getPopulation().size() * 
				spaceshipService.getMaxCapacityByName(cobolianShip.getName());
		if (discriminant1 > discriminant2)
			return javaltechianShip;
		else 
			return cobolianShip;
	}

	public List<Javaltechian> sortJavaltechiansForBattle(
			List<Javaltechian> javaltechians) {
		//TODO improve me
		return null;
	}
	
	
}
