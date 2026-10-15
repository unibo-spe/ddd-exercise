package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.money.Currency;
import it.unibo.ddd.exercises.money.Money;
import it.unibo.ddd.exercises.products.ProductID;

import java.time.Instant;

/** Service: computes prices in any currency, at any moment. */
public interface PricingService {
    Money priceOf(ProductID product, Currency currency, Instant when);

    Money totalPrice(Order order, Currency currency, Instant when);
}
