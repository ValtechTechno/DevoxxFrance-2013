package com.valtech.valtechquiz.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.valtech.androidtoolkit.common.event.AndroidEventBus;
import com.valtech.androidtoolkit.common.event.EventBus;
import com.valtech.valtechquiz.R;
import com.valtech.valtechquiz.event.EventQuizChrono;
import com.valtech.valtechquiz.event.EventQuizContinue;
import com.valtech.valtechquiz.event.EventQuizLoaded;
import com.valtech.valtechquiz.event.EventQuizSaveScore;
import com.valtech.valtechquiz.event.EventQuizValidate;
import com.valtech.valtechquiz.fragment.QuizQuestionFragment;
import com.valtech.valtechquiz.fragment.QuizResultFragment;
import com.valtech.valtechquiz.model.Question;
import com.valtech.valtechquiz.model.Score;

public class QuizActivity extends FragmentActivity implements EventQuizContinue.Listener, EventQuizValidate.Listener, EventQuizLoaded.Listener, EventQuizSaveScore.Listener {

	private static final String THEME = "theme";
	private static final String ARG_QUIZ = "quiz";
	private ArrayList<Question> questions;
	private EventBus eventBus;

	private String theme;
	private String email;
	private int timeToQuiz = 120000;
	private int currentTime = timeToQuiz;
	private Handler handler = new Handler();
	private Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {
			updateTime(true);
		}
	};
	private Runnable timerRunnableFirst = new Runnable() {

		@Override
		public void run() {
			updateTime(false);
		}
	};

	public static int result = 0;

	public static void startQuiz(Activity fromActivity, ArrayList<Question> questions, String theme) {
		Class<?> target = QuizActivity.class;
		Intent intent = new Intent(fromActivity.getBaseContext(), target);
		intent.putParcelableArrayListExtra(ARG_QUIZ, questions);
		intent.putExtra(THEME, theme);
		fromActivity.startActivity(intent);
	}

	public QuizActivity() {
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_quiz);
		// Data.
		if (bundle == null) {
			questions = getIntent().getParcelableArrayListExtra(ARG_QUIZ);
			theme = getIntent().getExtras().getString(THEME);
			alertEmail();
		} else {
			theme = bundle.getString(THEME);
			questions = bundle.getParcelableArrayList(ARG_QUIZ);
			currentTime = bundle.getInt("currentTime");
			email = bundle.getString("email");
			handler.post(timerRunnable);
		}

		eventBus = AndroidEventBus.fromActivity(this);
	}

	private void alertEmail() {
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.dialog_email);
		dialog.setTitle("Entrez votre Email");
		dialog.show();
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});

		final EditText emailEditText = (EditText) dialog.findViewById(R.id.dialog_email);
		final Button ok = (Button) dialog.findViewById(R.id.dialog_ok);

		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!TextUtils.isEmpty(emailEditText.getText().toString())) {
					email = emailEditText.getText().toString();
					dialog.dismiss();
					onQuizLoaded(questions);
					handler.post(timerRunnableFirst);
				} else {
					Toast.makeText(QuizActivity.this, "Veuillez entrer votre Email", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	protected void updateTime(boolean b) {
		if (b)
			currentTime -= 1000;

		if (currentTime <= 0) {
			handler.removeCallbacks(timerRunnable);
			Toast.makeText(getBaseContext(), "Temps écoulé", Toast.LENGTH_LONG).show();
			showResult(result, questions);

		} else {
			eventBus.dispatch(new EventQuizChrono(currentTime));
			handler.postDelayed(timerRunnable, 1000);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		eventBus.registerListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		eventBus.unregisterListener(this);
		handler.removeCallbacks(timerRunnable);
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putParcelableArrayList(ARG_QUIZ, questions);
		bundle.putInt("currentTime", currentTime);
		bundle.putString("email", email);
		handler.removeCallbacks(timerRunnable);
		super.onSaveInstanceState(bundle);
	}

	@Override
	public void onSaveScore(Score score) {
		score.setEmail(email);
		score.setTime(timeToQuiz - currentTime);
		score.setTheme(theme);
		MainActivity.datasource.open();
		MainActivity.datasource.createScore(score);
		MainActivity.datasource.close();

	}

	@Override
	public void onQuizLoaded(ArrayList<Question> questions) {
		this.questions = questions;
		QuizQuestionFragment questionFragment = QuizQuestionFragment.newInstance(questions);
		getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, questionFragment).commit();
	}

	@Override
	public void onQuizFailedLoading(ArrayList<Question> questions) {
		finish();
	}

	@Override
	public void onQuizValidate(int result, ArrayList<Question> questions, int questionIndex) {
		if (questionIndex >= questions.size() - 1) {
			handler.removeCallbacks(timerRunnable);
			showResult(result, questions);
		} else {
			showNextQuestion(result, questions, questionIndex);
		}
	}

	@Override
	public void onQuizContinue(int result, ArrayList<Question> questions, int questionIndex) {
		// Note: Continue is called for Quiz only (not Tests).
		if (questionIndex >= questions.size() - 1) {
			showResult(result, questions);
		} else {
			showNextQuestion(result, questions, questionIndex);
		}
	}

	// private void showLoading(ArrayList<Question> questions) {
	// QuizLoadingFragment loadingFragment =
	// QuizLoadingFragment.newInstance(questions);
	// getSupportFragmentManager().beginTransaction().replace(R.id.activity_content,
	// loadingFragment).commit();
	// }

	private void showNextQuestion(int result, ArrayList<Question> questions, int questionIndex) {
		int nextQuestionIndex = questionIndex + 1;
		QuizQuestionFragment questionFragment = QuizQuestionFragment.newInstance(result, questions, nextQuestionIndex);
		getSupportFragmentManager().beginTransaction()
		// .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
				.replace(R.id.activity_content, questionFragment).commit();
	}

	private void showResult(int result, ArrayList<Question> questions) {
		QuizResultFragment quizResultFragment = QuizResultFragment.newInstance(result, questions);
		getSupportFragmentManager().beginTransaction()
		// .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
				.replace(R.id.activity_content, quizResultFragment).commit();
	}

}
