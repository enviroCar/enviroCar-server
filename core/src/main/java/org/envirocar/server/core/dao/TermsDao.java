package org.envirocar.server.core.dao;

import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.core.util.UpCastingIterable;
import org.envirocar.server.core.util.pagination.Pagination;

public interface TermsDao<T extends Terms, V extends UpCastingIterable<T>> {
    V get(Pagination p);

    T getById(String id);

    T getLatest();
}
