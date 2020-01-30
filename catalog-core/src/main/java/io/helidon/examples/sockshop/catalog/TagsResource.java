package io.helidon.examples.sockshop.catalog;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Implementation of the Catalog Service {@code /tags} API.
 */
@ApplicationScoped
@Path("/tags")
public class TagsResource {

    @Inject
    private CatalogRepository catalog;

    @GET
    public Tags getTags() {
        return new Tags(catalog.getTags());
    }

    public static class Tags {
        public Set<String> tags;
        public Object err;

        Tags(Set<String> tags) {
            this.tags = tags;
        }
    }
}
