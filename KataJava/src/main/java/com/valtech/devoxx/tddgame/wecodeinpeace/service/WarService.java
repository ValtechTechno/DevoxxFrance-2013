package com.valtech.devoxx.tddgame.wecodeinpeace.service;

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
		//TODO improve me
		return null;
	}

}
