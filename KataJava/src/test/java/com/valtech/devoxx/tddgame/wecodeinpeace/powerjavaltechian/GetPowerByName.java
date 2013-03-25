package com.valtech.devoxx.tddgame.wecodeinpeace.powerjavaltechian;

import com.valtech.devoxx.tddgame.wecodeinpeace.service.IJavaltechianService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.JavaltechianService;

public class GetPowerByName {
	String nameOfTheJavaltechian;
	
	IJavaltechianService service;
	
	public String getNameOfTheJavaltechian() {
		return nameOfTheJavaltechian;
	}
	
	public void setNameOfTheJavaltechian(String nameOfTheJavaltechian) {
		this.nameOfTheJavaltechian = nameOfTheJavaltechian;
	}
	
	public int getPower(){
		service = new JavaltechianService();	
		return service.getPowerByName(nameOfTheJavaltechian);
	}
}
