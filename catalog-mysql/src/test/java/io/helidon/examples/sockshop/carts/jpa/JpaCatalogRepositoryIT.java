package io.helidon.examples.sockshop.carts.jpa;

import io.helidon.examples.sockshop.catalog.CatalogRepository;
import io.helidon.examples.sockshop.catalog.CatalogRepositoryTest;
import io.helidon.microprofile.server.Server;

/**
 * Integration tests for {@link JpaCatalogRepository}.
 */
public class JpaCatalogRepositoryIT extends CatalogRepositoryTest {

    /**
     * Starting server on ephemeral port in order to be able to get the
     * fully configured repository from the CDI container.
     */
    private static final Server SERVER = Server.builder().port(0).build().start();

    @Override
    protected CatalogRepository getCatalogRepository() {
        return SERVER.cdiContainer().select(CatalogRepository.class).get();
    }
}
