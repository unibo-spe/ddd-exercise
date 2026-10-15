package it.unibo.ddd.exercises.orders;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: remove @Disabled once the types under test exist (cf. step 4 of exercise1/walkthrough.md)
@Disabled("TODO: not implemented yet")
class OrdersTest {
    @Test
    void factoryAssignsFreshIdsAndCurrentTime() {
        // TODO: use Clock.fixed(...) to know which time to expect
        fail("TODO");
    }

    @Test
    void aggregateRootControlsItsItems() {
        // TODO: adding the same product twice sums amounts, non-positive amounts are rejected, items cannot be changed from outside the root
        fail("TODO");
    }

    @Test
    void deliveredOrdersCannotBeModified() {
        // TODO: any modification of a delivered order fails
        fail("TODO");
    }
}
