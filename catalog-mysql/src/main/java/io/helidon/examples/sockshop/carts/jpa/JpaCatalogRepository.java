package io.helidon.examples.sockshop.carts.jpa;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import io.helidon.examples.sockshop.catalog.DefaultCatalogRepository;
import io.helidon.examples.sockshop.catalog.Sock;

import org.eclipse.microprofile.opentracing.Traced;

/**
 * An implementation of {@link io.helidon.examples.sockshop.catalog.CatalogRepository}
 * that that uses relational database (via JPA) as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class JpaCatalogRepository extends DefaultCatalogRepository {

    @PersistenceContext
    private EntityManager em;

    private volatile boolean fLoaded = false;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize) {
        ensureDataLoaded();

        StringBuilder jql = new StringBuilder("select distinct s from Sock as s");

        boolean fTags = tags != null && tags.length() > 0;
        if (fTags) {
            jql.append(" join s.tag t where t in :tags");
        }
        if (order != null) {
            jql.append(" order by s.").append(order);
        }

        TypedQuery query = em.createQuery(jql.toString(), Sock.class);
        if (fTags) {
            query.setParameter("tags", Arrays.asList(tags.split(",")));
        }
        if (pageNum > 0 && pageSize > 0) {
            query.setFirstResult(pageSize * (pageNum - 1))
                 .setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    @Transactional
    public Sock getSock(String sockId) {
        ensureDataLoaded();
        return em.find(Sock.class, sockId);
    }

    @Override
    @Transactional
    public long getSockCount(String tags) {
        ensureDataLoaded();
        StringBuilder jql = new StringBuilder("Select count(distinct s) from Sock as s");

        boolean fTags = tags != null && tags.length() > 0;
        if (fTags) {
            jql.append(" join s.tag t where t in :tags");
        }

        Query query = em.createQuery(jql.toString());
        if (fTags) {
            query.setParameter("tags", Arrays.asList(tags.split(",")));
        }

        return (long) query.getSingleResult();
    }

    @Override
    @Transactional
    public Set<String> getTags() {
        ensureDataLoaded();
        return super.getTags();
    }

    private void ensureDataLoaded() {
        if (!fLoaded && isEmpty()) {
            loadSocksFromJson(Sock.class).forEach(em::persist);
            fLoaded = true;
        }
    }

    private boolean isEmpty() {
        long count = (long) em.createQuery("Select count(s) from Sock as s").getSingleResult();
        return count == 0L;
    }
}
