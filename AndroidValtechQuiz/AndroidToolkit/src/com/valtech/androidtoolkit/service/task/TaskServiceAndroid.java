package com.valtech.androidtoolkit.service.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.os.Handler;

public class TaskServiceAndroid implements TaskService
{
    private Handler mUIQueue;
    private Executor mTaskExecutor;


    /**
     * Must be instantiated from the UI Thread.
     */
    public TaskServiceAndroid() {
        super();
        // This code assumes the constructor is called on the UI Thread.
        mUIQueue = new Handler();
        mTaskExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable pRunnable) {
                Thread thread = new Thread(pRunnable);
                thread.setDaemon(true); // TODO Check if that's necessary.
                return thread;
            }
        });
    }

    @Override
    public <TResult> void execute(final TaskHandler<TResult> pHandler) {
        mTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final TResult result = pHandler.onProcess();
                    mUIQueue.post(new Runnable() {
                        @Override
                        public void run() {
                            pHandler.onFinish(result);
                        }
                    });
                } catch (final Exception eException) {
                    mUIQueue.post(new Runnable() {
                        @Override
                        public void run() {
                            pHandler.onError(eException);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void notifyProgress(final TaskProgressNotifier pProgressHandler) {
        mUIQueue.post(new Runnable() {
            @Override
            public void run() {
                pProgressHandler.onProgress();
            }
        });
    }
}
