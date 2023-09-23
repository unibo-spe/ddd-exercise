package it.unibo.ddd.cqrs;

import io.reactivex.rxjava3.core.Observable;
import it.unibo.ddd.Variation;

import java.util.Date;

public interface CounterReader {
    long getValue(Date at);
}
