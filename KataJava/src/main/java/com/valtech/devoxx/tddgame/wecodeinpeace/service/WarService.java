package com.valtech.devoxx.tddgame.wecodeinpeace.service;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;

public class WarService implements IWarService {

	ISpaceshipService spaceshipService = new SpaceshipService();
	IIronArmService ironArmService = new IronArmService();

	public Javaltechian getWarDeclarerBetweenTwoJavaltechians(
			Javaltechian javaltechianCaptain, Javaltechian cobolianCaptain) {
		//TODO improve me
		return null;
	}
}
