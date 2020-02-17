/*
 * Copyright (C) 2013-2020 The enviroCar project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.envirocar.server.mongo.dao;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.envirocar.server.core.dao.PrivacyStatementDao;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.PrivacyStatements;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoPrivacyStatement;
import org.envirocar.server.mongo.util.MongoUtils;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author Christian Autermann
 */
public class MongoPrivacyStatementDao extends AbstractMongoDao<ObjectId, MongoPrivacyStatement, PrivacyStatements>
        implements PrivacyStatementDao {

    @Inject
    public MongoPrivacyStatementDao(MongoDB mongoDB) {
        super(MongoPrivacyStatement.class, mongoDB);
    }

    @Override
    public long getCount() {
        return count();
    }

    @Override
    public PrivacyStatements get(Pagination p) {
        return fetch(q().order(MongoUtils.reverse(MongoPrivacyStatement.CREATION_DATE)), p);
    }

    @Override
    protected PrivacyStatements createPaginatedIterable(
            Iterable<MongoPrivacyStatement> i, Pagination p, long count) {
        return PrivacyStatements.from(i).withPagination(p).withElements(count).build();
    }

    @Override
    public Optional<PrivacyStatement> getById(String id) {
        ObjectId oid;
        try {
            oid = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(super.get(oid));
    }

    @Override
    public Optional<PrivacyStatement> getLatest() {
        Sort order = Sort.descending(MongoPrivacyStatement.ISSUED_DATE);
        FindOptions options = new FindOptions().limit(1);
        Iterator<MongoPrivacyStatement> privacyStatements = fetch(q().order(order), options).iterator();
        if (privacyStatements.hasNext()) {
            return Optional.of(privacyStatements.next());
        }
        return Optional.empty();
    }

}
