package it.polimi.ingsw.observe;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    T value;
    List<Subscriber<T>> subscriberList;

    public Observable(T initialValue) {
        value = initialValue;
        subscriberList = new ArrayList<>(5);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        value = newValue;

        notifySubscribers();
    }

    public void addSubscriber(Subscriber<T> newSubscriber) {
        subscriberList.add(newSubscriber);
    }

    public void notifySubscribers() {
        for (Subscriber<T> subscriber : subscriberList) {
            subscriber.subscribeNotification(getValue());
        }
    }
}
