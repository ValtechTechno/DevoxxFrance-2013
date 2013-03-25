package com.valtech.valtechquiz.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.valtech.valtechquiz.R;
import com.valtech.valtechquiz.activity.ScoresActivity;
import com.valtech.valtechquiz.event.EventQuizSaveScore;
import com.valtech.valtechquiz.model.Question;
import com.valtech.valtechquiz.model.Score;

public class QuizResultFragment extends BaseFragment {
	private static final String ARG_RESULT = "result";
	private static final String ARG_ARTICLE = "article";

	// UI.
	private TextView uiTitle;
	private TextView uiScore;
	private Button uiSave;

	private int result;
	private ArrayList<Question> questions;

	public static final QuizResultFragment newInstance(int result, ArrayList<Question> questions) {
		QuizResultFragment answerFragment = new QuizResultFragment();
		Bundle arguments = new Bundle();
		arguments.putInt(ARG_RESULT, result);
		arguments.putParcelableArrayList(ARG_ARTICLE, questions);
		answerFragment.setArguments(arguments);
		return answerFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

		// UI.
		View view = inflater.inflate(R.layout.quiz_result, container, false);
		uiTitle = (TextView) view.findViewById(R.id.quiz_result_title);
		uiScore = (TextView) view.findViewById(R.id.quiz_result_score);
		uiSave = (Button) view.findViewById(R.id.quiz_result_save);

		onRestoreInstanceState(bundle);
		onBind();
		return view;
	}

	public void onRestoreInstanceState(Bundle bundle) {
		if (bundle == null) {
			result = getArguments().getInt(ARG_RESULT);
			questions = getArguments().getParcelableArrayList(ARG_ARTICLE);
		} else {
			result = bundle.getInt(ARG_RESULT);
			questions = bundle.getParcelableArrayList(ARG_ARTICLE);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		// Note: It may look inefficient and risky (if the model evolves, e.g.
		// with links between articles, the whole model in
		// memory could be serialized) to save the whole article here. However,
		// since saving Quiz in database is not implemented
		// yet, we just can't store only the article id to get it later from
		// database when fragment is restored...
		bundle.putInt(ARG_RESULT, result);
		bundle.putParcelableArrayList(ARG_ARTICLE, questions);
	}

	public void onBind() {
		uiTitle.setText("Résultat");
		uiScore.setVisibility(View.VISIBLE);
		uiScore.setText(String.format("%02d/%02d", result, questions.size()));
		uiSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ScoresActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		save();
	}

	private void save() {
		Score score = new Score();
		score.setScore(result);
		getEventBus().dispatch(new EventQuizSaveScore(score));
	}
}
