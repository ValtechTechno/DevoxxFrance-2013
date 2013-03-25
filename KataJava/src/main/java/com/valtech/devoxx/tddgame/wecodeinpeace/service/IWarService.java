package com.valtech.devoxx.tddgame.wecodeinpeace.service;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.Javaltechian;

public interface IWarService {
	 public Javaltechian getWarDeclarerBetweenTwoJavaltechians(Javaltechian javaltechianCaptain, Javaltechian cobolianCaptain);
}
