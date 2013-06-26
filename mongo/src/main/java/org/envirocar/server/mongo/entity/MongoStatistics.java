/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.mongo.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Phenomenons;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("statistics")
public class MongoStatistics {
    public static final String KEY = Mapper.ID_KEY;
    public static final String CREATED = "created";
    public static final String STATISTICS = "statistics";
    private static final int EXPIRE_AFTER_SECONDS = 60 * 60 * 3; // 3 hours
    @Id
    @Embedded
    private MongoStatisticKey key;
    @Indexed(expireAfterSeconds = EXPIRE_AFTER_SECONDS)
    @Property(CREATED)
    private DateTime created = new DateTime();
    @Embedded(STATISTICS)
    private List<MongoStatistic> statistics = Collections.emptyList();

    public MongoStatistics() {
    }

    public MongoStatistics(MongoStatisticKey key,
                           List<MongoStatistic> statistics) {
        this.key = key;
        this.statistics = statistics;
    }

    public List<MongoStatistic> getStatistics() {
        return Collections.unmodifiableList(this.statistics);
    }

    public MongoStatistic getStatistic(Phenomenon phenomenon) {
        for (MongoStatistic statistic : this.statistics) {
            if (statistic.getPhenomenon().equals(phenomenon)) {
                return statistic;
            }
        }
        return null;
    }

    public List<MongoStatistic> getStatistics(Phenomenons phenomenons) {
        HashSet<Phenomenon> phens = Sets.newHashSet(phenomenons);
        List<MongoStatistic> list = Lists.newLinkedList();
        for (MongoStatistic statistic : statistics) {
            if (phens.contains(statistic.getPhenomenon())) {
                list.add(statistic);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public void setStatistics(List<MongoStatistic> statistics) {
        this.statistics = statistics;
    }

    public MongoStatisticKey getKey() {
        return this.key;
    }

    public void setKey(MongoStatisticKey key) {
        this.key = key;
    }

    public DateTime getCreated() {
        return created;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoStatistics other = (MongoStatistics) obj;
        return Objects.equal(this.key, other.key);
    }
}
