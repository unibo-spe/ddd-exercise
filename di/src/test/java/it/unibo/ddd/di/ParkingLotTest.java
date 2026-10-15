package it.unibo.ddd.di;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Each test builds its own parking lot, with its own test doubles: no waiting, no shared state. */
class ParkingLotTest {
    private static final Instant T0 = Instant.parse("2026-01-15T10:00:00Z");

    private final Clock clock = mock(Clock.class);                 // stub: we decide what time it is
    private final Display display = mock(Display.class);           // mock: we verify what is shown
    private final EntryRegistry entries = new InMemoryEntryRegistry(); // fake: real, yet in memory
    private final ParkingLot lot = new ParkingLot(entries, display, clock);

    private BigDecimal stay(String plate, Duration duration) {
        when(clock.instant()).thenReturn(T0, T0.plus(duration));
        lot.enter(plate);
        return lot.exit(plate);
    }

    @Test
    void shortStaysAreFree() {
        assertEquals(new BigDecimal("0.00"), stay("AB123CD", Duration.ofMinutes(15)));
    }

    @Test
    void eachStartedHourIsPaid() {
        assertEquals(new BigDecimal("1.50"), stay("AB123CD", Duration.ofMinutes(16)));
        assertEquals(new BigDecimal("1.50"), stay("AB123CD", Duration.ofMinutes(60)));
        assertEquals(new BigDecimal("3.00"), stay("AB123CD", Duration.ofMinutes(61)));
    }

    @Test
    void driversAreGreeted() {
        stay("AB123CD", Duration.ofHours(2));
        verify(display).show("Welcome, AB123CD");
        verify(display).show("Goodbye, AB123CD: please pay 3.00 EUR");
    }

    @Test
    void carsCannotEnterTwice() {
        when(clock.instant()).thenReturn(T0);
        lot.enter("AB123CD");
        assertThrows(IllegalStateException.class, () -> lot.enter("AB123CD"));
    }

    @Test
    void parkingLotsDoNotShareState() {
        when(clock.instant()).thenReturn(T0);
        lot.enter("AB123CD");
        var other = new ParkingLot(new InMemoryEntryRegistry(), display, clock);
        assertThrows(IllegalStateException.class, () -> other.exit("AB123CD"));
    }
}
