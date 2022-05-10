package it.polimi.ingsw.notifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotifierTest {

    static class TestDataClass {
        public int value;

        TestDataClass(int value) {
            this.value = value;
        }
    }

    @Test
    public void testNotify() {

        final int THREE = 3;

        // Create a dummy subscriber, that check that the value it receives is 3
        class TestSubscriber implements Subscriber<TestDataClass> {
            @Override
            public void subscribeNotification(TestDataClass newValue) {
                assertEquals(THREE, newValue.value);
            }
        }

        // Creating observable with an initial value
        Notifier<TestDataClass> observable = new Notifier<>();
        // Updating the value: I have not yet registered the dummy subscriber
        observable.notifySubscribers(new TestDataClass(2));

        TestSubscriber dummySubscriber = new TestSubscriber();
        observable.addSubscriber(dummySubscriber);

        // When setting this value, dummySubscriber.subscribeNotification is called
        // And the test check that the value is correctly THREE
        observable.notifySubscribers(new TestDataClass(THREE));
    }
}