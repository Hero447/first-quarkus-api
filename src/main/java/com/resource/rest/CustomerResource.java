package com.resource.rest;

import com.service.rest.CustomerService;
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
    public Uni<List<CustomerResponse>> getAllCustomers() {
        return customerService.list();
    }

    @Override
    public Uni<Response> deleteCustomerById(Long id) {
        return customerService.delete(id);
    }

    @Override
    public Uni<CustomerResponse> getCustomerById(Long id) {
        return customerService.findById(id);
    }

    @Override
    public Uni<CustomerResponse> createCustomer(CustomerCreateRequest customerRequest) {
        return customerService.create(customerRequest);
    }

    @Override
    public Uni<CustomerResponse> updateCustomer(CustomerUpdateRequest customerRequest) {
        return customerService.update(customerRequest);
    }
}
