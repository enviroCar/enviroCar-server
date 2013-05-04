package io.car.server.core;

import java.util.Iterator;

public class Measurements implements Iterable<Measurement>{
    private final Iterable<? extends Measurement> delegate;

    public Measurements(Iterable<? extends Measurement> delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Measurement> iterator() {
        return (Iterator<Measurement>) delegate.iterator();
    }

}
