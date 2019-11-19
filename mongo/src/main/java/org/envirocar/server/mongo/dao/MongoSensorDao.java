/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import dev.morphia.query.Query;
import org.bson.types.ObjectId;
import org.envirocar.server.core.CarSimilarityService;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.filter.PropertyFilter;
import org.envirocar.server.core.filter.SensorFilter;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.envirocar.server.mongo.util.MongoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoSensorDao extends AbstractMongoDao<ObjectId, MongoSensor, Sensors>
        implements SensorDao {

    private static final Logger LOG = LoggerFactory.getLogger(MongoSensorDao.class);
    private final CarSimilarityService carSimilarity;

    @Inject
    public MongoSensorDao(MongoDB mongoDB, CarSimilarityService carSimilarity) {
        super(MongoSensor.class, mongoDB);
        this.carSimilarity = carSimilarity;
    }

    @Override
    public Sensor getByIdentifier(String id) {
        try {
            /*
             * this is a backwards-compatibility solution for
             * client that use old (removed/mapped) sensor ids
             * that were duplicates. --> DB related, thats why
             * applied here
             */
            return this.carSimilarity.resolveMappedSensor(id);
        } catch (ResourceNotFoundException ex) {
            LOG.trace("No mapped sensor found for id {}, fetching from db", id);
        }

        ObjectId oid;
        try {
            oid = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return get(oid);
    }

    @Override
    public Sensors get(SensorFilter request) {
        Query<MongoSensor> q = q();
        if (request.hasType()) {
            q.field(MongoSensor.TYPE).equal(request.getType());
        }
        if (request.hasUser()) {
            q.field(MongoSensor.ID).in(getIds(request.getUser()));
        }
        if (request.hasFilters()) {
            applyFilters(q, request.getFilters());
        }
        return fetch(q, request.getPagination());
    }

    public List<ObjectId> getIds(User user) {
        DBObject group = MongoUtils.group(new BasicDBObject("_id", MongoUtils.valueOf(MongoMeasurement.SENSOR,
                                                                                      MongoSensor.ID)));
        DBObject match = MongoUtils.match(MongoMeasurement.USER, ref(user));
        DBCollection collection = getMongoDB().getDatastore().getCollection(MongoMeasurement.class);
        List<DBObject> ops = Arrays.asList(match, group);
        AggregationOptions options = AggregationOptions.builder().build();
        try (Cursor result = collection.aggregate(ops, options)) {
            List<ObjectId> ids = new LinkedList<>();
            result.forEachRemaining(x -> {
                //ids.add(new Key<>(MongoSensor.class, collection.getName(), x.get(ID)));
                ids.add((ObjectId) x.get("_id"));
            });
            return ids;
        }

    }

    @Override
    public Sensor create(Sensor sensor) {
        MongoSensor ms = (MongoSensor) sensor;
        save(ms);
        return ms;
    }

    @Override
    protected Sensors createPaginatedIterable(MongoCursor<MongoSensor> i, Pagination p, long count) {
        return Sensors.from(asCloseableIterator(i)).withElements(count).withPagination(p).build();
    }

    private Query<MongoSensor> applyFilters(Query<MongoSensor> q,
                                            Set<PropertyFilter> filters) {
        if (filters == null || filters.isEmpty()) {
            return q;
        }
        Multimap<String, Object> map = LinkedListMultimap.create();
        for (PropertyFilter f : filters) {
            String field = f.getField();
            String value = f.getValue();
            // "123" != 123 && "true" != true in MongoDB...
            if (field != null && value != null) {
                field = MongoUtils.path(MongoSensor.PROPERTIES, field);
                if (isTrue(value)) {
                    map.put(field, true);
                } else if (isFalse(value)) {
                    map.put(field, false);
                } else if (isNumeric(value)) {
                    map.put(field, Double.valueOf(value));
                } else {
                    map.put(field, value);
                }
            }
        }
        q.disableValidation();
        map.keySet().forEach(field -> q.field(field).in(map.get(field)));
        return q.enableValidation();
    }

    private boolean isTrue(String str) {
        return str.equalsIgnoreCase("true");
    }

    private boolean isFalse(String str) {
        return str.equalsIgnoreCase("false");
    }

    private boolean isNumeric(String str) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = symbols.getMinusSign();

        if (!Character.isDigit(str.charAt(0)) &&
            str.charAt(0) != localeMinusSign) {
            return false;
        }

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = symbols.getDecimalSeparator();

        for (char c : str.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }
}
