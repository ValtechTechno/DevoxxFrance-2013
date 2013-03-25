package com.valtech.devoxx.tddgame.wecodeinpeace.service;


import com.valtech.devoxx.tddgame.wecodeinpeace.model.ChifoumiPlayer;
import com.valtech.devoxx.tddgame.wecodeinpeace.model.Choice;

public class ChifoumiService implements IChifoumiService{
		 
	public void jeuUnTour(ChifoumiPlayer player1,ChifoumiPlayer player2){	
		Choice choice1 = player1.getChoice();
		Choice choice2 = player2.getChoice();
		switch (choice1) {
		case zombie:
			if (choice2.equals(Choice.monkey)||choice2.equals(Choice.pirate))
				player1.credit();
			if (choice2.equals(Choice.robot)||choice2.equals(Choice.ninja))
				player2.credit();
			break;
		case pirate:
			if (choice2.equals(Choice.monkey)||choice2.equals(Choice.robot))
				player1.credit();
			if (choice2.equals(Choice.zombie)||choice2.equals(Choice.ninja))
				player2.credit();
			break;
		case robot:
			if (choice2.equals(Choice.zombie)||choice2.equals(Choice.ninja))
				player1.credit();
			if (choice2.equals(Choice.pirate)||choice2.equals(Choice.monkey))
				player2.credit();
			break;
		case monkey:
			if (choice2.equals(Choice.robot)||choice2.equals(Choice.ninja))
				player1.credit();
			if (choice2.equals(Choice.zombie)||choice2.equals(Choice.pirate))
				player2.credit();
			break;
		case ninja:
			if (choice2.equals(Choice.zombie)||choice2.equals(Choice.pirate))
				player1.credit();
			if (choice2.equals(Choice.robot)||choice2.equals(Choice.monkey))
				player2.credit();
			break;
		default:
			break;
		}
	}
}
