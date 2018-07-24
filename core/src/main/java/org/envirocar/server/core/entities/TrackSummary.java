/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.core.entities;

import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author maurin
 */
public class TrackSummary {

    private String identifier;
    private Geometry startPosition;
    private Geometry endPosition;
    
    public String getIdentifier() {
       return this.identifier;
    }

    public void setIdentifier(String id) {
        this.identifier = id;
    }
    
    public boolean hasIdentifier() {
        return (this.identifier != null);
    }

    public Geometry getStartPosition() {
        return this.startPosition;
    }

    public void setStartPosition(Geometry startPosition) {
        this.startPosition = startPosition;
    }

    public boolean hasStartPosition() {
        return (this.startPosition != null);
    }

    public Geometry getEndPosition() {
        return this.endPosition;
    }

    public void setEndPosition(Geometry endPosition) {
        this.endPosition = endPosition;
    }

    public boolean hasEndPosition() {
        return (this.endPosition != null);
    }

}