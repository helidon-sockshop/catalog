/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.catalog;

import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Implementation of the Catalog Service {@code /catalogue} API.
 */
@ApplicationScoped
@Path("/catalogue")
public class CatalogResource implements CatalogApi{
    private static final Logger LOGGER = Logger.getLogger(CatalogResource.class.getName());

    @Inject
    private CatalogRepository catalog;

    @Override
    public Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize) {
        LOGGER.info("CatalogResource.getSocks: size=" + pageSize);
        return catalog.getSocks(tags, order, pageNum, pageSize);
    }

    @Override
    public Count getSockCount(String tags) {
        return new Count(catalog.getSockCount(tags));
    }

    @Override
    public Response getSock(String sockId) {
        Sock sock = catalog.getSock(sockId);
        return sock == null
                ? Response.status(NOT_FOUND).build()
                : Response.ok(sock).build();
    }

    @Override
    public Response getImage(String image) {
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
