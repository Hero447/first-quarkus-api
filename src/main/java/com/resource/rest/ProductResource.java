package com.resource.rest;


import com.service.rest.ProductService;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.api.ProductApi;
import org.openapi.quarkus.openapi_yaml.model.ProductCreateRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductUpdateRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductResponse;

import java.util.List;

public class ProductResource implements ProductApi {

    @Inject
    ProductService productService;

    @Override
    @CacheResult(cacheName = "cashedProductList")
    public Uni<List<ProductResponse>> getAllProducts(Integer minPrice, Integer maxPrice) {
        return productService.list(minPrice, maxPrice);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<Response> deleteProductById(Long id) {
        return productService.delete(id);
    }

    @Override
    @CacheResult(cacheName = "cashedProduct")
    public Uni<ProductResponse> getProductById(Long id) {
        return productService.findById(id);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> createProduct(ProductCreateRequest productRequest) {
        return productService.create(productRequest);

    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> updateProduct(ProductUpdateRequest productRequest) {
        return productService.update(productRequest);
    }
}
