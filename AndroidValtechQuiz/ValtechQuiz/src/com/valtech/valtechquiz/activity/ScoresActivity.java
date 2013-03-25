package com.valtech.valtechquiz.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.valtech.valtechquiz.R;
import com.valtech.valtechquiz.adapter.ScoresAdapter;
import com.valtech.valtechquiz.model.Question;
import com.valtech.valtechquiz.model.Score;

public class ScoresActivity extends Activity {

	public static ArrayList<Question> mainQuestions = new ArrayList<Question>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scores);
		ListView scoresList = (ListView)findViewById(R.id.scores_list);
		MainActivity.datasource.open();
		List<Score> scores = MainActivity.datasource.getAllScores();
		MainActivity.datasource.close();
		scoresList.setAdapter(new ScoresAdapter(getBaseContext(), scores));
	}
}
