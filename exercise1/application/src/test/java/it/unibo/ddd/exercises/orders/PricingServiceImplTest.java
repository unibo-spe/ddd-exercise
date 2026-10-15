package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.impl.CustomerFactoryImpl;
import it.unibo.ddd.exercises.money.CurrencyExchangeService;
import it.unibo.ddd.exercises.money.MoneyFactory;
import it.unibo.ddd.exercises.money.impl.MoneyFactoryImpl;
import it.unibo.ddd.exercises.orders.impl.OrderFactoryImpl;
import it.unibo.ddd.exercises.orders.impl.PricingServiceImpl;
import it.unibo.ddd.exercises.products.ProductFactory;
import it.unibo.ddd.exercises.products.Warehouse;
import it.unibo.ddd.exercises.products.impl.InMemoryWarehouse;
import it.unibo.ddd.exercises.products.impl.ProductFactoryImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceImplTest {
    private final MoneyFactory money = new MoneyFactoryImpl();
    private final ProductFactory products = new ProductFactoryImpl();
    private final Instant when = Instant.parse("2026-01-01T10:00:00Z");

    // test double: a stub replacing the actual (Internet-based) exchange service, 1 EUR = 2 USD
    private final CurrencyExchangeService exchange = (source, destination, at) -> {
        assertEquals(when, at);
        var rate = source.equals(money.eur()) ? new BigDecimal("2") : new BigDecimal("0.5");
        return money.exchangeRate(source, destination, rate, at);
    };

    @Test
    void totalPriceConvertsEachItemToTheRequestedCurrency() {
        Warehouse warehouse = new InMemoryWarehouse();
        var pen = products.productID("pen", "stationery");
        var book = products.productID("book", "stationery");
        warehouse.addNewProduct(products.newProduct(pen, money.money(new BigDecimal("1.50"), money.eur()), 10));
        warehouse.addNewProduct(products.newProduct(book, money.money(new BigDecimal("20"), money.usd()), 10));

        var order = new OrderFactoryImpl(Clock.systemUTC()).newOrder(new CustomerFactoryImpl().vatNumber("01234567890"));
        order.addProduct(pen, 4);
        order.addProduct(book, 1);

        var service = new PricingServiceImpl(warehouse, exchange, money);
        assertEquals(money.money(new BigDecimal("16"), money.eur()), service.totalPrice(order, money.eur(), when));
        assertEquals(money.money(new BigDecimal("32"), money.usd()), service.totalPrice(order, money.usd(), when));
    }
}
