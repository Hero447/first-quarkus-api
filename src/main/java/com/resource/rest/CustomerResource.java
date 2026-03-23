package com.resource.rest;

import com.service.rest.CustomerService;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.api.CustomerApi;
import org.openapi.quarkus.openapi_yaml.model.CustomerCreateRequest;
import org.openapi.quarkus.openapi_yaml.model.CustomerUpdateRequest;
import org.openapi.quarkus.openapi_yaml.model.CustomerResponse;


import java.util.List;

public class CustomerResource implements CustomerApi {
    @Inject
    CustomerService customerService;

    @Override
    @CacheResult(cacheName = "cashedCustomerList")
    public Uni<List<CustomerResponse>> getAllCustomers() {
        return customerService.list();
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<Response> deleteCustomerById(Long id) {
        return customerService.delete(id);
    }

    @Override
    @CacheResult(cacheName = "cashedCustomer")
    public Uni<CustomerResponse> getCustomerById(Long id) {
        return customerService.findById(id);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<CustomerResponse> createCustomer(CustomerCreateRequest customerRequest) {
        return customerService.create(customerRequest);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<CustomerResponse> updateCustomer(CustomerUpdateRequest customerRequest) {
        return customerService.update(customerRequest);
    }
}
