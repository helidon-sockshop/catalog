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

package io.helidon.examples.sockshop.catalog;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Entity
public class Sock implements Serializable {
    /**
     * Product identifier.
     */
    @Id
    @Schema(description = "Product identifier")
    private String id;

    /**
     * Product name.
     */
    @Schema(description = "Product name")
    private String name;

    /**
     * Product description.
     */
    @Schema(description = "Product description")
    private String description;

    /**
     * A list of product image URLs.
     */
    @ElementCollection
    @Schema(description = "A list of product image URLs")
    private List<String> imageUrl;

    /**
     * Product price.
     */
    @Schema(description = "Product price")
    private float price;

    /**
     * Product count.
     */
    @Schema(description = "Product count")
    private int count;

    /**
     * Product tags.
     */
    @ElementCollection
    @Schema(description = "Product tags")
    private Set<String> tag;
}
