package it.unibo.ddd.exercises.orders;

import io.reactivex.rxjava3.core.Observable;

/** Service: registers orders and publishes {@link OrderEvent}s, so that other components may react. */
public interface OrderManagementService {
    /**
     * @throws IllegalArgumentException if the order is empty, or it refers to unknown customers or products
     */
    void performOrder(Order order);

    /** @throws IllegalStateException if the order has already been delivered */
    void cancelOrder(Order order);

    Observable<OrderEvent> orderEvents();
}
