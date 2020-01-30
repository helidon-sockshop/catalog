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
