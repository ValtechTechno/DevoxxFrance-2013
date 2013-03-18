package com.valtech.valtechquiz.event;

import com.valtech.androidtoolkit.common.event.BaseEvent;
import com.valtech.androidtoolkit.common.event.EventListener;
import com.valtech.valtechquiz.model.Score;

/**
 * Dispatched save score
 */
public class EventQuizSaveScore extends BaseEvent<EventQuizSaveScore.Listener> {
	private Score score;

	public EventQuizSaveScore(Score score) {
		super();
		this.score = score;
	}

	@Override
	protected void notify(Listener pListener) {
		pListener.onSaveScore(score);
	}

	public interface Listener extends EventListener {
		void onSaveScore(Score score);
	}
}
