package io.helidon.examples.sockshop.catalog;

import javax.ws.rs.GET;

import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 * REST API for {@code /catalog} service.
 */
public interface TagApi {
    @GET
    @Operation(summary = "Return all tags")
    TagsResource.Tags getTags();
}
