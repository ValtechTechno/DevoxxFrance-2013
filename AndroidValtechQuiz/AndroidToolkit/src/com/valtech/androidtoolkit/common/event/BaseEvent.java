package com.valtech.androidtoolkit.common.event;

/**
 * Ev�nement de base. Notify est ex�cut� par l'EventManager pour notifier le destinataire (par
 * exemple en passant des param�tres personnalis�s).
 */
public abstract class BaseEvent<TListener>
{
    protected abstract void notify(TListener listener);
}
