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
package org.envirocar.server.rest.encoding.rdf;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;

import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.encoding.RDFEntityEncoder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Produces({ MediaTypes.XML_RDF, MediaTypes.TURTLE, MediaTypes.TURTLE_ALT })
public abstract class AbstractRDFMessageBodyWriter<T>
        implements MessageBodyWriter<T>, RDFEntityEncoder<T> {
    private final Class<T> classType;
    @Inject
    private Provider<UriInfo> uriInfo;

    public AbstractRDFMessageBodyWriter(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        if (this.classType.isAssignableFrom(type)) {
            return mediaType.isCompatible(MediaTypes.XML_RDF_TYPE) ||
                   mediaType.isCompatible(MediaTypes.TURTLE_TYPE) ||
                   mediaType.isCompatible(MediaTypes.TURTLE_ALT_TYPE);
        } else {
            return false;
        }
    }

    @Override
    public void writeTo(T t, Class<?> c, Type gt, Annotation[] a, MediaType mt,
                        MultivaluedMap<String, Object> h,
                        OutputStream out) throws IOException,
                                                 WebApplicationException {
        Model m = encodeRDF(t);
        m.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        m.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        m.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        if (mt.isCompatible(MediaTypes.XML_RDF_TYPE)) {
            String base = uriInfo.get().getAbsolutePath().toASCIIString();
            m.write(out, "RDF/XML-ABBREV", base);
        } else if (mt.isCompatible(MediaTypes.TURTLE_TYPE) ||
                   mt.isCompatible(MediaTypes.TURTLE_ALT_TYPE)) {
            String base = uriInfo.get().getBaseUri().toASCIIString();
            m.write(out, "TTL", base);
        }
        out.flush();
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public abstract Model encodeRDF(T t);
}
