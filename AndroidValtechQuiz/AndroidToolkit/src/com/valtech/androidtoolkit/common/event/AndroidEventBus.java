package com.valtech.androidtoolkit.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multimap;

/**
 * Classe permettant la transmission d'�v�nement inter-composants (ex: entre vue graphique d'une
 * application). L'EventManager permet donc de notifier des composants tiers en �vitant un couplage
 * fort.
 * 
 * Les EventManager peuvent �tre cha�n�s entre eux: des composants peuvent par exemple s'enregistrer
 * sur un "canal" local via un EventManager commun et utiliser un EventManager global pour
 * communiquer avec le reste de l'application. Sur RPA, le principe utilis� est le suivant: chaque
 * vue (au sens MVP/MVC) parent et ses enfants directs sont connect�s sur un "canal local". Ce
 * principe s'appliquant r�cursivement de fa�on hi�rarchique. Le parent peux forwarder les
 * �v�nements globaux aux enfant et r�ciproquement les �v�nements locaux vers l'ext�rieur.
 * 
 * L'objectif est que, comme avec le MVC-Hi�rarchique, le parent ait la r�sponsabilit� du transfert
 * des �v�nements et que ceux-ci soit transmis de fa�on ordonn� (afin d'�viter les interconnexions
 * multiples de gr� � gr� comme on peut l'obtenir avec le pattern Observable standard). De plus cela
 * permet de s�parer facilement plusieurs instances d'une IHM: par exemple si plusieurs composants
 * communiquent au sein d'un m�me �cran mais que plusieurs instances de l'�cran (onglets, etc.)
 * peuvent exister. Il n'y a alors pas de "parasitage" ou de n�cessiter de tester la source de
 * l'�v�nement.
 * 
 * Note: Les �v�nements sont forward�s � d'autres EventManagers syst�matiquement "apr�s" avoir
 * notifi� les listeners.
 */
public class AndroidEventBus implements EventBus
{
    // Stocke un EventBus par activité. Permet de simplifier la création et la récupération
    // d'EventBus notamment
    // dans les fragments. TOOD A préciser
    private static Map<Activity, EventBus> sEventBuses;

    private Thread mUIThread;
    // Contient des listener index�s par le type d'�v�nement.
    private Multimap<Class<?>, Object> mEventMap;
    // Conteneur d'EventManager index�s par le type d'�v�nement.
    private Multimap<Class<?>, EventBus> mForwardMap;
    private boolean mStopForwarding;

    // TODO Put in the ApplicationCore.
    static {
        sEventBuses = new MapMaker().weakKeys().weakValues().concurrencyLevel(1).makeMap();
    }


    public static EventBus fromActivity(Activity pActivity) {
        EventBus eventBus = sEventBuses.get(pActivity);
        if (eventBus == null) {
            eventBus = new AndroidEventBus();
            sEventBuses.put(pActivity, eventBus);
        }
        return eventBus;
    }

    // protected List<Object> mServices;

    // protected AndroidEventBus(AndroidEventBus context) {
    // this();
    // mServices = context.mServices;
    // }

    public AndroidEventBus() {
        mUIThread = Thread.currentThread();
        mEventMap = LinkedListMultimap.create();
        mForwardMap = LinkedListMultimap.create();
        mStopForwarding = false;
        // mServices = new LinkedList<Object>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TListener, TEvent extends BaseEvent<TListener>> //
    void dispatch(final TEvent pEvent) {
        Class<?> eventClass = pEvent.getClass();

        // TODO activity.runOnUiThread
        // Si on n'est pas sur le thread UI...
        if (Thread.currentThread() != mUIThread) {
            throw new IllegalAccessError("Il est interdit d'envoyer un �v�nement depuis un thread non-UI");
        }

        // Dispatch l'�v�nements aux listeners.
        for (final Object listener : mEventMap.get(eventClass)) {
            pEvent.notify((TListener) listener);
            if (mStopForwarding) {
                mStopForwarding = false;
                return;
            }

            // L'ex�cution asynchrone de message ()cad depuis un autre thread) est d�sactiv� car
            // cela ne devrait
            // normalement pas
            // �tre utile (il vaut mieux les envoyer depuis le thread UI). Si cela est vraiment
            // indispensable, il reste
            // possible de
            // d�commenter le code suivant.
            // Il serait int�ressant, dans ce cas, de conserver la distinction threadUI et non-UI
            // car cela permet de
            // d�bugguer plus
            // facilement (l'utilisation d'un UIJob fait que l'on perd l'�tat de la pile d'appel).
            // Cela permet de plus
            // d'avoir un
            // StackOverflow tr�s rapidement en cas de "boucle" d'�v�nements et donc de facilement
            // d�tecter l'erreur.
            // // on utilise un UIJob qui sera lanc� par le Thread de l'UI via un asyncExec
            // new UIJob(eventClass.getName()) {
            // @Override
            // @SuppressWarnings("unchecked")
            // public IStatus runInUIThread(IProgressMonitor monitor) {
            // // job async --> on verifie que le listener est toujours r�ferenc� evite, par ex,
            // qu'au dispose()
            // // d'une view, le desenregistrement d'un listener ne soit pas pris en compte
            // instantan�mment
            // if (_eventMap.containsValue(listener)) {
            // event.notify((TListener) listener);
            // }
            // return Status.OK_STATUS;
            // }
            // }.schedule();
        }

        // Dispatch l'�v�nement aux autres EventManager (si enregistr�).
        for (EventBus forwardEventManager : mForwardMap.get(eventClass)) {
            forwardEventManager.dispatch(pEvent);
            if (mStopForwarding) {
                mStopForwarding = false;
                return;
            }
        }
    }

    @Override
    public <TListener, TEvent extends BaseEvent<TListener>> //
    void dispatchTo(TEvent pEvent, TListener pListener) {
        if (pListener != null) {
            pEvent.notify(pListener);
        }
    }

    @Override
    public void registerListener(Object listener, EventBus... forwardTo) {
        // R�cup�re les interfaces impl�ment�es par le listener.
        List<Class<?>> interfaceClasses = getAllInterfaces(listener.getClass());
        for (Class<?> interfaceClass : interfaceClasses) {
            // R�cup�re les interfaces parentes impl�ment�e par interfaceClass. Attention: fait que
            // sur un seul niveau.
            Class<?>[] interfaceSuperClasses = interfaceClass.getInterfaces();
            for (Class<?> interfaceSuperClass : interfaceSuperClasses) {
                // V�rifie si l'une des interfaces parentes est EventListener.
                // Si c'est le cas, interfaceClass est un Listener destin� � l'EventManager et est
                // donc enregistr�.
                // Il faut cependant que celle-ci soit englob� dans une classe �v�nement (car c'est
                // l�v�nement qui est
                // en fait enregistr�).
                if ((interfaceSuperClass == EventListener.class) && (interfaceClass.getEnclosingClass() != null)) {
                    mEventMap.put(interfaceClass.getEnclosingClass(), listener);
                    for (EventBus forwardEventManager : forwardTo) {
                        mForwardMap.put(interfaceClass.getEnclosingClass(), forwardEventManager);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public <TBaseListener, TListener extends TBaseListener, TEvent extends BaseEvent<TBaseListener>> //
    void addListener(Class<TEvent> event, TListener listener, EventBus... forwardTo) {
        mEventMap.put(event, listener);
        for (EventBus forwardEventManager : forwardTo) {
            mForwardMap.put(event, forwardEventManager);
        }
    }

    @Override
    public <TListener, TEvent extends BaseEvent<TListener>> //
    void forwardTo(Class<TEvent> event, EventBus... forwardTo) {
        for (EventBus forwardEventManager : forwardTo) {
            mForwardMap.put(event, forwardEventManager);
        }
    }

    @Override
    public void unregisterListener(Object listener, EventBus... forwardTo) {
        for (Class<?> event : mEventMap.keys()) {
            mEventMap.remove(event, listener);
            for (EventBus forwardEventManager : forwardTo) {
                mForwardMap.remove(event, forwardEventManager);
            }
        }
    }

    @Override
    public void clearListeners() {
        mEventMap.clear();
        mForwardMap.clear();
    }

    /**
     * Renvoie l'ensemble des interfaces implémentées par une classe, y compris les classes parentes
     * (pas les interfaces parentes des interfaces attention).
     * 
     * @param type classe à analyser
     * @return Liste d'interfaces
     */
    private List<Class<?>> getAllInterfaces(Class<?> pType) {
        List<Class<?>> interfaces = new ArrayList<Class<?>>();
        // Parcours la hiérarchie de classes du paramètre.
        while ((pType != null) && (pType != Object.class)) {
            // Ajoute toutes les interfaces de la classe en cours.
            Class<?>[] currentInterfaces = pType.getInterfaces();
            for (Class<?> i : currentInterfaces) {
                interfaces.add(i);
            }

            // Passe au parent de la classe courante.
            pType = pType.getSuperclass();
        }
        return interfaces;
    }

    /**
     * Enregistre un service.
     * 
     * @param service
     */
    // public void registerService(Object service) {
    // mServices.add(service);
    // }
    //
    // /**
    // * Supprime un service en fonction de son type (ou type dont il h�rite ou qu'il impl�mente).
    // Il est vivement
    // * conseill� d'utiliser une interface pour �viter une d�pendance vers une impl�mentation
    // sp�cifique.
    // *
    // * Attention: si plusieurs classes instance de serviceType existe, tous sont supprim�s.
    // *
    // * @param serviceType Type du service � supprimer.
    // */
    // public void removeService(Class<?> serviceType) {
    // for (Object iservice : mServices) {
    // if (serviceType.isInstance(iservice)) {
    // mServices.remove(iservice);
    // }
    // }
    // }
    //
    // /**
    // * R�cup�re un service selon son type. Il est vivement conseill� d'utiliser une interface pour
    // �viter une d�pendance
    // * vers une impl�mentation sp�cifique.
    // *
    // * Attention: si plusieurs classes instance de serviceType existe, le premier enregistr� est
    // retourn�.
    // *
    // * @param <TService>
    // * @param serviceClass Type de service � r�cup�rer.
    // * @return Service du type demand�.
    // * @throws UnknownServiceException Si le service n'est pas enregistr�.
    // */
    // @SuppressWarnings("unchecked")
    // public <TService> TService getService(Class<TService> serviceClass) {
    // for (Object service : mServices) {
    // if (serviceClass.isInstance(service)) {
    // return (TService) service;
    // }
    // }
    // throw new UnknownServiceException(String.format("%1$s n'est pas un service entregistr�.",
    // serviceClass
    // .getName()));
    // }
}
