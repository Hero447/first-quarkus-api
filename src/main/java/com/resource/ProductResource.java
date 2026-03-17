package com.resource;


import com.proto.service.*;
import com.service.ProductService;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.api.ProductApi;
import org.openapi.quarkus.openapi_yaml.model.ProductRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductResponse;

import java.util.List;

public class ProductResource implements ProductApi {

    @Inject
    ProductService productService;

    @Override
    @CacheResult(cacheName = "cashedProductList")
    public Uni<List<ProductResponse>> apiV1ProductsGet() {
        return productService.list();
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<Response> apiV1ProductsIdDelete(Long id) {
        return productService.delete(id);
    }

    @Override
    @CacheResult(cacheName = "cashedProduct")
    public Uni<ProductResponse> apiV1ProductsIdGet(Long id) {
        return productService.findById(id);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> apiV1ProductsPost(ProductRequest productRequest) {
        return productService.create(productRequest);

    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> apiV1ProductsPut(ProductRequest productRequest) {
        return productService.update(productRequest);
    }
}
