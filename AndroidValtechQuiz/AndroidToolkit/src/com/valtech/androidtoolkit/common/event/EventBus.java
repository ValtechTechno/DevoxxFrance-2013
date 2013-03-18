package com.valtech.androidtoolkit.common.event;

public interface EventBus
{
    /**
     * Fait suivre l'�v�nement pEvent aux listeners abonn�s.
     * 
     * ATTENTION: Doit �tre appel� depuis le thread-UI.
     * 
     * @param pEvent L'�v�nement lev�.
     */
    <TListener, TEvent extends BaseEvent<TListener>> //
    void dispatch(final TEvent event);

    <TListener, TEvent extends BaseEvent<TListener>> //
    void dispatchTo(final TEvent pEvent, TListener pListener);

    /**
     * Enregistre un listener d'�v�nement. Les �v�nements �cout�s sont ceux pour lesquels une
     * interface de type EventListener est enregistr�e (h�ritage d'un niveau max, par exemple
     * MyEvent.Listener extends EventListener. Suppose que le MyEvent.Listener est englob� dans un
     * event MyEvent). Permet �galement de sp�cifier des EventManager � qui forwarder l'�v�nement.
     * 
     * @param listener Le listener � ajouter
     * @param forwardTo EventManagers � qui forwarder l'�v�nement
     */
    void registerListener(Object listener, EventBus... forwardTo);

    /**
     * Ajoute un listener sur �v�nement. Permet �galement de sp�cifier des EventManager � qui
     * forwarder l'�v�nement. Utiliser registerListener si possible car celui-ci enregistre tous les
     * types d'�v�nements qu'impl�mente une classe. Utiliser plut�t addListener lorsqu'une class
     * doit dispatcher ceertains �v�nements � des enfants ou aux "conjoints".
     * 
     * @param event Evenement causant la notification du listener
     * @param listener Le listener � ajouter
     * @param forwardTo EventManagers � qui forwarder l'�v�nement
     */
    <TBaseListener, TListener extends TBaseListener, TEvent extends BaseEvent<TBaseListener>> //
    void addListener(Class<TEvent> event, TListener listener, EventBus... forwardTo);

    /**
     * Indique que l'�v�nement sp�cifi� doit �tre forward� aux EventManager sp�cifi�s.
     * 
     * @param event Evenement causant le forward
     * @param forwardTo EventManager devant �tre inform� de l'�v�nement
     */
    <TListener, TEvent extends BaseEvent<TListener>> void forwardTo(Class<TEvent> event, EventBus... forwardTo);

    /**
     * Retire toute r�f�rence � un listener. Doit imp�rativement �tre appel� lorsqu'une vue est
     * "dispos�".
     * 
     * @param pListener Le listener � retirer
     */
    void unregisterListener(Object listener, EventBus... forwardTo);

    void clearListeners();

    // <TService> TService getService(Class<TService> serviceClass);
    // void removeService(Class<?> serviceType);
    // void registerService(Object service);
}
