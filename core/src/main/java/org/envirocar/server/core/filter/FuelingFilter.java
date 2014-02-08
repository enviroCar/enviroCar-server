package org.envirocar.server.core.filter;

import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class FuelingFilter {

    private final User user;
    private final TemporalFilter temporalFilter;
    private final Pagination pagination;

    public FuelingFilter(User u, TemporalFilter tf, Pagination p) {
        this.user = u;
        this.pagination = p;
        this.temporalFilter = tf;
    }

    public FuelingFilter(User u, Pagination p) {
        this(u, null, p);
    }

    public User getUser() {
        return user;
    }

    public boolean hasUser() {
        return user != null;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public boolean hasPagination() {
        return pagination != null;
    }

    public TemporalFilter getTemporalFilter() {
        return temporalFilter;
    }

    public boolean hasTemporalFilter() {
        return temporalFilter != null;
    }
}
