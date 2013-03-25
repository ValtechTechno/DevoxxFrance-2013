package com.valtech.valtechquiz.event;

import java.util.ArrayList;

import com.valtech.androidtoolkit.common.event.BaseEvent;
import com.valtech.androidtoolkit.common.event.EventListener;
import com.valtech.valtechquiz.model.Question;

/**
 * Dispatched when user has equested to start a Quiz.
 */
public class EventQuizLoaded extends BaseEvent<EventQuizLoaded.Listener> {
	private boolean loaded;
	private ArrayList<Question> questions;

	public static EventQuizLoaded success(ArrayList<Question> questions) {
		return new EventQuizLoaded(questions, true);
	}

	public static EventQuizLoaded failure(ArrayList<Question> questions) {
		return new EventQuizLoaded(questions, false);
	}

	public EventQuizLoaded(ArrayList<Question> questions, boolean loaded) {
		super();
		this.loaded = loaded;
		this.questions = questions;
	}

	@Override
	protected void notify(Listener pListener) {
		if (loaded) {
			pListener.onQuizLoaded(questions);
		} else {
			pListener.onQuizFailedLoading(questions);
		}
	}

	public interface Listener extends EventListener {
		void onQuizLoaded(ArrayList<Question> questions);

		void onQuizFailedLoading(ArrayList<Question> questions);
	}
}
