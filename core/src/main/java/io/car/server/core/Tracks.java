package io.car.server.core;

import java.util.Iterator;

public class Tracks implements Iterable<Track>{
    private final Iterable<? extends Track> delegate;

    public Tracks(Iterable<? extends Track> delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Track> iterator() {
        return (Iterator<Track>) delegate.iterator();
    }
}
