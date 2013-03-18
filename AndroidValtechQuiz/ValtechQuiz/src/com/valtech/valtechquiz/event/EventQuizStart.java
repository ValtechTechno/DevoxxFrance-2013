package com.valtech.valtechquiz.event;

import java.util.ArrayList;

import com.valtech.androidtoolkit.common.event.BaseEvent;
import com.valtech.androidtoolkit.common.event.EventListener;
import com.valtech.valtechquiz.model.Question;

/**
 * Dispatched when user has equested to start a Quiz.
 */
public class EventQuizStart extends BaseEvent<EventQuizStart.Listener> {
	private ArrayList<Question> questions;

	public EventQuizStart(ArrayList<Question> questions) {
		super();
		this.questions = questions;

	}

	@Override
	protected void notify(Listener pListener) {
		pListener.onQuizStart(questions);
	}

	public interface Listener extends EventListener {
		void onQuizStart(ArrayList<Question> questions);
	}
}
