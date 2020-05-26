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

import java.util.Collection;
import java.util.Set;

/**
 * A repository interface that should be implemented by
 * the various data store integrations.
 */
public interface CatalogRepository {
    /**
     * Return socks from the catalog based on the specified criteria.
     *
     * @param tags     a comma-separated list of tags; can be {@code null}
     * @param order    the name of the property to order the results by;
     *                 can be {@code price} or {@code name}
     * @param pageNum  the page of results to return
     * @param pageSize the maximum number of results to return
     *
     * @return a collection of {@code Sock}s based on the specified criteria
     */
    Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize);

    /**
     * Return a {@code Sock} with the specified identifier.
     *
     * @param sockId the sock identifier
     *
     * @return a {@code Sock} with the specified identifier
     */
    Sock getSock(String sockId);

    /**
     * Return the number of socks in the catalog based on the specified criteria.
     *
     * @param tags a comma-separated list of tags; can be {@code null}
     *
     * @return the number of socks in the catalog for the specified criteria
     */
    long getSockCount(String tags);

    /**
     * Return all tags from the catalog.
     *
     * @return all tags from the catalog
     */
    Set<String> getTags();
}
