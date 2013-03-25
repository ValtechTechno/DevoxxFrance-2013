package com.valtech.valtechquiz.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.valtech.androidtoolkit.common.event.AndroidEventBus;
import com.valtech.androidtoolkit.common.event.EventBus;

public class BaseFragment extends Fragment
{
    private EventBus eventBus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = AndroidEventBus.fromActivity(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        eventBus.unregisterListener(this);
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
