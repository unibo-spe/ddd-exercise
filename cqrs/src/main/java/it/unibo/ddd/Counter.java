package it.unibo.ddd;

import io.reactivex.rxjava3.core.Observable;

public interface Counter {
    long getValue();
    void setValue(long value);

    Observable<Variation> onVariation();
}
