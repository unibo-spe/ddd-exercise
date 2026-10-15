package it.unibo.ddd.exercises.orders;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: remove @Disabled once the types under test exist (cf. step 6 of exercise1/walkthrough.md)
@Disabled("TODO: not implemented yet")
class OrderManagementServiceTest {
    @Test
    void performedOrdersAreStoredAndNotified() {
        // TODO: integration test: inject in-memory repositories, subscribe to events before performing orders
        fail("TODO");
    }

    @Test
    void ordersForUnknownCustomersAreRejected() {
        // TODO: the order is not stored, and no event is published
        fail("TODO");
    }

    @Test
    void ordersForUnknownProductsAreRejected() {
        // TODO: the order is not stored, and no event is published
        fail("TODO");
    }
}
