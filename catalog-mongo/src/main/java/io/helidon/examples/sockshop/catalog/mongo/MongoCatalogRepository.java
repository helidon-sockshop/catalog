package io.helidon.examples.sockshop.catalog.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.catalog.DefaultCatalogRepository;
import io.helidon.examples.sockshop.catalog.Sock;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

/**
 * @author Aleksandar Seovic  2020.01.16
 */
@ApplicationScoped
@Specializes
public class MongoCatalogRepository extends DefaultCatalogRepository {
    private static final Logger LOGGER = Logger.getLogger(MongoCatalogRepository.class.getName());

    @Inject
    private MongoCollection<MongoSock> socks;

    @Override
    public Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize) {
        ArrayList<MongoSock> results = new ArrayList<>(pageSize);

        int skipCount = pageSize * (pageNum - 1);
        socks.find(tagsFilter(tags))
                .sort(Sorts.ascending(order))
                .skip(skipCount)
                .limit(pageSize)
                .forEach((Consumer<? super MongoSock>) results::add);

        return results;
    }

    @Override
    public Sock getSock(String sockId) {
        return socks.find(eq("id", sockId)).first();
    }

    @Override
    public long getSockCount(String tags) {
        return socks.countDocuments(tagsFilter(tags));
    }

    @Override
    public Set<String> getTags() {
        Set<String> tags = new HashSet<>();
        socks.distinct("tag", String.class)
                .forEach((Consumer<? super String>) tags::add);
        return tags;
    }

    @Override
    @PostConstruct
    public void loadData() {
        if (this.socks.countDocuments() == 0) {
            this.socks.insertMany(loadSocksFromJson(MongoSock.class));
        }
    }

    private Bson tagsFilter(String tags) {
        if (tags != null && !"".equals(tags)) {
            List<Bson> filters = Arrays.stream(tags.split(","))
                    .map(tag -> eq("tag", tag))
                    .collect(Collectors.toList());
            return or(filters);
        }
        return new BsonDocument();
    }
}
