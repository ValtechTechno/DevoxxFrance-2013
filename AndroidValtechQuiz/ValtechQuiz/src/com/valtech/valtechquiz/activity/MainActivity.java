package com.valtech.valtechquiz.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.valtech.valtechquiz.R;
import com.valtech.valtechquiz.data.JsonQuestionsParser;
import com.valtech.valtechquiz.data.ScoresDataSource;
import com.valtech.valtechquiz.model.Question;

public class MainActivity extends Activity {

	public static ArrayList<Question> mainQuestions = new ArrayList<Question>();
	public static ScoresDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (MainActivity.datasource == null)
			MainActivity.datasource = new ScoresDataSource(this);

		mainQuestions = getExampleQuestions();

		Button playButton = (Button) findViewById(R.id.button_play);
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuizActivity.startQuiz(MainActivity.this, getExampleQuestions());
			}
		});

		Button scoresButton = (Button) findViewById(R.id.button_scores);
		scoresButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ScoresActivity.class);
				startActivity(intent);
			}
		});
	}

	private ArrayList<Question> getExampleQuestions() {
		ArrayList<Question> total = new ArrayList<Question>();
		JsonQuestionsParser jqp = new JsonQuestionsParser();
		try {
			total = (ArrayList<Question>) jqp.parseFeed(getAssets().open("questions.json"));
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
