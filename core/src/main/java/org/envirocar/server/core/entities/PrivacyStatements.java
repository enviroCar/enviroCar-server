package org.envirocar.server.core.entities;

import org.envirocar.server.core.util.UpCastingIterable;

public class PrivacyStatements extends UpCastingIterable<PrivacyStatement> {

    protected PrivacyStatements(PrivacyStatements.Builder builder) {
        super(builder);
    }

    public static PrivacyStatements.Builder from(Iterable<? extends PrivacyStatement> delegate) {
        return new PrivacyStatements.Builder(delegate);
    }

    public static class Builder extends UpCastingIterable.Builder<PrivacyStatements.Builder, PrivacyStatements, PrivacyStatement> {

        protected Builder(Iterable<? extends PrivacyStatement> delegate) {
            super(delegate);
        }

        @Override
        public PrivacyStatements build() {
            return new PrivacyStatements(this);
        }
    }

}
