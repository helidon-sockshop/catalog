package io.helidon.examples.sockshop.catalog;

import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Implementation of the Catalog Service {@code /catalogue} API.
 */
@ApplicationScoped
@Path("/catalogue")
public class CatalogResource {
    private static final Logger LOGGER = Logger.getLogger(CatalogResource.class.getName());

    @Inject
    private CatalogRepository catalog;

    @GET
    @Produces(APPLICATION_JSON)
    public Collection<? extends Sock> getSocks(@QueryParam("tags") String tags,
                                     @QueryParam("order") @DefaultValue("price") String order,
                                     @QueryParam("page") @DefaultValue("1") int pageNum,
                                     @QueryParam("size") @DefaultValue("10") int pageSize) {
        LOGGER.info("CatalogResource.getSocks: size=" + pageSize);
        return catalog.getSocks(tags, order, pageNum, pageSize);
    }

    @GET
    @Path("size")
    @Produces(APPLICATION_JSON)
    public Count getSockCount(@QueryParam("tags") String tags) {
        return new Count(catalog.getSockCount(tags));
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response getSock(@PathParam("id") String sockId) {
        Sock sock = catalog.getSock(sockId);
        return sock == null
                ? Response.status(NOT_FOUND).build()
                : Response.ok(sock).build();
    }

    @GET
    @Path("images/{image}")
    @Produces("image/jpeg")
    public Response getImage(@PathParam("image") String image) {
        InputStream img = getClass().getClassLoader().getResourceAsStream("web/images/" + image);
        return img == null
                ? Response.status(NOT_FOUND).build()
                : Response.ok(img).build();
    }

    public static class Count {
        public long size;
        public Object err;

        public Count(long size) {
            this.size = size;
        }
    }
}
