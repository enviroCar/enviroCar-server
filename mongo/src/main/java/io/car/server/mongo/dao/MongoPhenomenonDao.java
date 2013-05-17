/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.mongo.dao;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.google.inject.Inject;

import io.car.server.core.dao.PhenomenonDao;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.mongo.entity.MongoPhenomenon;
import io.car.server.mongo.entity.MongoSensor;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class MongoPhenomenonDao extends BasicDAO<MongoPhenomenon, ObjectId> implements PhenomenonDao {
    @Inject
    public MongoPhenomenonDao(Datastore datastore) {
        super(MongoPhenomenon.class, datastore);
    }

    @Override
    public Phenomenon getByName(String name) {
        return createQuery().field(MongoSensor.NAME).equal(name).get();
    }

    @Override
    public Phenomenons get() {
        return new Phenomenons(find().fetch());
    }
    
    @Override
    public Phenomenon create(Phenomenon phen) {
        MongoPhenomenon ph = (MongoPhenomenon) phen;
        save(ph);
        return ph;
    }
}
