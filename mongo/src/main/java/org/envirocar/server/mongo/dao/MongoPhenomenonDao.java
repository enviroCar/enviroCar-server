/*
 * Copyright (C) 2013-2022 The enviroCar project
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
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Phenomenons;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoSensor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class MongoPhenomenonDao extends AbstractMongoDao<String, MongoPhenomenon, Phenomenons>
        implements PhenomenonDao {
    @Inject
    public MongoPhenomenonDao(MongoDB mongoDB) {
        super(MongoPhenomenon.class, mongoDB);
    }

    @Override
    public MongoPhenomenon getByName(String name) {
        return q().field(MongoSensor.ID).equal(name).get();
    }

    @Override
    public Phenomenons get(Pagination p) {
        return fetch(q(), p);
    }

    @Override
    public long getCount() {
        return count();
    }

    @Override
    public MongoPhenomenon create(Phenomenon phenomenon) {
        MongoPhenomenon mongoPhenomenon = (MongoPhenomenon) phenomenon;
        save(mongoPhenomenon);
        return mongoPhenomenon;
    }

    @Override
    protected Phenomenons createPaginatedIterable(Iterable<MongoPhenomenon> i,
                                                  Pagination p, long count) {
        return Phenomenons.from(i).withPagination(p).withElements(count).build();
    }
}
