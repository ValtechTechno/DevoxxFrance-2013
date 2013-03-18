package com.valtech.valtechquiz.event;

import com.valtech.androidtoolkit.common.event.BaseEvent;
import com.valtech.androidtoolkit.common.event.EventListener;

/**
 * Dispatched chrono
 */
public class EventQuizChrono extends BaseEvent<EventQuizChrono.Listener> {
	private int time;

	public EventQuizChrono(int time) {
		super();
		this.time = time;
	}

	@Override
	protected void notify(Listener pListener) {
		pListener.onQuizChrono(time);
	}

	public interface Listener extends EventListener {
		void onQuizChrono(int time);
	}
}
