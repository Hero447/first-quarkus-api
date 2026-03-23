package com.service.cache;

import io.quarkus.cache.CacheKeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import org.openapi.quarkus.openapi_yaml.model.CustomerUpdateRequest;

import java.lang.reflect.Method;

@ApplicationScoped
public class CustomerIdKeyGenerator implements CacheKeyGenerator {
    @Override
    public Object generate(Method method, Object... methodParams) {
        CustomerUpdateRequest request = (CustomerUpdateRequest) methodParams[0];
        return request.getId();
    }
}
