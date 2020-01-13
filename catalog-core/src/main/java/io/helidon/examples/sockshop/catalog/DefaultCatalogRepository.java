package io.helidon.examples.sockshop.catalog;

import java.io.InputStream;
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
import javax.enterprise.inject.Alternative;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

/**
 * Default in-memory implementation that can be used for testing.
 */
@ApplicationScoped
@Alternative
public class DefaultCatalogRepository implements CatalogRepository {
    private Map<String, Sock> socks = new ConcurrentHashMap<>();

    @Override
    public Collection<Sock> getSocks(String tags, String order, int pageNum, int pageSize) {
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
    public int getSockCount(String tags) {
        return (int) socks.values().stream().filter(new TagsPredicate(tags)).count();
    }

    @Override
    public Set<String> getTags() {
        return socks.values().stream()
                .flatMap(sock -> sock.getTag().stream())
                .collect(Collectors.toSet());
    }

    @PostConstruct
    public void loadData() {
        if (this.socks.isEmpty()) {
            Jsonb jsonb = JsonbBuilder.create();
            InputStream in = getClass().getClassLoader().getResourceAsStream("data.json");

            List<Sock> socks = jsonb.fromJson(
              in,
              new ArrayList<Sock>(){}.getClass().getGenericSuperclass()
            );

            Map<String, Sock> sockMap = socks.stream()
                    .collect(Collectors.toMap(Sock::getId, sock -> sock));
            this.socks.putAll(sockMap);
        }
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
