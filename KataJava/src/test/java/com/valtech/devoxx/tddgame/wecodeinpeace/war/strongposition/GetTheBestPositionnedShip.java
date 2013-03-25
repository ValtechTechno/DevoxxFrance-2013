package com.valtech.devoxx.tddgame.wecodeinpeace.war.strongposition;

import java.util.ArrayList;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;
import com.valtech.devoxx.tddgame.wecodeinpeace.model.SpaceShip;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IWarService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.WarService;

public class GetTheBestPositionnedShip {
	private IWarService service = new WarService();
	
	private Javaltechian javaltechianCaptain = new Javaltechian();
	private Javaltechian cobolianCaptain = new Javaltechian();
	
	private SpaceShip javaltechianShip = new SpaceShip();
	private SpaceShip cobolianShip = new SpaceShip();
	
	public void setJavaltechianShipName(String name){
		javaltechianShip.setName(name);
	}
	
	public void setJavaltechianShipPopulation(int population){
		javaltechianShip.setPopulation(new ArrayList<Javaltechian>(population));
	}
	
	public void setCobolianShipName(String name){
		cobolianShip.setName(name);
	}

	public void setCobolianShipPopulation(int population){
		cobolianShip.setPopulation(new ArrayList<Javaltechian>(population));
	}
	
	public void setAgeOfTheJavaltechianCaptain(int age){
		javaltechianCaptain.setAge(age);
	}
	
	public void setAgeOfTheCobolianCaptain(int age){
		cobolianCaptain.setAge(age);
	}
	
	public String whichShipIsTheBestPositionned(){
		 return service.getBestPositionnedShip(javaltechianShip, cobolianShip, javaltechianCaptain, cobolianCaptain).toString();
	}
	
	
}
