package com.valtech.devoxx.tddgame.wecodeinpeace.war.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;
import com.valtech.devoxx.tddgame.wecodeinpeace.model.SpaceShip;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IJavaltechianService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IWarService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.JavaltechianService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.WarService;

public class RoundOneFight {
	IWarService warService = new WarService();
	IJavaltechianService service = new JavaltechianService();
	private String javaltechianShipName;
	private String cobolianShipName;
	private ArrayList<Javaltechian> javaltechians;
	private Map<String, Javaltechian> javaltechianMap = new HashMap<String, Javaltechian>();
	private String populationOfJavaltechians;
	private String populationOfCobolians;
	private ArrayList<Javaltechian> cobolians;
	
	public String getJavaltechianShipName() {
		return javaltechianShipName;
	}

	public void setJavaltechianShipName(String javaltechianShipName) {
		this.javaltechianShipName = javaltechianShipName;
	}

	public String getCobolianShipName() {
		return cobolianShipName;
	}

	public void setCobolianShipName(String cobolianShipName) {
		this.cobolianShipName = cobolianShipName;
	}

	public String getPopulationOfJavaltechians() {
		return populationOfJavaltechians;
	}

	public void setPopulationOfJavaltechians(String populationOfJavaltechians) {
		this.populationOfJavaltechians = populationOfJavaltechians;
	}

	public String getPopulationOfCobolians() {
		return populationOfCobolians;
	}

	public void setPopulationOfCobolians(String populationOfCobolians) {
		this.populationOfCobolians = populationOfCobolians;
	}
	
	public String andTheWinnerIs(){
		warService = new WarService();
		javaltechians = new ArrayList<Javaltechian>();
		cobolians = new ArrayList<Javaltechian>();
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
		for (String javaltechian : populationOfJavaltechians.split(";")) {
			javaltechians.add(javaltechianMap.get(javaltechian));
		}
		for (String cobolian : populationOfCobolians.split(";")) {
			cobolians.add(javaltechianMap.get(cobolian));
		}
		SpaceShip javaltechianShip = new SpaceShip(javaltechianShipName);
		javaltechianShip.setPopulation(javaltechians);
		SpaceShip cobolianShip = new SpaceShip(cobolianShipName);
		cobolianShip.setPopulation(cobolians);
		return warService.andTheWinnerIs(javaltechianShip, cobolianShip).toString();
	}
}
