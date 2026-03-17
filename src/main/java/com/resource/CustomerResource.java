package com.resource;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.CustomerMapper;
import com.proto.service.CustomerList;
import com.proto.service.CustomerService;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.api.CustomerApi;
import org.openapi.quarkus.openapi_yaml.model.CustomerRequest;
import org.openapi.quarkus.openapi_yaml.model.CustomerResponse;


import java.util.List;


public class CustomerResource implements CustomerApi {
    @GrpcClient("customer-service")
    CustomerService customerService;
    @Inject
    CustomerMapper mapper;

    @Override
    @CacheResult(cacheName = "cashedCustomerList")
    public Uni<List<CustomerResponse>> apiV1CustomersGet() {
        return customerService.list(Empty.newBuilder().build())
                .onItem().transform(CustomerList::getResultListList)
                .map(mapper::customerListToCustomerResponseList);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<Response> apiV1CustomersIdDelete(Long id) {
        return customerService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

    @Override
    @CacheResult(cacheName = "cashedCustomer")
    public Uni<CustomerResponse> apiV1CustomersIdGet(Long id) {
        return customerService.findById(Int64Value.of(id)).map(mapper::customerToCustomerResponse);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<CustomerResponse> apiV1CustomersPost(CustomerRequest customerRequest) {
        return customerService.create(mapper.customerRequestToCustomer(customerRequest))
                .map(mapper::customerToCustomerResponse);
    }

    @Override
    @CacheInvalidateAll(cacheName = "cashedCustomer")
    @CacheInvalidateAll(cacheName = "cashedCustomerList")
    public Uni<CustomerResponse> apiV1CustomersPut(CustomerRequest customerRequest) {
        return customerService.update(mapper.customerRequestToCustomer(customerRequest))
                .map(mapper::customerToCustomerResponse);
    }
}
