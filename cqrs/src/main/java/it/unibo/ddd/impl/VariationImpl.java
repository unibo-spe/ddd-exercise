package it.unibo.ddd.impl;

import it.unibo.ddd.Variation;

import java.util.Date;

public record VariationImpl(long before, long after, Date when) implements Variation {
}
