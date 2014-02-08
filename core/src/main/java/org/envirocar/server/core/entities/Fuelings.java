package org.envirocar.server.core.entities;

import org.envirocar.server.core.util.UpCastingIterable;

/**
 *
 * A collection of {@code Fueling}s.
 *
 * @author Christian Autermann
 */
public class Fuelings extends UpCastingIterable<Fueling> {
    protected Fuelings(Builder builder) {
        super(builder);
    }

    public static Builder from(Iterable<? extends Fueling> delegate) {
        return new Builder(delegate);
    }

    public static class Builder extends UpCastingIterable.Builder<Builder, Fuelings, Fueling> {

        protected Builder(Iterable<? extends Fueling> delegate) {
            super(delegate);
        }

        @Override
        public Fuelings build() {
            return new Fuelings(this);
        }
    }
}
