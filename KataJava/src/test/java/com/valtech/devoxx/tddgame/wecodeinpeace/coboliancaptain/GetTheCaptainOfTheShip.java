package com.valtech.devoxx.tddgame.wecodeinpeace.coboliancaptain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IIronArmService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IJavaltechianService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.ISpaceshipService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IronArmService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.JavaltechianService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.SpaceshipService;

public class GetTheCaptainOfTheShip {
	IJavaltechianService service;
	IIronArmService ironArmService;
	ISpaceshipService spaceshipService;
	
	Map<String,Javaltechian> javaltechianMap;		
	List<Javaltechian> javaltechians;
	
	private String populationOfTheShip;
	
	public String getPopulationOfTheShip() {
		return populationOfTheShip;
	}

	public void setPopulationOfTheShip(String populationOfTheShip) {
		this.populationOfTheShip = populationOfTheShip;
	}
	
	public String whoIsTheCaptain(){
		service= new JavaltechianService();
		ironArmService = new IronArmService();
		spaceshipService = new SpaceshipService();
		javaltechianMap = new HashMap<String, Javaltechian>();			
		javaltechians = new ArrayList<Javaltechian>();
		javaltechianMap.put("Dowe", new Javaltechian("Dowe",  "John", 34,service.getPowerByName("Dowe")));
		javaltechianMap.put("Brardow", new Javaltechian("Brardow",  "Bernie", 32,service.getPowerByName("Brardow")));
		javaltechianMap.put("Lothalirin", new Javaltechian("Lothalirin",  "Bruce", 44,service.getPowerByName("Lothalirin")));
		javaltechianMap.put("Borgas", new Javaltechian("Borgas",  "Milkysnow", 36,service.getPowerByName("Borgas")));
		javaltechianMap.put("Dreisean", new Javaltechian("Dreisean",  "Poppins", 23,service.getPowerByName("Dreisean")));
		javaltechianMap.put("Multivoxx", new Javaltechian("Multivoxx",  "Shaina", 44,service.getPowerByName("Multivoxx")));
		javaltechianMap.put("Nurrok", new Javaltechian("Nurrok",  "Demendadir", 36,service.getPowerByName("Nurrok")));
		javaltechianMap.put("Igir", new Javaltechian("Igir",  "Nusenmil", 55,service.getPowerByName("Igir")));
		javaltechianMap.put("Bamendar", new Javaltechian("Bamendar",  "Emegil", 48,service.getPowerByName("Bamendar")));
		javaltechianMap.put("Penethir", new Javaltechian("Penethir",  "Pelennorn", 13,service.getPowerByName("Penethir")));
		for (String javaltechian : populationOfTheShip.split(";")) {
			javaltechians.add(javaltechianMap.get(javaltechian));
		}
		return spaceshipService.getCobolianCaptainByPopulation(javaltechians).toString();
	}
}
