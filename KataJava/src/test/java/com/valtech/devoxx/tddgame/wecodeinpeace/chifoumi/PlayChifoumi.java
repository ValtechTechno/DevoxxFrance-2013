package com.valtech.devoxx.tddgame.wecodeinpeace.chifoumi;

import com.valtech.devoxx.tddgame.wecodeinpeace.model.ChifoumiPlayer;
import com.valtech.devoxx.tddgame.wecodeinpeace.model.Choice;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.ChifoumiService;
import com.valtech.devoxx.tddgame.wecodeinpeace.service.IChifoumiService;

public class PlayChifoumi {

	private IChifoumiService service = new ChifoumiService();
	
	private ChifoumiPlayer player1 = new ChifoumiPlayer();
	
	private ChifoumiPlayer player2 = new ChifoumiPlayer();
	
	public void setChoiceOfJavaltechian1(String choice){
		player1.setChoice(Choice.valueOf(choice.toLowerCase()));	
	}
	public void setChoiceOfJavaltechian2(String choice){
		player2.setChoice(Choice.valueOf(choice.toLowerCase()));	
	}
	public String noiseHeared(){
		player1.reset();
		player2.reset();
		service.jeuUnTour(player1, player2);
		if (player1.getScore() > player2.getScore())
			return player1.getChoice().getNoise();
		else
			return player2.getChoice().getNoise();
			
	}
	
	
	
}
