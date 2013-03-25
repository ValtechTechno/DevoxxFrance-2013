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
		int limit = javaltechians.size();
		boolean permutation = true;
		Javaltechian objOne; 
		Javaltechian objTwo; 
		while (permutation) {
			permutation = false;
			for (int i =0; i<limit - 1; i++) {
				objOne = (Javaltechian)javaltechians.get(i);
				objTwo = (Javaltechian)javaltechians.get(i+1);
				if (objOne.getPower() > objTwo.getPower()) {
					javaltechians.set(i+1, objOne);
					javaltechians.set(i, objTwo);
					permutation = true;
				}
			}
		}
		return(javaltechians);
	}
	
	public SpaceShip andTheWinnerIs(SpaceShip javaltechianShip,
			SpaceShip cobolianShip) {
		List<Javaltechian> jList = sortJavaltechiansForBattle(javaltechianShip.getPopulation());
		List<Javaltechian> cList = sortJavaltechiansForBattle(cobolianShip.getPopulation());
		if (jList.isEmpty())
			return cobolianShip;
		if (cList.isEmpty())
			return javaltechianShip;		
		Javaltechian jWarrior = jList.get(0);
		Javaltechian cWarrior = cList.get(0);
		if (ironArmService.getWinnerOfTwoJavaltechians(jWarrior, cWarrior).equals(cWarrior)){
			jList.remove(0);
			if (jList.isEmpty())
				return cobolianShip;
			cList.remove(0);
			cWarrior.setPower((int) (cWarrior.getPower()*0.85));
			cList.add(cWarrior);
		}
		if (ironArmService.getWinnerOfTwoJavaltechians(jWarrior, cWarrior).equals(jWarrior)){
			cList.remove(0);
			if (cList.isEmpty())
				return javaltechianShip;
			jList.remove(0);
			jWarrior.setPower((jWarrior.getPower()*0.8));
			jList.add(jWarrior);
		}
		Javaltechian cobolianCaptain = spaceshipService.getCobolianCaptainByPopulation(cList);
		Javaltechian javaltechianCaptain = spaceshipService.getJavaltechianCaptainByPopulation(jList);
		if (javaltechianShip.equals(this.getBestPositionnedShip(javaltechianShip, cobolianShip, javaltechianCaptain, cobolianCaptain))){
			for (Javaltechian javaltechian : jList) {
				javaltechian.setPower(javaltechian.getPower()*0.02);
			}
			for (Javaltechian javaltechian : cList) {
				javaltechian.setPower(javaltechian.getPower()*0.01);
			}
		}
		if (cobolianShip.equals(this.getBestPositionnedShip(javaltechianShip, cobolianShip, javaltechianCaptain, cobolianCaptain))){
			for (Javaltechian javaltechian : cList) {
				javaltechian.setPower(javaltechian.getPower()*0.02);
			}
			for (Javaltechian javaltechian : jList) {
				javaltechian.setPower(javaltechian.getPower()*0.01);
			}
		}	
		return andTheWinnerIs(javaltechianShip, cobolianShip);
	}
}
