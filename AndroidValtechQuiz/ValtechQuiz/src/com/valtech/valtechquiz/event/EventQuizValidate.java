package com.valtech.valtechquiz.event;

import java.util.ArrayList;

import com.valtech.androidtoolkit.common.event.BaseEvent;
import com.valtech.androidtoolkit.common.event.EventListener;
import com.valtech.valtechquiz.model.Question;

/**
 * Dispatched when user has answered a question during a Quiz or Test.
 */
public class EventQuizValidate extends BaseEvent<EventQuizValidate.Listener> {
	private ArrayList<Question> questions;
	private int questionIndex;
	private int result;

	public EventQuizValidate(int result, ArrayList<Question> questions, int questionIndex) {
		super();
		this.result = result;
		this.questions = questions;
		this.questionIndex = questionIndex;
	}

	@Override
	protected void notify(Listener pListener) {
		pListener.onQuizValidate(result, questions, questionIndex);
	}

	public interface Listener extends EventListener {
		void onQuizValidate(int result, ArrayList<Question> questions, int questionIndex);
	}
}
