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
package org.envirocar.server.rest.encoding.shapefile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.MeasurementValues;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.rights.AccessRights;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.n52.wps.io.IOUtils;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Point;

/**
 * TODO: Javadoc
 * 
 * @author Benjamin Pross
 */
@Provider
public class TrackShapefileEncoder extends AbstractShapefileTrackEncoder<Track> {

	private static final Logger log = LoggerFactory
            .getLogger(TrackShapefileEncoder.class);
	
	private SimpleFeatureTypeBuilder typeBuilder;
    private final DataService dataService;
    private CoordinateReferenceSystem crs_wgs84;
    
    @Inject
	public TrackShapefileEncoder(DataService dataService){
    	super(Track.class);
        this.dataService = dataService;
	}
	
	@Override
	public File encodeShapefile(Track t, AccessRights rights,
			MediaType mediaType) {
		
		File zippedShapeFile = null;
		try {
			if (rights.canSeeMeasurementsOf(t)) {
				Measurements measurements = dataService
						.getMeasurements(new MeasurementFilter(t));					
				zippedShapeFile = createZippedShapefile(createShapeFile(createFeatureCollection(measurements)));
			}
			
		} catch (IOException e) {
			log.debug(e.getMessage());
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		
		return zippedShapeFile;
	}

	private FeatureCollection<SimpleFeatureType, SimpleFeature> createFeatureCollection(Measurements measurements){

		List<SimpleFeature> simpleFeatureList = new ArrayList<SimpleFeature>();

		String uuid = UUID.randomUUID().toString().substring(0, 5);

		String namespace = "http://enviroCar.org/" + uuid;

		String idAttributeName = "id";
		String geometryAttributeName = "geometry";
		String timeAttributeName = "time";
		
		SimpleFeatureType sft = null;

		SimpleFeatureBuilder sfb = null;

		typeBuilder = new SimpleFeatureTypeBuilder();
		
		typeBuilder.setCRS(getCRS_WGS84());

		typeBuilder.setNamespaceURI(namespace);
		Name nameType = new NameImpl(namespace, "Feature-" + uuid);
		typeBuilder.setName(nameType);

		typeBuilder.add(geometryAttributeName, Point.class);
		typeBuilder.add(idAttributeName, String.class);
		typeBuilder.add(timeAttributeName, String.class);		
		
		if (sft == null) {
			sft = buildFeatureType(measurements);
			sfb = new SimpleFeatureBuilder(sft);
		}	
		
        for (Measurement measurement : measurements) {
        	
        	MeasurementValues values = measurement.getValues();

			String id = measurement.getIdentifier();
			
			sfb.set(idAttributeName, id);
			sfb.set(timeAttributeName, measurement.getCreationTime().toString());
			sfb.set(geometryAttributeName, measurement.getGeometry());
			
        	for (MeasurementValue measurementValue : values) {
				
        		Phenomenon phenomenon = measurementValue.getPhenomenon();
        		
				String value = measurementValue.getValue().toString();
				String unit = phenomenon.getUnit();

				/*
				 * create property name
				 */
				String propertyName = getPropertyName(phenomenon.getName(), unit);
				
				if (sfb != null) {
					sfb.set(propertyName, value);
				}
			}
			if (sfb != null) {
				simpleFeatureList.add(sfb.buildFeature(id));
			}
        }
        return  new ListFeatureCollection(sft, simpleFeatureList);		
	}
	
	private SimpleFeatureType buildFeatureType(Measurements measurements) {

		Set<String> distinctPhenomenonNames = new HashSet<String>();
		
		for (Measurement measurement : measurements) {

			MeasurementValues values = measurement.getValues();

			for (MeasurementValue measurementValue : values) {

				Phenomenon phenomenon = measurementValue.getPhenomenon();

				String unit = phenomenon.getUnit();

				/*
				 * create property name
				 */
				String propertyName = getPropertyName(phenomenon.getName(), unit);
				
				distinctPhenomenonNames.add(propertyName);
			}

		}

		Iterator<String> distinctPhenomenonNamesIterator = distinctPhenomenonNames
				.iterator();

		while (distinctPhenomenonNamesIterator.hasNext()) {
			String phenomenonNameAndUnit = (String) distinctPhenomenonNamesIterator
					.next();
			typeBuilder.add(phenomenonNameAndUnit,
					String.class);
		}

		return typeBuilder.buildFeatureType();
	}

	private File createShapeFile(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) throws Exception{
		
		String shapeFileSuffix = ".shp";
		
		File tempBaseFile = File.createTempFile("resolveDir", ".tmp");
		tempBaseFile.deleteOnExit();
		File parent = tempBaseFile.getParentFile();
		
		File shpBaseDirectory = new File(parent, UUID.randomUUID().toString());
		
		if (!shpBaseDirectory.mkdir()) {
			throw new IllegalStateException("Could not create temporary shp directory.");
		}
		
		File tempSHPfile = File.createTempFile("shp", shapeFileSuffix, shpBaseDirectory);
		tempSHPfile.deleteOnExit();
		DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", tempSHPfile.toURI().toURL());
		params.put("create spatial index", Boolean.TRUE);

		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory
				.createNewDataStore(params);

		newDataStore.createSchema((SimpleFeatureType) collection.getSchema());
		if(collection.getSchema().getCoordinateReferenceSystem()==null){
			newDataStore.forceSchemaCRS(getCRS_WGS84());
		}else{
			newDataStore.forceSchemaCRS(collection.getSchema()
				.getCoordinateReferenceSystem());
		}

		Transaction transaction = new DefaultTransaction("create");

		String typeName = newDataStore.getTypeNames()[0];
		@SuppressWarnings("unchecked")
		FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) newDataStore
				.getFeatureSource(typeName);
		featureStore.setTransaction(transaction);
		try {
			featureStore.addFeatures(collection);
			transaction.commit();
		} catch (Exception problem) {
			transaction.rollback();
		} finally {
			transaction.close();
		}

		// Get names of additional files
		String path = tempSHPfile.getAbsolutePath();
		String baseName = path.substring(0, path.length() - shapeFileSuffix.length());
		File shx = new File(baseName + ".shx");
		File dbf = new File(baseName + ".dbf");
		File prj = new File(baseName + ".prj");
		
		// mark created files for delete
		tempSHPfile.deleteOnExit();
		shx.deleteOnExit();
		dbf.deleteOnExit();
		prj.deleteOnExit();
		shpBaseDirectory.deleteOnExit();
		
		return shpBaseDirectory;
	}
	
	private File createZippedShapefile(File shapeDirectory) throws IOException {
		if (shapeDirectory != null && shapeDirectory.isDirectory()) {
			File[] files = shapeDirectory.listFiles();
			return IOUtils.zip(files);
		}

		return null;
	}
	
	private String getPropertyName(String propertyName, String unit){
		
		return propertyName + "(" + unit + ")";		
	}
	
	private CoordinateReferenceSystem getCRS_WGS84() {

		if (crs_wgs84 == null) {

			try {
				crs_wgs84 = CRS.decode("EPSG:4326");
			} catch (NoSuchAuthorityCodeException e) {
				log.debug(e.getMessage());
			} catch (FactoryException e) {
				log.debug(e.getMessage());
			}
		}
		return crs_wgs84;
	}
	
}
