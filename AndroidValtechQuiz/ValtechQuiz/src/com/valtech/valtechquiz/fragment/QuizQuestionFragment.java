package com.valtech.valtechquiz.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.valtech.androidtoolkit.view.Table;
import com.valtech.valtechquiz.R;
import com.valtech.valtechquiz.activity.QuizActivity;
import com.valtech.valtechquiz.event.EventQuizValidate;
import com.valtech.valtechquiz.event.EventQuizChrono;
import com.valtech.valtechquiz.model.Question;

public class QuizQuestionFragment extends BaseFragment implements EventQuizChrono.Listener {
	private static final String ARG_RESULT = "result";
	private static final String ARG_ARTICLE = "article";
	private static final String ARG_QUESTION = "question";
	private static final String ARG_ANSWER = "answer";
	// UI.
	private TextView uiHeader;
	private TextView uiQuestion;
	private ImageView uiImage;
	private TextView uiChrono;
	private Table uiAnswerTable;
	private Button uiValidate;
	// Data.
	private ArrayList<Question> questions;
	private Question question;
	private int questionIndex;
	private int answerIndex;
	private int result;

	public static final QuizQuestionFragment newInstance(ArrayList<Question> questions) {
		return newInstance(-1, questions, 0);
	}

	public static final QuizQuestionFragment newInstance(int result, ArrayList<Question> questions, int questionIndex) {
		QuizQuestionFragment questionFragment = new QuizQuestionFragment();
		Bundle arguments = new Bundle();
		arguments.putInt(ARG_RESULT, result);
		arguments.putParcelableArrayList(ARG_ARTICLE, questions);
		arguments.putInt(ARG_QUESTION, questionIndex);
		questionFragment.setArguments(arguments);
		return questionFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		// UI.
		View view = inflater.inflate(R.layout.quiz_question, container, false);
		uiHeader = (TextView) view.findViewById(R.id.quiz_question_header);
		uiQuestion = (TextView) view.findViewById(R.id.quiz_question);
		uiImage = (ImageView) view.findViewById(R.id.quiz_question_image);
		uiChrono = (TextView) view.findViewById(R.id.quiz_question_chrono);
		uiValidate = (Button) view.findViewById(R.id.quiz_validate);
		uiAnswerTable = (Table) view.findViewById(R.id.quiz_question_table);
		// uiAnswerTable.setColumnCount(1);
		// uiAnswerTable.setItemLayout(R.layout.quiz_question_item);

		uiAnswerTable.setAdapter(new Table.Adapter() {
			@Override
			public int getItemCount() {
				return question.answers.size();
			}

			@Override
			public void onCreateCell(View cell, int index) {
				onBindAnswer(cell, index);
			}
		});
		uiValidate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onValidate();
			}
		});

		onRestoreInstanceState(bundle);
		onBind();
		return view;
	}

	public void onRestoreInstanceState(Bundle bundle) {
		if (bundle == null) {
			result = getArguments().getInt(ARG_RESULT);
			questions = getArguments().getParcelableArrayList(ARG_ARTICLE);
			questionIndex = getArguments().getInt(ARG_QUESTION);
			answerIndex = -1;
		} else {
			result = bundle.getInt(ARG_RESULT);
			questions = bundle.getParcelableArrayList(ARG_ARTICLE);
			questionIndex = bundle.getInt(ARG_QUESTION);
			answerIndex = bundle.getInt(ARG_ANSWER);
		}

		if (result == -1) {
			result = 0;
		}
		question = questions.get(questionIndex);
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
		bundle.putInt(ARG_QUESTION, questionIndex);
		bundle.putInt(ARG_ANSWER, answerIndex);
	}

	protected void onBind() {
		uiHeader.setText(String.format("%02d/%02d", questionIndex + 1, questions.size()));
		uiQuestion.setText(question.title);
		uiAnswerTable.notifyDataSetChanged();
		uiValidate.setEnabled(answerIndex != -1);
		if (!TextUtils.isEmpty(question.image)) {
			Bitmap bitmap;
			try {
				Options options = new Options();
				options.inSampleSize = 2;
				bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open(question.getImagePath()), new Rect(0, 0, 400, 300), options);
				uiImage.setVisibility(View.VISIBLE);
				uiImage.setImageBitmap(bitmap);
			} catch (IOException e) {
			}
		} else {
			uiImage.setVisibility(View.GONE);
		}
	}

	private void onBindAnswer(View cell, final int index) {
		String answer = question.getAnswerLabel(index);
		CheckBox uiCheckBox = (CheckBox) cell.findViewById(R.id.quiz_answer_item_chk);

		uiCheckBox.setText(answer);
		uiCheckBox.setTextColor(Color.WHITE);

		// uiCheckBox.setChecked(index == answerIndex);
		uiCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onCheckAnswer(index);
			}
		});
	}

	private void onCheckAnswer(int index) {
		int nbCheked = 0;
		for (View cell : uiAnswerTable) {
			CheckBox checkBox = (CheckBox) cell.findViewById(R.id.quiz_answer_item_chk);
			if (checkBox.isChecked())
				nbCheked++;
		}
		if (nbCheked > 0)
			uiValidate.setEnabled(true);
		else {
			uiValidate.setEnabled(false);
		}
	}

	private void onValidate() {
		// result.selectAnswer(question, question.answers.get(answerIndex));
		int newResult = result + checkResult(questionIndex);
		QuizActivity.result = newResult;
		getEventBus().dispatch(new EventQuizValidate(newResult, questions, questionIndex));
	}

	private int checkResult(int questionIndex) {
		int questionResult = 1;
		int currentIndex = 0;
		for (View cell : uiAnswerTable) {
			CheckBox checkBox = (CheckBox) cell.findViewById(R.id.quiz_answer_item_chk);
			if (checkBox.isChecked()) {
				if (!question.isAnswerCorrect(currentIndex))
					questionResult = 0;
			} else {
				if (question.isAnswerCorrect(currentIndex))
					questionResult = 0;
			}
			++currentIndex;
		}
		return questionResult;
	}

	@Override
	public void onQuizChrono(int time) {
		uiChrono.setText(convertTime(time));
	}

	private String convertTime(int time) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time), TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}
}
