package io.helidon.examples.sockshop.catalog;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Sock implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<String> imageUrl;
    private float price;
    private int count;
    private Set<String> tag;

    public Sock() {
    }

    public Sock(String id, String name, String description, List<String> imageUrl, float price, int count, Set<String> tag) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.count = count;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Sock{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl=" + imageUrl +
                ", price=" + price +
                ", count=" + count +
                ", tag=" + tag +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<String> getTag() {
        return tag;
    }

    public void setTag(Set<String> tag) {
        this.tag = tag;
    }
}
