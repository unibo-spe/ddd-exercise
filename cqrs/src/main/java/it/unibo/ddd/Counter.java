package it.unibo.ddd;

import io.reactivex.rxjava3.core.Observable;

/** Repository memorising the current value of a counter (a snapshot), initially 0. */
public interface Counter {
    long getValue();

    /** Publishes a {@link Variation} via {@link #onVariation()}. */
    void setValue(long value);

    Observable<Variation> onVariation();
}
