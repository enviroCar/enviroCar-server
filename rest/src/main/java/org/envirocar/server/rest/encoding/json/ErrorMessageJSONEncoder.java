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
package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.rights.AccessRights;
import org.envirocar.server.rest.util.ErrorMessage;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.*;

@Singleton
@Provider
public class ErrorMessageJSONEncoder extends AbstractJSONEntityEncoder<ErrorMessage> {

    public ErrorMessageJSONEncoder() {
        super(ErrorMessage.class);
    }

    @Override
    public ObjectNode encodeJSON(ErrorMessage errorMessage, AccessRights rights, MediaType mediaType) {
        ObjectNode node = getJsonFactory().objectNode();
        node.put(JSONConstants.STATUS_CODE, errorMessage.getStatus().getStatusCode());
        node.put(JSONConstants.REASON_PHRASE, errorMessage.getStatus().getReasonPhrase());
        if (errorMessage.getMessage() != null) {
            node.put(JSONConstants.MESSAGE, errorMessage.getMessage());
        }
        if (errorMessage.getDetails() != null) {
            node.set(JSONConstants.DETAILS, errorMessage.getDetails());
        }
        if (errorMessage.getThrowable() != null) {
            node.set(JSONConstants.STACK_TRACE, encodeStackTrace(errorMessage.getThrowable()));
        }
        return node;
    }

    private ObjectNode encodeStackTrace(Throwable t) {
        Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
        dejaVu.add(t);
        ObjectNode throwableNode = getJsonFactory().objectNode();

        throwableNode.put(JSONConstants.TYPE_KEY, t.getClass().getName());
        if (t.getMessage() != null) {
            throwableNode.put(JSONConstants.MESSAGE, t.getMessage());
        }

        StackTraceElement[] trace = t.getStackTrace();
        if (trace.length > 0) {
            ArrayNode at = throwableNode.putArray(JSONConstants.AT);
            Arrays.stream(trace)
                    .map(this::encodeStackTraceElement)
                    .forEach(at::add);
        }

        Throwable[] suppressed = t.getSuppressed();
        if (suppressed.length > 0) {
            ArrayNode suppressedNode = throwableNode.putArray(JSONConstants.SUPPRESSED);
            Arrays.stream(suppressed)
                    .map(se -> encodeEnclosedStackTrace(se, trace, dejaVu))
                    .forEach(suppressedNode::add);
        }
        Optional.ofNullable(t.getCause())
                .map(cause -> encodeEnclosedStackTrace(cause, trace, dejaVu))
                .ifPresent(cause -> throwableNode.set(JSONConstants.CAUSED_BY, cause));
        return throwableNode;
    }

    /**
     * Print our stack trace as an enclosed exception for the specified
     * stack trace.
     */
    private JsonNode encodeEnclosedStackTrace(Throwable t, StackTraceElement[] enclosingTrace, Set<Throwable> dejaVu) {

        ObjectNode throwableNode = getJsonFactory().objectNode();
        throwableNode.put(JSONConstants.TYPE_KEY, t.getClass().getName());
        if (t.getMessage() != null) {
            throwableNode.put(JSONConstants.MESSAGE, t.getMessage());
        }

        if (dejaVu.contains(t)) {
            ObjectNode circularNode = getJsonFactory().objectNode();
            circularNode.set(JSONConstants.CIRCULAR_REFERENCE, throwableNode);
            return circularNode;
        }
        dejaVu.add(t);

        StackTraceElement[] trace = t.getStackTrace();
        int m = trace.length - 1;
        int n = enclosingTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
            m--;
            n--;
        }
        int common = trace.length - 1 - m;
        ArrayNode at = throwableNode.putArray(JSONConstants.AT);
        Arrays.stream(trace, 0, m + 1).map(this::encodeStackTraceElement).forEach(at::add);
        if (common > 0) {
            at.add(String.format("... %d more", common));
        }

        Throwable[] suppressed = t.getSuppressed();
        if (suppressed.length > 0) {
            ArrayNode suppressedNode = throwableNode.putArray(JSONConstants.SUPPRESSED);
            Arrays.stream(suppressed).map(se -> encodeEnclosedStackTrace(se, trace, dejaVu))
                    .forEach(suppressedNode::add);
        }
        Optional.ofNullable(t.getCause())
                .map(cause -> encodeEnclosedStackTrace(cause, trace, dejaVu))
                .ifPresent(cause -> throwableNode.set(JSONConstants.CAUSED_BY, cause));
        return throwableNode;
    }

    private JsonNode encodeStackTraceElement(StackTraceElement element) {
        return getJsonFactory().textNode(element.toString());
    }


}
