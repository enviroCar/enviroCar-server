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
package org.envirocar.server.rest.decoding.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.rest.JSONConstants;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 */
public class ContextKnowledgeTest {
    private SensorDao sensorDao;
    private MeasurementDao measurementsDao;
    private PhenomenonDao phenonmenonDao;
    private EntityFactory ef;
    private DateTimeFormatter dtf;
    private ContextKnowledgeFactory ckf;
    private ContextKnowledge context;
    
    @Before
    public void initMocks() {
        this.ef = Mockito.mock(EntityFactory.class);
        Mockito.when(ef.createTrack()).thenReturn(Mockito.mock(Track.class));
        Mockito.when(ef.createMeasurement()).thenReturn(Mockito.mock(Measurement.class));
        Mockito.when(ef.createMeasurementValue()).thenReturn(Mockito.mock(MeasurementValue.class));
        Mockito.when(ef.createPhenomenon()).thenReturn(Mockito.mock(Phenomenon.class));

        this.dtf = ISODateTimeFormat.dateTimeNoMillis();
        
        this.ckf = Mockito.mock(ContextKnowledgeFactory.class);
        this.context = Mockito.spy(new ContextKnowledge());
        Mockito.when(this.ckf.create()).thenReturn(context);
    }
    
    @Test
    public void testContextKnowledge() throws IOException {
        createDaos();
        JSONEntityDecoder geomDec = Mockito.mock(JSONEntityDecoder.class);
        MeasurementDecoder measDec = new MeasurementDecoder(geomDec, phenonmenonDao, sensorDao);
        measDec.setEntityFactory(ef);
        measDec.setDateTimeFormat(dtf);
        
        TrackDecoder trackDec = new TrackDecoder(measDec, sensorDao, measurementsDao, ckf);
        trackDec.setEntityFactory(ef);
        
        JsonNode node = new ObjectMapper().readTree(getClass().getResourceAsStream("track-context-test.json"));
        trackDec.decode(node, MediaType.APPLICATION_JSON_TYPE);
        
        Mockito.verify(context, Mockito.times(1)).get(JSONConstants.SENSOR_KEY);
        Mockito.verify(context, Mockito.times(1)).get(JSONConstants.PHENOMENONS_KEY);
    }

    private void createDaos() {
        this.sensorDao = Mockito.mock(SensorDao.class);
        this.measurementsDao = Mockito.mock(MeasurementDao.class);
        this.phenonmenonDao = Mockito.mock(PhenomenonDao.class);
        Mockito.when(phenonmenonDao.getByName(Mockito.any(String.class))).thenReturn(Mockito.mock(LocalPhenomenon.class));
    }
    
    public abstract class LocalPhenomenon implements Phenomenon {
        
    }
    
}
