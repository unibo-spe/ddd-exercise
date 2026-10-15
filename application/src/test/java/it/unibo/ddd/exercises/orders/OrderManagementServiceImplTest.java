package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.CustomerFactory;
import it.unibo.ddd.exercises.customers.CustomerRegistry;
import it.unibo.ddd.exercises.customers.impl.CustomerFactoryImpl;
import it.unibo.ddd.exercises.customers.impl.InMemoryCustomerRegistry;
import it.unibo.ddd.exercises.money.impl.MoneyFactoryImpl;
import it.unibo.ddd.exercises.orders.impl.InMemoryOrderStore;
import it.unibo.ddd.exercises.orders.impl.OrderFactoryImpl;
import it.unibo.ddd.exercises.orders.impl.OrderManagementServiceImpl;
import it.unibo.ddd.exercises.products.ProductID;
import it.unibo.ddd.exercises.products.Warehouse;
import it.unibo.ddd.exercises.products.impl.InMemoryWarehouse;
import it.unibo.ddd.exercises.products.impl.ProductFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** Integration test: the service is wired with real (in-memory) repositories, the test acts as composition root. */
class OrderManagementServiceImplTest {
    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");
    private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
    private final CustomerFactory customerFactory = new CustomerFactoryImpl();
    private final ProductID pen = new ProductFactoryImpl().productID("pen", "stationery");

    private OrderStore store;
    private OrderFactory orderFactory;
    private OrderManagementService service;
    private List<OrderEvent> events;

    @BeforeEach
    void setUp() {
        CustomerRegistry customers = new InMemoryCustomerRegistry();
        customers.addNewCustomer(customerFactory.newCustomerPerson(
                customerFactory.taxCode("RSSMRA80A01H501U"), "Mario Rossi", "mario@rossi.it"));
        Warehouse warehouse = new InMemoryWarehouse();
        var money = new MoneyFactoryImpl();
        warehouse.addNewProduct(new ProductFactoryImpl().newProduct(pen, money.money(BigDecimal.ONE, money.eur()), 100));
        store = new InMemoryOrderStore();
        orderFactory = new OrderFactoryImpl(clock);
        service = new OrderManagementServiceImpl(store, customers, warehouse, clock);
        events = new ArrayList<>();
        service.orderEvents().subscribe(events::add);
    }

    @Test
    void performedOrdersAreStoredAndNotified() {
        var order = orderFactory.newOrder(customerFactory.taxCode("RSSMRA80A01H501U"));
        order.addProduct(pen, 2);
        service.performOrder(order);
        assertEquals(order, store.findOrderById(order.getID()).orElseThrow());
        assertEquals(
                List.of(new OrderEvent(OrderEvent.Kind.PLACED, order.getID(), order.getCustomer(), NOW, Map.of(pen, 2L))),
                events
        );
    }

    @Test
    void cancelledOrdersAreRemovedAndNotified() {
        var order = orderFactory.newOrder(customerFactory.taxCode("RSSMRA80A01H501U"));
        order.addProduct(pen, 2);
        service.performOrder(order);
        service.cancelOrder(order);
        assertTrue(store.findOrderById(order.getID()).isEmpty());
        assertEquals(OrderEvent.Kind.CANCELLED, events.get(1).kind());
    }

    @Test
    void ordersForUnknownCustomersAreRejected() {
        var order = orderFactory.newOrder(customerFactory.taxCode("VRDLGU80A01H501U"));
        order.addProduct(pen, 1);
        assertThrows(IllegalArgumentException.class, () -> service.performOrder(order));
        assertTrue(events.isEmpty());
    }

    @Test
    void ordersForUnknownProductsAreRejected() {
        var order = orderFactory.newOrder(customerFactory.taxCode("RSSMRA80A01H501U"));
        order.addProduct(new ProductFactoryImpl().productID("unicorn", "toys"), 1);
        assertThrows(IllegalArgumentException.class, () -> service.performOrder(order));
        assertTrue(events.isEmpty());
    }
}
