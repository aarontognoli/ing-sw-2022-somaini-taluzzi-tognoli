package it.polimi.ingsw.observe;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObservableTest {

    static class TestDataClass {
        public int value;

        TestDataClass(int value) {
            this.value = value;
        }
    }

    @Test
    public void testSubscription() {

        final int THREE = 3;

        // Create a dummy subscriber, that check that the value it receives is 3
        class TestSubscriber implements Subscriber<TestDataClass> {
            @Override
            public void subscribeNotification(TestDataClass newValue) {
                assertEquals(THREE, newValue.value);
            }
        }

        // Creating observable with an initial value
        Observable<TestDataClass> observable = new Observable<>(new TestDataClass(1));
        // Updating the value: I have not yet registered the dummy subscriber
        observable.setValue(new TestDataClass(2));

        TestSubscriber dummySubscriber = new TestSubscriber();
        observable.addSubscriber(dummySubscriber);

        // When setting this value, dummySubscriber.subscribeNotification is called
        // And the test check that the value is correctly THREE
        observable.setValue(new TestDataClass(THREE));
    }
}