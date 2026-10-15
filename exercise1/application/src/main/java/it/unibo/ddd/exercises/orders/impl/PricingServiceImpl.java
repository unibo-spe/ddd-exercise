package it.unibo.ddd.exercises.orders.impl;

import it.unibo.ddd.exercises.money.Currency;
import it.unibo.ddd.exercises.money.CurrencyExchangeService;
import it.unibo.ddd.exercises.money.Money;
import it.unibo.ddd.exercises.money.MoneyFactory;
import it.unibo.ddd.exercises.orders.Order;
import it.unibo.ddd.exercises.orders.PricingService;
import it.unibo.ddd.exercises.products.ProductID;
import it.unibo.ddd.exercises.products.Warehouse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class PricingServiceImpl implements PricingService {
    private final Warehouse warehouse;
    private final CurrencyExchangeService exchange;
    private final MoneyFactory moneyFactory;

    public PricingServiceImpl(Warehouse warehouse, CurrencyExchangeService exchange, MoneyFactory moneyFactory) {
        this.warehouse = Objects.requireNonNull(warehouse);
        this.exchange = Objects.requireNonNull(exchange);
        this.moneyFactory = Objects.requireNonNull(moneyFactory);
    }

    @Override
    public Money priceOf(ProductID product, Currency currency, Instant when) {
        var price = warehouse.findProductById(product)
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + product))
                .getPrice();
        if (price.getCurrency().equals(currency)) {
            return price;
        }
        return exchange.getExchangeRate(price.getCurrency(), currency, when).convert(price);
    }

    @Override
    public Money totalPrice(Order order, Currency currency, Instant when) {
        var total = moneyFactory.money(BigDecimal.ZERO, currency);
        for (var item : order.getAmounts().entrySet()) {
            total = total.plus(priceOf(item.getKey(), currency, when).times(item.getValue()));
        }
        return total;
    }
}
