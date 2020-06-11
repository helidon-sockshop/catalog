/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog.coherence;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import javax.inject.Inject;

import io.helidon.examples.sockshop.catalog.Sock;
import io.helidon.examples.sockshop.catalog.DefaultCatalogRepository;

import com.oracle.coherence.cdi.Name;
import com.tangosol.util.Aggregators;
import com.tangosol.util.Filter;
import com.tangosol.util.Filters;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.UniversalExtractor;
import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.LimitFilter;
import org.eclipse.microprofile.opentracing.Traced;

import com.tangosol.net.NamedMap;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * An implementation of {@link io.helidon.examples.sockshop.catalog.CatalogRepository}
 * that that uses Coherence as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class CoherenceCatalogRepository extends DefaultCatalogRepository {
    private NamedMap<String, Sock> socks;
    private static Comparator<Sock> PRICE_COMPARATOR = new ExtractorComparator<>(new UniversalExtractor<Sock, Float>("price"));
    private static Comparator<Sock> NAME_COMPARATOR  = new ExtractorComparator<>(new UniversalExtractor<Sock, String>("name"));

    @Inject
    public CoherenceCatalogRepository(@Name("socks") NamedMap<String, Sock> socks) {
        super(socks);
        this.socks = socks;
    }

    @Override
    public Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize) {
        Comparator<Sock> comparator = "price".equals(order)
                ? PRICE_COMPARATOR
                : "name".equals(order)
                        ? NAME_COMPARATOR
                        : null;

        LimitFilter<Sock> filter = new LimitFilter<>(createTagsFilter(tags), pageSize);
        filter.setPage(pageNum -1);
        return socks.values(filter, comparator);
    }

    @Override
    public long getSockCount(String tags) {
        return socks.aggregate(createTagsFilter(tags), Aggregators.count());
    }

    @Override
    public Set<String> getTags() {
        Collection<Sock> result = socks.values();
        return Arrays.stream(result.toArray())
                .flatMap(sock -> ((Sock) sock).getTag().stream())
                .collect(Collectors.toSet());
    }

    private Filter<Sock> createTagsFilter(String tags) {
        Filter<Sock> filter = AlwaysFilter.INSTANCE();
        if (tags != null && !"".equals(tags)) {
            String[] aTags = tags.split(",");
            filter = Filters.containsAny(Sock::getTag, aTags);
        }
        return filter;
    }
}