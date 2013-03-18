package com.valtech.valtechquiz.data;

import java.util.ArrayList;
import java.util.Random;

import com.valtech.valtechquiz.activity.MainActivity;
import com.valtech.valtechquiz.model.Question;

public class QuizService {

	public QuizService() {
	}

	public ArrayList<Question> download() {
		ArrayList<Question> total = MainActivity.mainQuestions;
		
		Random r = new Random();
		
		ArrayList<Question> questions = new ArrayList<Question>();
		
	    while (questions.size() < 15) {
	       int idx = r.nextInt(total.size());
	       questions.add(total.get(idx));
	       total.remove(idx);
	    }
		
		return questions;
	}
}
