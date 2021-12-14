package org.envirocar.server.rest.filter;

import com.sun.jersey.core.header.LinkHeader;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.resources.RootResource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ApiDocsLinkFilter implements ContainerResponseFilter {

    private static final String LINK_HEADER = "Link";
    private static final String LINK_REL = "service-desc";
    private final Provider<UriInfo> uriInfo;

    @Inject
    public ApiDocsLinkFilter(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        URI uri = this.uriInfo.get().getBaseUriBuilder().path(RootResource.API_DOCS).build();
        response.getHttpHeaders()
                .add(LINK_HEADER, LinkHeader.uri(uri).type(MediaTypes.YAML_TYPE).rel(LINK_REL).build().toString());
        response.getHttpHeaders()
                .add(LINK_HEADER, LinkHeader.uri(uri).type(MediaTypes.JSON_TYPE).rel(LINK_REL).build().toString());
        return response;
    }
}
