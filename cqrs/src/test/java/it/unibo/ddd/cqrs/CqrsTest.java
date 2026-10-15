package it.unibo.ddd.cqrs;

import it.unibo.ddd.cqrs.impl.CounterReaderImpl;
import it.unibo.ddd.cqrs.impl.CounterWriterImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

// TODO: remove @Disabled once the implementations are ready (cf. cqrs/walkthrough.md)
@Disabled("TODO: not implemented yet")
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
        // TODO: setValue(5), then setValue(3), then check that the variations are exactly
        //       Variation.of(0, 5, T0) and Variation.of(5, 3, T0 + 1 minute)
        fail("TODO");
    }

    @Test
    void readerReconstructsPastValues() {
        var writer = new CounterWriterImpl(tickingClock());
        var reader = new CounterReaderImpl(writer);
        // TODO: set 3 values, then query the reader before T0, exactly at T0, between two variations, and long after
        fail("TODO");
    }

    @Test
    void readerCreatedLaterReplaysHistory() {
        // TODO: set some values, then create the reader: it must know the past too
        fail("TODO");
    }
}
