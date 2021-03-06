package com.github.piasy.testunderstand.rx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Subscription;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Piasy{github.com/Piasy} on 5/5/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class TakeTest {
    @Mock
    EventTracker mEventTracker;

    @Test
    public void testFirst() {
        ReplaySubject<Integer> subject = ReplaySubject.create();
        TestSubscriber<Integer> subscriber = new TestSubscriber<>();

        Subscription subscription = subject.asObservable()
                .subscribeOn(Schedulers.from(command -> {
                    System.out.println("start subscriber thread");
                    new Thread(command).start();
                }))
                .doOnSubscribe(() -> System.out.println("doOnSubscribe"))
                .doOnUnsubscribe(() -> System.out.println("doOnUnsubscribe"))
                .filter(integer -> {
                    System.out.println("filter: " + integer);
                    mEventTracker.track();
                    return integer > 5;
                })
                .first()
                .subscribe(subscriber);

        System.out.println("start emit");
        System.out.println("to subscribe 1, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(1);
        System.out.println("to subscribe 3, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(3);
        System.out.println("to subscribe 5, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(5);
        System.out.println("to subscribe 7, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(7);
        System.out.println("to subscribe 9, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(9);
        System.out.println("to subscribe 1, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(1);
        System.out.println("to subscribe 3, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(3);
        System.out.println("to subscribe 5, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(5);
        System.out.println("finish emit");

        subscriber.awaitTerminalEvent();
        System.out.println("awaitTerminalEvent");

        // all event will go to filter operator, even the observable is unsubscribed
        verify(mEventTracker, times(4)).track();
    }

    @Test
    public void testTake() {
        ReplaySubject<Integer> subject = ReplaySubject.create();
        TestSubscriber<Integer> subscriber = new TestSubscriber<>();

        Subscription subscription = subject.asObservable()
                .subscribeOn(Schedulers.from(command -> {
                    System.out.println("start subscriber thread");
                    new Thread(command).start();
                }))
                .doOnSubscribe(() -> System.out.println("doOnSubscribe"))
                .doOnUnsubscribe(() -> System.out.println("doOnUnsubscribe"))
                .filter(integer -> {
                    System.out.println("filter: " + integer);
                    mEventTracker.track();
                    return integer > 5;
                })
                .take(1)
                .subscribe(subscriber);

        System.out.println("start emit");
        System.out.println("to subscribe 1, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(1);
        System.out.println("to subscribe 3, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(3);
        System.out.println("to subscribe 5, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(5);
        System.out.println("to subscribe 7, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(7);
        System.out.println("to subscribe 9, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(9);
        System.out.println("to subscribe 1, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(1);
        System.out.println("to subscribe 3, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(3);
        System.out.println("to subscribe 5, unsubscribed? " + subscription.isUnsubscribed());
        subject.onNext(5);
        System.out.println("finish emit");

        subscriber.awaitTerminalEvent();
        System.out.println("awaitTerminalEvent");

        // all event will go to filter operator, even the observable is unsubscribed
        verify(mEventTracker, times(4)).track();
    }

    interface EventTracker {
        void track();
    }
}
