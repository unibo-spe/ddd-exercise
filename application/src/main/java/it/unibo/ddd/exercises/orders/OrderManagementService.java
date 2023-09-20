package it.unibo.ddd.exercises.orders;

import io.reactivex.rxjava3.core.Observable;

public interface OrderManagementService {
    void PerformOrder(Order order);

    Observable<OrderEvent> onOrderPerformed();
}
