package io.helidon.examples.sockshop.catalog;

import io.helidon.microprofile.server.Server;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.catalog.CatalogResource}.
 */
public class CatalogResourceIT {
    /**
     * This will start the application on ephemeral port to avoid port conflicts.
     * We can discover the actual port by calling {@link io.helidon.microprofile.server.Server#port()} method afterwards.
     */
    private static final Server SERVER = Server.builder().port(0).build().start();

    @BeforeEach
    void setup() {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = SERVER.port();
    }

    @Test
    void testQueryWithoutFilter() {
        when().
                get("/catalogue").
        then().
                statusCode(200).
                body("name", containsInAnyOrder("Nerd leg", "YouTube.sock", "Classic", "Figueroa",
                                                "SuperSport XL", "Cat socks", "Crossed", "Colourful", "Holy"));
    }

    @Test
    void testQueryWithFilter() {
        when().
                get("/catalogue?tags=sport").
        then().
                statusCode(200).
                body("name", contains("SuperSport XL"));
    }

    @Test
    void testQueryPaging() {
        when().
                get("/catalogue?order=name&page=2&size=3").
        then().
                statusCode(200).
                body("name", contains("Crossed", "Figueroa", "Holy"));
    }

    @Test
    void testGetById() {
        when().
                get("/catalogue/{productId}", "03fef6ac-1896-4ce8-bd69-b798f85c6e0b").
        then().
                statusCode(200).
                body("name", is("Holy"));
    }

    @Test
    void testSockCountWithoutFilter() {
        when().
                get("/catalogue/size").
        then().
                statusCode(200).
                body("size", is(9));
    }

    @Test
    void testSockCountWithFilter() {
        when().
                get("/catalogue/size?tags=formal").
        then().
                statusCode(200).
                body("size", is(5));
    }

    @Test
    void testGetTags() {
        when().
                get("/tags").
        then().
                statusCode(200).
                body("tags",
                     containsInAnyOrder("formal", "red", "magic", "green", "blue",
                                        "geek", "black", "skin", "action", "brown", "sport"));
    }
}
