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

package io.helidon.examples.sockshop.catalog.mongo;

import io.helidon.examples.sockshop.catalog.CatalogRepository;
import io.helidon.examples.sockshop.catalog.CatalogRepositoryTest;

import static io.helidon.examples.sockshop.catalog.mongo.MongoProducers.*;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.catalog.mongo.MongoCatalogRepository}.
 */
public class MongoCatalogRepositoryIT extends CatalogRepositoryTest {
    @Override
    protected CatalogRepository getCatalogRepository() {
        String host = System.getProperty("db.host","localhost");
        int    port = Integer.parseInt(System.getProperty("db.port","27017"));

        return new MongoCatalogRepository(socks(db(client(host, port)))).loadData();
    }
}
