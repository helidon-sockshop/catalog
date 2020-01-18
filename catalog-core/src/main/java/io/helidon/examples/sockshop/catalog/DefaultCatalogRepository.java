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

/**
 * Default in-memory implementation that can be used for testing.
 */
@ApplicationScoped
public class DefaultCatalogRepository implements CatalogRepository {
    private Map<String, Sock> socks = new ConcurrentHashMap<>();

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

    @PostConstruct
    public void loadData() {
        if (socks.isEmpty()) {
            loadSocksFromJson(Sock.class)
                    .forEach(sock -> socks.put(sock.getId(), sock));
        }
    }

    protected <T> List<T> loadSocksFromJson(Class<T> asClass) {
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

    private static class TagsPredicate implements Predicate<Sock> {
        private Set<String> tags = new HashSet<>();

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
