package it.unibo.ddd.exercises.orders;

/** Value object: orders are identified by a serial number. */
public interface OrderID {
    long getSerialNumber();
}
