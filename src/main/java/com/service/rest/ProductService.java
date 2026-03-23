package com.service.rest;

import com.google.protobuf.Int64Value;
import com.mapper.ProductMapper;
import com.proto.service.ProductFilter;
import com.proto.service.ProductList;
import com.service.cache.ProductIdKeyGenerator;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.model.ProductCreateRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductUpdateRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductResponse;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @GrpcClient("product-service")
    com.proto.service.ProductService productService;
    @Inject
    ProductMapper mapper;

    @CacheResult(cacheName = "cashedProductList")
    public Uni<List<ProductResponse>> list(Integer minPrice, Integer maxPrice) {
        ProductFilter.Builder filterBuilder = ProductFilter.newBuilder();
        if (minPrice != null) {
            filterBuilder.setMinPrice(minPrice);
        }

        if (maxPrice != null) {
            filterBuilder.setMaxPrice(maxPrice);
        }

        return productService.list(filterBuilder.build())
                .onItem().transform(ProductList::getResultListList)
                .map(mapper::productListToProductResponseList);
    }

    @CacheInvalidate(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<Response> delete(Long id) {
        return productService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

    @CacheResult(cacheName = "cashedProduct")
    public Uni<ProductResponse> findById(Long id) {
        return productService.findById(Int64Value.of(id)).map(mapper::productToProductResponse);
    }

    @CacheInvalidate(cacheName = "cashedProduct", keyGenerator = ProductIdKeyGenerator.class)
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> update(ProductUpdateRequest productRequest) {
        return productService.update(mapper.productUpdateRequestToProduct(productRequest))
                .map(mapper::productToProductResponse);
    }

    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> create(ProductCreateRequest productRequest) {
        return productService.create(mapper.productCreateRequestToProduct(productRequest))
                .map(mapper::productToProductResponse);
    }
}
