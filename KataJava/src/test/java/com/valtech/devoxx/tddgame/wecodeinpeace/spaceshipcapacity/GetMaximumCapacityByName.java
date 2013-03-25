package com.valtech.devoxx.tddgame.wecodeinpeace.spaceshipcapacity;

import com.valtech.devoxx.tddgame.wecodeinpeace.service.ISpaceshipService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.SpaceshipService;

public class GetMaximumCapacityByName {
	
	private ISpaceshipService service;
	private String nameOfTheSpaceship;
	
	public String getNameOfTheSpaceship() {
		return nameOfTheSpaceship;
	}

	public void setNameOfTheSpaceship(String nameOfTheSpaceship) {
		this.nameOfTheSpaceship = nameOfTheSpaceship;
	}

	public int getMaximumCapacity(){
		service = new SpaceshipService();
		return service.getMaxCapacityByName(nameOfTheSpaceship);
	}
}
