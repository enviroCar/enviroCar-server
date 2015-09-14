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
package org.envirocar.server.core;

import java.util.List;

import org.envirocar.server.core.exception.ValidationException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

/**
 * class for representing spatial filter
 *  
 * @author staschc
 *
 */
public class SpatialFilter {
	
	/**
	 * operator of the spatial filter 
	 */
	private final SpatialFilterOperator operator;
	
	/** 
	 * generic list for keeping additional filter params
	 */
	private List<Double> params;
	
	/**
	 * geometry used in the spatial filter
	 */
	private Geometry geom;
	
	/**
	 * constructor for creating bbox filter
	 * 
	 * @param poly
	 * 		polygon keeping min and max point of bbox filter
	 */
	public SpatialFilter(Geometry poly){
		if (poly instanceof Polygon){
			geom=poly;
			operator = SpatialFilterOperator.BBOX;
		}
		else {
			throw new ValidationException("Other pure geometry filters than bbox not supported!");
		}
	}
	
	/**
	 * generic constructor for spatial filters
	 * 
	 * @param operatorp
	 * @param g
	 * @param paramsp
	 */
	public SpatialFilter(SpatialFilterOperator operatorp, Geometry g, List<Double> paramsp){
		operator = operatorp;
		geom = g;
		params = paramsp;
	}
	
	/**
	 * enum for keeping the operator that is used in the spatial filter
	 * 
	 * @author staschc
	 *
	 */
	public enum SpatialFilterOperator{
		BBOX,NEARPOINT;
	}
	
	public SpatialFilterOperator getOperator() {
		return operator;
	}

	public List<Double> getParams() {
		return params;
	}

	public Geometry getGeom() {
		return geom;
	}
	
}
