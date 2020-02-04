package io.helidon.examples.sockshop.carts.redis;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.catalog.DefaultCatalogRepository;
import io.helidon.examples.sockshop.catalog.Sock;

import org.eclipse.microprofile.opentracing.Traced;
import org.redisson.api.RMap;

/**
 * An implementation of {@link io.helidon.examples.sockshop.catalog.CatalogRepository}
 * that that uses Redis (via Redisson) as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class RedisCatalogRepository extends DefaultCatalogRepository {
    @Inject
    public RedisCatalogRepository(RMap<String, Sock> socks) {
        super(socks);
    }
}
