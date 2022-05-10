package it.polimi.ingsw.notifier;

import java.util.ArrayList;
import java.util.List;

public class Notifier<T> {
    List<Subscriber<T>> subscriberList;

    public Notifier() {
        subscriberList = new ArrayList<>(5);
    }

    public void addSubscriber(Subscriber<T> newSubscriber) {
        subscriberList.add(newSubscriber);
    }

    public void notifySubscribers(T newValue) {
        for (Subscriber<T> subscriber : subscriberList) {
            subscriber.subscribeNotification(newValue);
        }
    }
}
