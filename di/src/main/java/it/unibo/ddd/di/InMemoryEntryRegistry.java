package it.unibo.ddd.di;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryEntryRegistry implements EntryRegistry {
    private final Map<String, Instant> entries = new HashMap<>();

    @Override
    public boolean register(String plate, Instant when) {
        return entries.putIfAbsent(plate, when) == null;
    }

    @Override
    public Optional<Instant> remove(String plate) {
        return Optional.ofNullable(entries.remove(plate));
    }
}
