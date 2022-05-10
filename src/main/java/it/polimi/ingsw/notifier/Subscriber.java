package it.polimi.ingsw.notifier;

public interface Subscriber<T> {
    void subscribeNotification(T newValue);
}
