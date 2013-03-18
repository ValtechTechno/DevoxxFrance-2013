package com.valtech.androidtoolkit.service.task;

public interface TaskHandler<TResult>
{
    /**
     * Exécution de la tâche de fond sur un thread séparé. Ne pas modifier d'objets liés à l'UI ici.
     * @throws Exception Si une erreur quelconque survient, alors celle-ci est renvoyé au
     *             gestionnaire onError.
     */
    TResult onProcess() throws Exception;

    /**
     * Si l'exécution de onProgress() se termine correctement, alors onFinish() est appelé sur le
     * thread UI. C'est ici que doivent être modifiés les objets liés à l'UI (ex: fusion des
     * résultats de la tâche avec les résultats déjà affichés).
     */
    void onFinish(TResult pResult);

    /**
     * Si l'exécution de onProgress() échoue, onError() est alors appelé sur le thread UI. Tout
     * message d'erreur ou modification/effacement des données affichées dans l'UI doivent être
     * réalisé ici.
     */
    void onError(Exception pException);
}
