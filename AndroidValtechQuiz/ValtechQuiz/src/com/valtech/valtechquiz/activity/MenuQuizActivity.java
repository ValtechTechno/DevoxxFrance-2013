package com.valtech.valtechquiz.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.valtech.valtechquiz.R;
import com.valtech.valtechquiz.data.JsonQuestionsParser;
import com.valtech.valtechquiz.model.Question;

public class MenuQuizActivity extends Activity {

	public static ArrayList<Question> mainQuestions = new ArrayList<Question>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_quiz);

		mainQuestions = getExampleQuestions("questions.json");

		Button androidButton = (Button) findViewById(R.id.button_android);
		androidButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuizActivity.startQuiz(MenuQuizActivity.this, getExampleQuestions("questions.json"), "Android");
			}
		});

		Button javaButton = (Button) findViewById(R.id.button_java);
		javaButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuizActivity.startQuiz(MenuQuizActivity.this, getExampleQuestions("java-questions.json"), "Java");
			}
		});
		
		Button jqueryButton = (Button) findViewById(R.id.button_jquery);
		jqueryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QuizActivity.startQuiz(MenuQuizActivity.this, getExampleQuestions("jquery-questions.json"), "JQuery");
			}
		});
	}

	private ArrayList<Question> getExampleQuestions(String fileName) {
		ArrayList<Question> total = new ArrayList<Question>();
		JsonQuestionsParser jqp = new JsonQuestionsParser();
		try {
			total = (ArrayList<Question>) jqp.parseFeed(getAssets().open(fileName));
		} catch (IOException e) {
		}

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
