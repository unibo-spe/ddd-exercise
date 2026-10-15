package it.unibo.ddd.exercises.orders.impl;

import it.unibo.ddd.exercises.orders.OrderID;

record OrderIDImpl(long serialNumber) implements OrderID {
    @Override
    public long getSerialNumber() {
        return serialNumber;
    }
}
