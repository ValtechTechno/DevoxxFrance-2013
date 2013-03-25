package com.valtech.devoxx.tddgame.wecodeinpeace.service;

import java.util.List;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;

public class SpaceshipService implements ISpaceshipService {
	
	public int getMaxCapacityByName(String name){ 
		if (name.toLowerCase().startsWith("bo"))
			 return 8*10;
		if (name.toLowerCase().startsWith("lo"))
			 return 64*10;
		if (name.toLowerCase().startsWith("in"))
			 return 32*10;
		if (name.toLowerCase().startsWith("do"))
			 return 64*10;
		if (name.toLowerCase().startsWith("fl"))
			 return 32*10;
		if (name.toLowerCase().startsWith("ch"))
			 return 16*10;
		if (name.toLowerCase().startsWith("by"))
			 return 8*10;
		if (name.toLowerCase().startsWith("sh"))
			 return 16*10;
		else return 10;
	}
	
	public Javaltechian getJavaltechianCaptainByPopulation(
			List<Javaltechian> javaltechians) {
	 	if (javaltechians.size() == 1){
			return javaltechians.get(0);
		}
		double d1 =  Math.sqrt(javaltechians.get(0).getPower()) - 2 * javaltechians.get(0).getAge();
		double d2 =  Math.sqrt(javaltechians.get(1).getPower()) - 2 * javaltechians.get(1).getAge();	
		if (d1 > d2)
			javaltechians.remove(1);
		else
			javaltechians.remove(0);
		return getJavaltechianCaptainByPopulation(javaltechians);
	}
}
