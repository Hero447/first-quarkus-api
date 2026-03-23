package com.resource.rest;


import com.service.rest.ProductService;
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
    public Uni<List<ProductResponse>> getAllProducts(Integer minPrice, Integer maxPrice) {
        return productService.list(minPrice, maxPrice);
    }

    @Override
    public Uni<Response> deleteProductById(Long id) {
        return productService.delete(id);
    }

    @Override
    public Uni<ProductResponse> getProductById(Long id) {
        return productService.findById(id);
    }

    @Override
    public Uni<ProductResponse> createProduct(ProductCreateRequest productRequest) {
        return productService.create(productRequest);

    }

    @Override
    public Uni<ProductResponse> updateProduct(ProductUpdateRequest productRequest) {
        return productService.update(productRequest);
    }
}
