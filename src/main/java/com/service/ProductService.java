package com.service;

import com.google.protobuf.Int64Value;
import com.mapper.ProductMapper;
import com.proto.service.ProductFilter;
import com.proto.service.ProductList;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.model.ProductRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductResponse;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @GrpcClient("product-service")
    com.proto.service.ProductService productService;
    @Inject
    ProductMapper mapper;

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

    public Uni<Response> delete(Long id) {
        return productService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

    public Uni<ProductResponse> findById(Long id) {
        return productService.findById(Int64Value.of(id)).map(mapper::productToProductResponse);
    }

    public Uni<ProductResponse> update(ProductRequest productRequest) {
        return productService.update(mapper.productRequestToProduct(productRequest))
                .map(mapper::productToProductResponse);
    }

    public Uni<ProductResponse> create(ProductRequest productRequest) {
        return productService.create(mapper.productRequestToProduct(productRequest))
                .map(mapper::productToProductResponse);
    }
}
