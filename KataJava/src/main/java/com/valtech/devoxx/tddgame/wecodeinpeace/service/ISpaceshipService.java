package com.valtech.devoxx.tddgame.wecodeinpeace.service;

import java.util.List;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;

public interface ISpaceshipService {
	public int getMaxCapacityByName(String name);
	public Javaltechian getJavaltechianCaptainByPopulation(List<Javaltechian> javaltechians);
	public Javaltechian getCobolianCaptainByPopulation(List<Javaltechian> javaltechians);
}
