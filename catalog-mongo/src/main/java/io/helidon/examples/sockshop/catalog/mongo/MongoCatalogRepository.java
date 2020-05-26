/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.catalog.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.catalog.CatalogRepository;
import io.helidon.examples.sockshop.catalog.DefaultCatalogRepository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.opentracing.Traced;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

/**
 * An implementation of {@link io.helidon.examples.sockshop.catalog.CatalogRepository}
 * that that uses MongoDB as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class MongoCatalogRepository extends DefaultCatalogRepository {

    private MongoCollection<MongoSock> socks;

    @Inject
    MongoCatalogRepository(MongoCollection<MongoSock> socks) {
        this.socks = socks;
    }

    @PostConstruct
    void init() {
        loadData();
        socks.createIndex(Indexes.hashed("id"));
    }

    @Override
    public Collection<? extends MongoSock> getSocks(String tags, String order, int pageNum, int pageSize) {
        ArrayList<MongoSock> results = new ArrayList<>();

        int skipCount = pageSize * (pageNum - 1);
        socks.find(tagsFilter(tags))
                .sort(Sorts.ascending(order))
                .skip(skipCount)
                .limit(pageSize)
                .forEach((Consumer<? super MongoSock>) results::add);

        return results;
    }

    @Override
    public MongoSock getSock(String sockId) {
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
    public CatalogRepository loadData() {
        if (this.socks.countDocuments() == 0) {
            this.socks.insertMany(loadSocksFromJson(MongoSock.class));
        }
        return this;
    }

    /**
     * Helper method to create tags filter.
     *
     * @param tags a comma-separated list of tags; can be {@code null}
     *
     * @return a MongoDB filter for the specified tags
     */
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
