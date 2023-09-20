package it.unibo.ddd.exercises.money;

import java.math.BigDecimal;

public interface Money {
    BigDecimal getAmount();
    Currency getCurrency();
}
