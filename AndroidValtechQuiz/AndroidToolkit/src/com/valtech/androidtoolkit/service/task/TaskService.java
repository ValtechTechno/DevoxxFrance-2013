package com.valtech.androidtoolkit.service.task;

public interface TaskService
{
    /**
     * Exécute une tâche asynchrone.
     * @param handler Décrit le contenu de la tâche asynchrone.
     */
    <TResult> void execute(TaskHandler<TResult> handler);

    void notifyProgress(TaskProgressNotifier pProgressHandler);
}
