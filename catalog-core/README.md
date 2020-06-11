# catalog-core

This module contains the bulk of the Product Catalog service implementation, including the 
[domain model](./src/main/java/io/helidon/examples/sockshop/catalog/Sock.java), 
[REST API](./src/main/java/io/helidon/examples/sockshop/catalog/CatalogResource.java), as well as the
[data repository abstraction](./src/main/java/io/helidon/examples/sockshop/catalog/CatalogRepository.java) 
and its [in-memory implementation](./src/main/java/io/helidon/examples/sockshop/catalog/DefaultCatalogRepository.java).

## Building the Service

See [main documentation page](../README.md#building-the-service) for instructions.

## Running the Service

Because this implementation uses in-memory data store, it is trivial to run.

Once you've built the Docker image per instructions above, you can simply run it by executing:

```bash
$ docker run -p 7001:7001 helidon/sockshop/catalog-core
``` 

Once the container is up and running, you should be able to access [service API](../README.md#api) 
by navigating to http://localhost:7001/catalogue/.

As a basic test, you should be able to perform an HTTP GET against `/catalogue/size` endpoint:

```bash
$ curl http://localhost:7001/catalogue/size
``` 
which should return JSON response
```json
{
  "size": 9
}
```

## License

The Universal Permissive License (UPL), Version 1.0
