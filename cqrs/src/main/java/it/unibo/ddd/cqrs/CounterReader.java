package it.unibo.ddd.cqrs;

import java.time.Instant;

/** Read model: answers queries about the value of the counter, now or in the past. */
public interface CounterReader {
    long getValue();

    /** @return the value of the counter at the given moment (0 if it was never set before) */
    long getValue(Instant at);
}
