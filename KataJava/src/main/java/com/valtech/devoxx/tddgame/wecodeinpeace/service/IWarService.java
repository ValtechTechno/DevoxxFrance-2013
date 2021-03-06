package com.valtech.devoxx.tddgame.wecodeinpeace.service;

import java.util.List;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;
import com.valtech.devoxx.tddgame.wecodeinpeace.model.SpaceShip;

public interface IWarService {
	 public Javaltechian getWarDeclarerBetweenTwoJavaltechians(Javaltechian javaltechianCaptain, Javaltechian cobolianCaptain);
	 public SpaceShip getBestPositionnedShip(SpaceShip javaltechianShip, SpaceShip cobolianShip, Javaltechian javaltechianCaptain, Javaltechian cobolianCaptain);
	 public List<Javaltechian> sortJavaltechiansForBattle(List<Javaltechian> javaltechians);
	 public SpaceShip andTheWinnerIs(SpaceShip javaltechianShip, SpaceShip cobolianShip);
}
