package it.unibo.ddd.exercises.products;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: remove @Disabled once the types under test exist (cf. step 3 of exercise1/walkthrough.md)
@Disabled("TODO: not implemented yet")
class ProductsTest {
    @Test
    void productsAreEqualIffTheirIdentifiersAreEqual() {
        // TODO: two products with the same identifier are equal, even if their prices differ
        fail("TODO");
    }

    @Test
    void productsEnforceTheirInvariants() {
        // TODO: negative prices or quantities are rejected, both at creation and when set
        fail("TODO");
    }

    @Test
    void repositoryStoresAndRetrievesProducts() {
        // TODO: products are found by identifier, duplicates are refused, removed products are forgotten
        fail("TODO");
    }
}
