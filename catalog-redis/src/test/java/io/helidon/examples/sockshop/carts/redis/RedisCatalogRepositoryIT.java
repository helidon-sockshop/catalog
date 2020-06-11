/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts.redis;

import io.helidon.examples.sockshop.catalog.CatalogRepository;
import io.helidon.examples.sockshop.catalog.CatalogRepositoryTest;

import static io.helidon.examples.sockshop.carts.redis.RedisProducers.socks;
import static io.helidon.examples.sockshop.carts.redis.RedisProducers.client;

/**
 * Tests for Redis repository implementation.
 */
class RedisCatalogRepositoryIT extends CatalogRepositoryTest {
    public CatalogRepository getCatalogRepository() {
        String host = System.getProperty("db.host","localhost");
        int    port = Integer.parseInt(System.getProperty("db.port","6379"));

        return new RedisCatalogRepository(socks(client(host, port))).loadData();
    }
}
