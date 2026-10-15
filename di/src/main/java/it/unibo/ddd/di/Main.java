package it.unibo.ddd.di;

import java.math.BigDecimal;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        var service = new InvoiceService();
        service.issue("Mario Rossi", Map.of("book", new BigDecimal("20.00"), "pen", new BigDecimal("1.50")), "USD");
    }
}
