package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.products.ProductID;

public interface OrderEvent {
    OrderID getOrderID();
    ProductID getProductID();
    int getQuantity();
}
