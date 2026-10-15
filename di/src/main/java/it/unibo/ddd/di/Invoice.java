package it.unibo.ddd.di;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Invoice(long number, String customer, BigDecimal total, String currency, LocalDate issued, LocalDate due) {
}
