package it.unibo.ddd.di;

import java.time.Clock;

public class Main {
    public static void main(String[] args) {
        // the composition root: the only place deciding which implementations to use
        var lot = new ParkingLot(new InMemoryEntryRegistry(), System.out::println, Clock.systemDefaultZone());
        lot.enter("AB123CD");
        lot.exit("AB123CD");
    }
}
