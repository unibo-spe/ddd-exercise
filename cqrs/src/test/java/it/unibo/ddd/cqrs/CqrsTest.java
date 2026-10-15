package it.unibo.ddd.cqrs;

import it.unibo.ddd.Variation;
import it.unibo.ddd.cqrs.impl.CounterReaderImpl;
import it.unibo.ddd.cqrs.impl.CounterWriterImpl;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CqrsTest {
    private static final Instant T0 = Instant.parse("2026-01-01T10:00:00Z");

    /** A clock that moves 1 minute ahead each time it is read. */
    private static Clock tickingClock() {
        return new Clock() {
            private Instant now = T0;

            @Override
            public Instant instant() {
                var result = now;
                now = now.plus(Duration.ofMinutes(1));
                return result;
            }

            @Override
            public ZoneId getZone() {
                return ZoneOffset.UTC;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    void writerMemorisesVariations() {
        var writer = new CounterWriterImpl(tickingClock());
        writer.setValue(5);
        writer.setValue(3);
        assertEquals(List.of(
                Variation.of(0, 5, T0),
                Variation.of(5, 3, T0.plus(Duration.ofMinutes(1)))
        ), writer.getVariations());
    }

    @Test
    void readerReconstructsPastValues() {
        var writer = new CounterWriterImpl(tickingClock());
        var reader = new CounterReaderImpl(writer);
        assertEquals(0, reader.getValue());
        writer.setValue(5);  // at T0
        writer.setValue(3);  // at T0 + 1 min
        writer.setValue(10); // at T0 + 2 min
        assertEquals(10, reader.getValue());
        assertEquals(0, reader.getValue(T0.minusSeconds(1)));
        assertEquals(5, reader.getValue(T0));
        assertEquals(5, reader.getValue(T0.plusSeconds(30)));
        assertEquals(3, reader.getValue(T0.plus(Duration.ofMinutes(1))));
        assertEquals(10, reader.getValue(T0.plus(Duration.ofDays(1))));
    }

    @Test
    void readerCreatedLaterReplaysHistory() {
        var writer = new CounterWriterImpl(tickingClock());
        writer.setValue(5);
        writer.setValue(3);
        var reader = new CounterReaderImpl(writer);
        assertEquals(5, reader.getValue(T0));
        assertEquals(3, reader.getValue());
    }
}
