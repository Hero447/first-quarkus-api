package com.mapper;

import com.dto.ProductRequest;
import com.dto.ProductResponse;
import com.proto.service.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface ProductMapper {
    Product productRequestToProduct(ProductRequest productRequest);

    ProductResponse productToProductResponse(Product product);

    List<ProductResponse> productListToProductResponseList(List<Product> productList);
}
