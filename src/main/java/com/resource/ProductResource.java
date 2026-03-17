package com.resource;


import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.ProductMapper;
import com.proto.service.*;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.api.ProductApi;
import org.openapi.quarkus.openapi_yaml.model.ProductRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductResponse;

import java.util.List;


public class ProductResource implements ProductApi {

    @GrpcClient("product-service")
    ProductService productService;
    @Inject
    ProductMapper mapper;

    @Override
    @CacheResult(cacheName = "cashedProductList")
    public Uni<List<ProductResponse>> apiV1ProductsGet() {
        return productService.list(Empty.newBuilder().build())
                .onItem().transform(ProductList::getResultListList)
                .map(mapper::productListToProductResponseList);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<Response> apiV1ProductsIdDelete(Long id) {
        return productService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

    @Override
    @CacheResult(cacheName = "cashedProduct")
    public Uni<ProductResponse> apiV1ProductsIdGet(Long id) {
        return productService.findById(Int64Value.of(id)).map(mapper::productToProductResponse);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> apiV1ProductsPost(ProductRequest productRequest) {
        return productService.create(mapper.productRequestToProduct(productRequest))
                .map(mapper::productToProductResponse);

    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> apiV1ProductsPut(ProductRequest productRequest) {
        return productService.update(mapper.productRequestToProduct(productRequest))
                .map(mapper::productToProductResponse);
    }
}
