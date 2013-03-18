package com.valtech.valtechquiz.event;

import java.util.ArrayList;

import com.valtech.androidtoolkit.common.event.BaseEvent;
import com.valtech.androidtoolkit.common.event.EventListener;
import com.valtech.valtechquiz.model.Question;

/**
 * Dispatched when User has acknowledged an answer to a question during a Quiz
 * (not a Test).
 */
public class EventQuizContinue extends BaseEvent<EventQuizContinue.Listener> {
	private ArrayList<Question> questions;
	private int questionIndex;
	private int result;

	public EventQuizContinue(int result, ArrayList<Question> questions, int questionIndex) {
		super();
		this.result = result;
		this.questions = questions;
		this.questionIndex = questionIndex;
	}

	@Override
	protected void notify(Listener pListener) {
		pListener.onQuizContinue(result, questions, questionIndex);
	}

	public interface Listener extends EventListener {
		void onQuizContinue(int result, ArrayList<Question> questions, int questionIndex);
	}
}
