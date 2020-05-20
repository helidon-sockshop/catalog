# catalog-coherence

This module implements [Coherence](https://coherence.java.net/) [backend](src/main/java/io/helidon/examples/sockshop/catalog/coherence/CoherenceCatalogRepository.java)
for the [Catalog Service](../README.md).

## Building the Service

See [main documentation page](../README.md#building-the-service) for instructions.

## Running the Service

Unlike other back end implementations, which require separate containers for the service
and the back end data store, Coherence is embedded into your application and runs as part
of your application container.

That means that it is as easy to run as the basic [in-memory implementation](../catalog-core/README.md)
of the service, but it allows you to easily scale your service to hundreds of **stateful**,
and optionally **persistent** nodes.

To run Coherence implementation of the service, simply execute

```bash
$ docker run -p 7001:7001 helidon/sockshop/catalog-coherence
``` 

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

Apache License 2.0
