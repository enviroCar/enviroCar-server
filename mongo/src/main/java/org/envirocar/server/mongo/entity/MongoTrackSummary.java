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
package org.envirocar.server.mongo.entity;

import org.locationtech.jts.geom.Geometry;
import org.envirocar.server.core.entities.TrackSummary;
import dev.morphia.annotations.Property;

/**
 * @author maurin
 */
public class MongoTrackSummary implements TrackSummary {

    public static final String IDENTIFIER = "identifier";
    public static final String START_POSITION = "startPosition";
    public static final String END_POSITION = "endPosition";

    @Property(IDENTIFIER)
    private String identifier;
    @Property(START_POSITION)
    private Geometry startPosition;
    @Property(END_POSITION)
    private Geometry endPosition;

    public MongoTrackSummary() {}

    public MongoTrackSummary(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setIdentifier(String id) {
        this.identifier = id;
    }

    @Override
    public boolean hasIdentifier() {
        return (this.identifier != null);
    }

    @Override
    public Geometry getStartPosition() {
        return this.startPosition;
    }

    @Override
    public void setStartPosition(Geometry startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public boolean hasStartPosition() {
        return (this.startPosition != null);
    }

    @Override
    public Geometry getEndPosition() {
        return this.endPosition;
    }

    @Override
    public void setEndPosition(Geometry endPosition) {
        this.endPosition = endPosition;
    }

    @Override
    public boolean hasEndPosition() {
        return (this.endPosition != null);
    }

}