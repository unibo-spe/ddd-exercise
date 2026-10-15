package it.unibo.ddd.di;

/** Where messages for drivers are shown. */
@FunctionalInterface
public interface Display {
    void show(String message);
}
