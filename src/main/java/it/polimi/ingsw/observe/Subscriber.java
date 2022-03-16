package it.polimi.ingsw.observe;

public interface Subscriber<T> {
    void subscribeNotification(T newValue);
}
