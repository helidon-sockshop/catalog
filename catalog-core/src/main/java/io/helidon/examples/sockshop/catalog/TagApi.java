/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

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
