package com.service.cache;

import io.quarkus.cache.CacheKeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import org.openapi.quarkus.openapi_yaml.model.ProductUpdateRequest;

import java.lang.reflect.Method;

@ApplicationScoped
public class ProductIdKeyGenerator implements CacheKeyGenerator {
    @Override
    public Object generate(Method method, Object... methodParams) {
        ProductUpdateRequest request = (ProductUpdateRequest) methodParams[0];
        return request.getId();
    }
}

