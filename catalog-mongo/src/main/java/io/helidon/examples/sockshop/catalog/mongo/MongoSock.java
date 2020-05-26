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

import javax.json.bind.annotation.JsonbTransient;

import io.helidon.examples.sockshop.catalog.Sock;

import org.bson.types.ObjectId;

/**
 * MongoDB treats {@link Sock#getId()} as object ID, which causes errors.
 * <p/>
 * We need to define an {@code ObjectId _id} field explicitly to avoid
 * that.
 */
public class MongoSock extends Sock {
    /**
     * Just to make MongoDB happy.
     */
    @SuppressWarnings("unused")
    @JsonbTransient
    public ObjectId _id;

    /**
     * Construct {@code MongoSock} instance.
     */
    public MongoSock() {
    }
}
