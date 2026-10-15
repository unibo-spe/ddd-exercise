package it.unibo.ddd.di;

import java.time.Instant;
import java.util.Optional;

/** Keeps track of which cars are parked, and since when. */
public interface EntryRegistry {
    /** @return false if the car is already parked */
    boolean register(String plate, Instant when);

    /** @return when the car entered, or nothing if it is not parked */
    Optional<Instant> remove(String plate);
}
