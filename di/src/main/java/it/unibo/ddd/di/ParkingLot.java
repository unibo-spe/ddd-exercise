package it.unibo.ddd.di;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * The first 15 minutes are free, then each started hour costs 1.50 EUR.
 */
public class ParkingLot {
    static final Duration FREE_TIME = Duration.ofMinutes(15);
    static final BigDecimal HOURLY_FEE = new BigDecimal("1.50");

    private static final Map<String, Instant> ENTRIES = new HashMap<>();

    public void enter(String plate) {
        if (ENTRIES.putIfAbsent(plate, Instant.now()) != null) {
            throw new IllegalStateException(plate + " is already parked");
        }
        System.out.println("Welcome, " + plate);
    }

    public BigDecimal exit(String plate) {
        var entry = ENTRIES.remove(plate);
        if (entry == null) {
            throw new IllegalStateException(plate + " is not parked");
        }
        var fee = fee(Duration.between(entry, Instant.now()));
        System.out.println("Goodbye, " + plate + ": please pay " + fee + " EUR");
        return fee;
    }

    static BigDecimal fee(Duration stay) {
        if (stay.compareTo(FREE_TIME) <= 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        var startedHours = (stay.toMinutes() + 59) / 60;
        return HOURLY_FEE.multiply(BigDecimal.valueOf(startedHours));
    }
}
