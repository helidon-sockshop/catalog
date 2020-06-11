/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.catalog;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.opentracing.Traced;

/**
 * Simple in-memory implementation of {@link io.helidon.examples.sockshop.catalog.CatalogRepository}
 * that can be used for demos and testing.
 * <p/>
 * This implementation is obviously not suitable for production use, because it is not
 * persistent and it doesn't scale, but it is trivial to write and very useful for the
 * API testing and quick demos.
 */
@ApplicationScoped
@Traced
public class DefaultCatalogRepository implements CatalogRepository {
    private Map<String, Sock> socks;

    /**
     * Construct {@code DefaultCatalogRepository} with empty storage map.
     */
    public DefaultCatalogRepository() {
        this(new ConcurrentHashMap<>());
    }

    /**
     * Construct {@code DefaultCatalogRepository} with the specified storage map.
     *
     * @param socks the storage map to use
     */
    protected DefaultCatalogRepository(Map<String, Sock> socks) {
        this.socks = socks;
    }

    @Override
    public Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize) {
        Comparator<Sock> comparator = "price".equals(order)
                ? Comparator.comparing(Sock::getPrice)
                : Comparator.comparing(Sock::getName);

        List<Sock> results = socks.values().stream()
                .filter(new TagsPredicate(tags))
                .sorted(comparator)
                .collect(Collectors.toList());

        int from = Math.min((pageNum-1) * pageSize, results.size());
        int to   = Math.min(from + pageSize, results.size());

        return results.subList(from, to);
    }

    @Override
    public Sock getSock(String sockId) {
        return socks.get(sockId);
    }

    @Override
    public long getSockCount(String tags) {
        return socks.values().stream().filter(new TagsPredicate(tags)).count();
    }

    @Override
    public Set<String> getTags() {
        return socks.values().stream()
                .flatMap(sock -> sock.getTag().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Initialize this repository.
     */
    @PostConstruct
    void init() {
        loadData();
    }

    /**
     * Load test data into this repository.
     */
    public CatalogRepository loadData() {
        if (socks.isEmpty()) {
            loadSocksFromJson(Sock.class)
                    .forEach(sock -> socks.put(sock.getId(), sock));
        }
        return this;
    }

    /**
     * Load socks from a JSON file.
     *
     * @param asClass the class to load data as; must be {@link io.helidon.examples.sockshop.catalog.Sock}
     *                or its subclass
     * @param <T>     the type to load data as
     *
     * @return a list of socks
     */
    protected <T extends Sock> List<T> loadSocksFromJson(Class<T> asClass) {
        Jsonb jsonb = JsonbBuilder.create();
        InputStream in = getClass().getClassLoader().getResourceAsStream("data.json");

        return jsonb.fromJson(
          in,
          new ParameterizedType() {
              @Override
              public Type[] getActualTypeArguments() {
                  return new Type[] { asClass };
              }

              @Override
              public Type getRawType() {
                  return ArrayList.class;
              }

              @Override
              public Type getOwnerType() {
                  return null;
              }
          }
        );
    }

    /**
     * Custom {@link java.util.function.Predicate} implementation that allows us
     * to filter socks based on one or more tags.
     */
    private static class TagsPredicate implements Predicate<Sock> {
        private Set<String> tags = new HashSet<>();

        /**
         * Construct {@code TagsPredicate}.
         *
         * @param tags a comma-separated list of tags; can be {@code null}
         */
        TagsPredicate(String tags) {
            if (tags != null && !"".equals(tags)) {
                this.tags.addAll(Arrays.asList(tags.split(",")));
            }
        }

        @Override
        public boolean test(Sock sock) {
            if (tags.isEmpty()) {
                return true;
            }

            for (String tag : tags) {
                if (sock.getTag().contains(tag)) {
                    return true;
                }
            }

            return false;
        }
    }
}
