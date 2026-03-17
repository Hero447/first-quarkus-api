package com.resource;

import com.service.CustomerService;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.api.CustomerApi;
import org.openapi.quarkus.openapi_yaml.model.CustomerRequest;
import org.openapi.quarkus.openapi_yaml.model.CustomerResponse;


import java.util.List;

public class CustomerResource implements CustomerApi {
    @Inject
    CustomerService customerService;

    @Override
    @CacheResult(cacheName = "cashedCustomerList")
    public Uni<List<CustomerResponse>> apiV1CustomersGet() {
        return customerService.list();
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<Response> apiV1CustomersIdDelete(Long id) {
        return customerService.delete(id);
    }

    @Override
    @CacheResult(cacheName = "cashedCustomer")
    public Uni<CustomerResponse> apiV1CustomersIdGet(Long id) {
        return customerService.findById(id);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<CustomerResponse> apiV1CustomersPost(CustomerRequest customerRequest) {
        return customerService.create(customerRequest);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<CustomerResponse> apiV1CustomersPut(CustomerRequest customerRequest) {
        return customerService.update(customerRequest);
    }
}
