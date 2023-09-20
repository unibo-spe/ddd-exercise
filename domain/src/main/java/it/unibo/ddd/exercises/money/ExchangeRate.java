package it.unibo.ddd.exercises.money;

import java.util.Date;

public interface ExchangeRate {
    Currency getSource();
    Currency getDestination();
    double getRate();
    Date getWhen();
}
