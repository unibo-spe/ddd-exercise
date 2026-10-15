package it.unibo.ddd.exercises.orders.impl;

import it.unibo.ddd.exercises.customers.CustomerID;
import it.unibo.ddd.exercises.orders.Order;
import it.unibo.ddd.exercises.orders.OrderFactory;

import java.time.Clock;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class OrderFactoryImpl implements OrderFactory {
    private final Clock clock;
    private final AtomicLong nextSerialNumber;

    /** The clock is injected, so that tests can control what "now" means. */
    public OrderFactoryImpl(Clock clock, long firstSerialNumber) {
        this.clock = Objects.requireNonNull(clock);
        this.nextSerialNumber = new AtomicLong(firstSerialNumber);
    }

    public OrderFactoryImpl(Clock clock) {
        this(clock, 1);
    }

    @Override
    public Order newOrder(CustomerID customer) {
        return new OrderImpl(new OrderIDImpl(nextSerialNumber.getAndIncrement()), customer, clock.instant());
    }
}
