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
package org.envirocar.server.mongo.util;

import com.mongodb.QueryOperators;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface Ops {
    String IN = QueryOperators.IN;
    String MATCH = "$match";
    String UNWIND = "$unwind";
    String AVG = "$avg";
    String MIN = "$min";
    String MAX = "$max";
    String SUM = "$sum";
    String GROUP = "$group";
    String PROJECT = "$project";
    String ADD_TO_SET = "$addToSet";
    String GEO_WITHIN = "$geoWithin";
    String NEAR_SPHERE = QueryOperators.NEAR_SPHERE;
    String GEOMETRY = "$geometry";
    String GREATER_THAN = QueryOperators.GT;
    String LESS_THAN = QueryOperators.LT;
    String EQUALS = "$eq";
}
