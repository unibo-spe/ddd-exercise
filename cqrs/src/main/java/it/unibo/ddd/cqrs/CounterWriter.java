package it.unibo.ddd.cqrs;

import io.reactivex.rxjava3.core.Observable;
import it.unibo.ddd.Variation;

/**
 * Write model: receives commands, and memorises the resulting {@link Variation}s (event sourcing)
 * instead of the current value of the counter.
 */
public interface CounterWriter {
    /** Command: records a new {@link Variation} and publishes it via {@link #onVariation()}. */
    void setValue(long value);

    /** The whole history of the counter, in chronological order. */
    Iterable<Variation> getVariations();

    Observable<Variation> onVariation();
}
