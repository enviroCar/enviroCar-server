/*
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
package io.car.server.mongo.entity;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.utils.IndexDirection;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import io.car.server.core.entities.TrackBase;

public class MongoTrackBase extends MongoEntityBase implements TrackBase {
    @Indexed(IndexDirection.GEO2D)
    @Embedded(BBOX)
    private Geometry bbox;
    @Property(DESCIPTION)
    private String description;
    @Property(NAME)
    private String name;
    @Inject
    @Transient
    private GeometryFactory factory;

    @Override
    public void setBbox(Geometry bbox) {
        this.bbox = bbox;
    }

    @Override
    public Geometry getBbox() {
        return this.bbox;
    }

    @Override
    public void setBbox(double minx, double miny, double maxx, double maxy) {
        Coordinate[] coords = new Coordinate[] { new Coordinate(minx, miny),
                                                 new Coordinate(maxx, maxy) };
        this.bbox = factory.createPolygon(coords);
    }

    @Override
    public String getIdentifier() {
        return (getId() == null) ? null : getId().toString();
    }

    @Override
    public void setIdentifier(String id) {
        setId(new ObjectId(id));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
