package com.mapper;


import com.proto.service.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapi.quarkus.openapi_yaml.model.ProductRequest;
import org.openapi.quarkus.openapi_yaml.model.ProductResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface ProductMapper {
    Product productRequestToProduct(ProductRequest productRequest);

    ProductResponse productToProductResponse(Product product);

    List<ProductResponse> productListToProductResponseList(List<Product> productList);
}
