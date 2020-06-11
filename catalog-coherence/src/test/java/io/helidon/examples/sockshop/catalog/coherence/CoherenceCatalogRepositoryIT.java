/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog.coherence;

import io.helidon.examples.sockshop.catalog.CatalogRepository;
import io.helidon.examples.sockshop.catalog.CatalogRepositoryTest;

import com.tangosol.net.CacheFactory;

/**
 * Tests for Coherence repository implementation.
 */
class CoherenceCatalogRepositoryIT extends CatalogRepositoryTest {
    public CatalogRepository getCatalogRepository() {
        return new CoherenceCatalogRepository(CacheFactory.getCache("socks")).loadData();
    }
}