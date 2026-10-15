package it.unibo.ddd.exercises.orders.impl;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import it.unibo.ddd.exercises.customers.CustomerRegistry;
import it.unibo.ddd.exercises.orders.Order;
import it.unibo.ddd.exercises.orders.OrderEvent;
import it.unibo.ddd.exercises.orders.OrderManagementService;
import it.unibo.ddd.exercises.orders.OrderStore;
import it.unibo.ddd.exercises.products.Warehouse;

import java.time.Clock;
import java.util.Objects;

public class OrderManagementServiceImpl implements OrderManagementService {
    private final OrderStore orders;
    private final CustomerRegistry customers;
    private final Warehouse warehouse;
    private final Clock clock;
    private final Subject<OrderEvent> events = PublishSubject.create();

    // constructor injection: dependencies are received, not created
    public OrderManagementServiceImpl(OrderStore orders, CustomerRegistry customers, Warehouse warehouse, Clock clock) {
        this.orders = Objects.requireNonNull(orders);
        this.customers = Objects.requireNonNull(customers);
        this.warehouse = Objects.requireNonNull(warehouse);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public void performOrder(Order order) {
        if (order.getAmounts().isEmpty()) {
            throw new IllegalArgumentException("Empty order: " + order);
        }
        if (customers.findCustomerById(order.getCustomer()).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer: " + order.getCustomer());
        }
        for (var product : order.getAmounts().keySet()) {
            if (warehouse.findProductById(product).isEmpty()) {
                throw new IllegalArgumentException("Unknown product: " + product);
            }
        }
        orders.addNewOrder(order);
        events.onNext(OrderEvent.of(OrderEvent.Kind.PLACED, order, clock.instant()));
    }

    @Override
    public void cancelOrder(Order order) {
        if (order.isDelivered()) {
            throw new IllegalStateException("Cannot cancel a delivered order: " + order);
        }
        orders.removeOrder(order);
        events.onNext(OrderEvent.of(OrderEvent.Kind.CANCELLED, order, clock.instant()));
    }

    @Override
    public Observable<OrderEvent> orderEvents() {
        return events;
    }
}
