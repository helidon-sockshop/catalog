package io.helidon.examples.sockshop.catalog;

/**
 * Tests for default in memory repository implementation.
 */
public class DefaultCatalogRepositoryTest extends CatalogRepositoryTest {
    @Override
    protected CatalogRepository getCatalogRepository() {
        return new DefaultCatalogRepository().loadData();
    }
}
