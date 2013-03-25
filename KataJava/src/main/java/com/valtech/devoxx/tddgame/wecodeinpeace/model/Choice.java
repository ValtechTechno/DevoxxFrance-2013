package com.valtech.devoxx.tddgame.wecodeinpeace.model;

public enum Choice {
	
	zombie("braaaaaaains !"),
	pirate("all aboard !"), 
	ninja("kiiii-ah !"), 
	monkey("ee-ee-eek !"), 
	robot("ex-ter-min-ate !");
	
	private final String noise;
	
	public static Choice getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }

    Choice(String noise) {
		this.noise = noise;
	}

	public String getNoise() {
		return noise;
	}
}
