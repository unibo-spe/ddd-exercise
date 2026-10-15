package it.unibo.ddd.di;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

/**
 * The first 15 minutes are free, then each started hour costs 1.50 EUR.
 * All dependencies are received via the constructor: none is created here.
 */
public class ParkingLot {
    static final Duration FREE_TIME = Duration.ofMinutes(15);
    static final BigDecimal HOURLY_FEE = new BigDecimal("1.50");

    private final EntryRegistry entries;
    private final Display display;
    private final Clock clock;

    public ParkingLot(EntryRegistry entries, Display display, Clock clock) {
        this.entries = Objects.requireNonNull(entries);
        this.display = Objects.requireNonNull(display);
        this.clock = Objects.requireNonNull(clock);
    }

    public void enter(String plate) {
        if (!entries.register(plate, clock.instant())) {
            throw new IllegalStateException(plate + " is already parked");
        }
        display.show("Welcome, " + plate);
    }

    public BigDecimal exit(String plate) {
        var entry = entries.remove(plate)
                .orElseThrow(() -> new IllegalStateException(plate + " is not parked"));
        var fee = fee(Duration.between(entry, clock.instant()));
        display.show("Goodbye, " + plate + ": please pay " + fee + " EUR");
        return fee;
    }

    // a pure function is not a hidden dependency: there is no need to inject it
    static BigDecimal fee(Duration stay) {
        if (stay.compareTo(FREE_TIME) <= 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        var startedHours = (stay.toMinutes() + 59) / 60;
        return HOURLY_FEE.multiply(BigDecimal.valueOf(startedHours));
    }
}
