package io.helidon.examples.sockshop.catalog;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Sock implements Serializable {
    /**
     * Product identifier.
     */
    @Id
    private String id;

    /**
     * Product name.
     */
    private String name;

    /**
     * Product description.
     */
    private String description;

    /**
     * A list of product image URLs.
     */
    private List<String> imageUrl;

    /**
     * Product price.
     */
    private float price;

    /**
     * Product count.
     */
    private int count;

    /**
     * Product tags.
     */
    private Set<String> tag;
}
