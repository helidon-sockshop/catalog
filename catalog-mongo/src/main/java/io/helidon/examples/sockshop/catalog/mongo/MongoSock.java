package io.helidon.examples.sockshop.catalog.mongo;

import java.util.List;
import java.util.Set;

import io.helidon.examples.sockshop.catalog.Sock;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 * @author Aleksandar Seovic  2020.01.16
 */
public class MongoSock extends Sock {
    @BsonId
    public ObjectId _id;

    public MongoSock() {
    }

    public MongoSock(String id,
                     String name,
                     String description,
                     List<String> imageUrl,
                     float price,
                     int count,
                     Set<String> tag) {
        super(id, name, description, imageUrl, price, count, tag);
    }
}
