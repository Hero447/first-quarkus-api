package com.mapper;

import com.dto.ProductRequest;
import com.dto.ProductResponse;
import com.proto.service.Product;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductMapper {
    public Product productRequestToProduct(ProductRequest productRequest) {
        if (productRequest == null) {
            return null;
        }

        return Product.newBuilder()
                .setId(productRequest.getId())
                .setName(productRequest.getName())
                .setPrice(productRequest.getPrice())
                .build();
    }

    public ProductResponse productToProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }


    public List<ProductResponse> productListToProductResponseList(List<Product> productList) {
        if (productList == null) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>(productList.size());
        for (Product product : productList) {
            list.add(productToProductResponse(product));
        }

        return list;
    }
}
