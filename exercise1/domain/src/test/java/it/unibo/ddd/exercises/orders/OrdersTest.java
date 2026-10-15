package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.CustomerID;
import it.unibo.ddd.exercises.customers.impl.CustomerFactoryImpl;
import it.unibo.ddd.exercises.orders.impl.InMemoryOrderStore;
import it.unibo.ddd.exercises.orders.impl.OrderFactoryImpl;
import it.unibo.ddd.exercises.products.ProductID;
import it.unibo.ddd.exercises.products.impl.ProductFactoryImpl;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrdersTest {
    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

    private final OrderFactory factory = new OrderFactoryImpl(Clock.fixed(NOW, ZoneOffset.UTC));
    private final CustomerID mario = new CustomerFactoryImpl().taxCode("RSSMRA80A01H501U");
    private final ProductID pen = new ProductFactoryImpl().productID("pen", "stationery");
    private final ProductID book = new ProductFactoryImpl().productID("book", "stationery");

    @Test
    void factoryAssignsFreshIdsAndCurrentTime() {
        var first = factory.newOrder(mario);
        var second = factory.newOrder(mario);
        assertNotEquals(first.getID(), second.getID());
        assertEquals(NOW, first.getTimestamp());
        assertTrue(first.getAmounts().isEmpty());
    }

    @Test
    void aggregateRootControlsItsItems() {
        var order = factory.newOrder(mario);
        order.addProduct(pen, 2);
        order.addProduct(pen, 3);
        order.addProduct(book, 1);
        assertEquals(Map.of(pen, 5L, book, 1L), order.getAmounts());
        assertThrows(UnsupportedOperationException.class, () -> order.getAmounts().put(pen, 100L));
        assertThrows(IllegalArgumentException.class, () -> order.addProduct(pen, 0));
        order.removeProduct(book);
        assertEquals(Map.of(pen, 5L), order.getAmounts());
    }

    @Test
    void deliveredOrdersCannotBeModified() {
        var order = factory.newOrder(mario);
        order.addProduct(pen, 1);
        order.markDelivered();
        assertThrows(IllegalStateException.class, () -> order.addProduct(book, 1));
        assertThrows(IllegalStateException.class, () -> order.setTimestamp(Instant.EPOCH));
    }

    @Test
    void storeFindsOrdersByCustomer() {
        OrderStore store = new InMemoryOrderStore();
        var order = factory.newOrder(mario);
        store.addNewOrder(order);
        assertEquals(List.of(order), store.findOrdersByCustomer(mario));
        assertEquals(order, store.findOrderById(order.getID()).orElseThrow());
    }
}
