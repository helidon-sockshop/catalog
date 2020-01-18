package io.helidon.examples.sockshop.catalog;

import java.util.Collection;
import java.util.Set;

/**
 * @author Aleksandar Seovic  2020.01.13
 */
public interface CatalogRepository {
    Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize);
    Sock getSock(String sockId);
    long getSockCount(String tags);

    Set<String> getTags();
}
