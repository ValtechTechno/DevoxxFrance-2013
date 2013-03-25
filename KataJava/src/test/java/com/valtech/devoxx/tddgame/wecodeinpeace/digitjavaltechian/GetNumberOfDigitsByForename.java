package com.valtech.devoxx.tddgame.wecodeinpeace.digitjavaltechian;

import com.valtech.devoxx.tddgame.wecodeinpeace.service.IJavaltechianService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.JavaltechianService;

public class GetNumberOfDigitsByForename {
	String forenameOfTheJavaltechian;
	
	IJavaltechianService service;
	
	public String getForenameOfTheJavaltechian() {
		return forenameOfTheJavaltechian;
	}
	
	public void setForenameOfTheJavaltechian(String forenameOfTheJavaltechian) {
		this.forenameOfTheJavaltechian = forenameOfTheJavaltechian;
	}
	
	public int getNumberOfDigits(){
		service = new JavaltechianService();	
		return service.getNumberOfDigitsByForename(forenameOfTheJavaltechian);
	}
}
