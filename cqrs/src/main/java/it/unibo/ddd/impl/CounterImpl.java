package it.unibo.ddd.impl;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import it.unibo.ddd.Counter;
import it.unibo.ddd.Variation;

import java.time.Clock;
import java.util.Objects;

public class CounterImpl implements Counter {
    private final Clock clock;
    private long value;
    private final Subject<Variation> onVariation = PublishSubject.create();

    public CounterImpl(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    public CounterImpl() {
        this(Clock.systemUTC());
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public void setValue(long value) {
        final var oldValue = this.value;
        this.value = value;
        onVariation.onNext(Variation.of(oldValue, value, clock.instant()));
    }

    @Override
    public Observable<Variation> onVariation() {
        return onVariation;
    }
}
