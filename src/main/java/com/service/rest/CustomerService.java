package com.service.rest;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.CustomerMapper;
import com.proto.service.CustomerList;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.openapi.quarkus.openapi_yaml.model.CustomerCreateRequest;
import org.openapi.quarkus.openapi_yaml.model.CustomerUpdateRequest;
import org.openapi.quarkus.openapi_yaml.model.CustomerResponse;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    @GrpcClient("customer-service")
    com.proto.service.CustomerService customerService;
    @Inject
    CustomerMapper mapper;

    public Uni<List<CustomerResponse>> list() {
        return customerService.list(Empty.newBuilder().build())
                .onItem().transform(CustomerList::getResultListList)
                .map(mapper::customerListToCustomerResponseList);
    }

    public Uni<Response> delete(Long id) {
        return customerService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

    public Uni<CustomerResponse> findById(Long id) {
        return customerService.findById(Int64Value.of(id)).map(mapper::customerToCustomerResponse);
    }

    public Uni<CustomerResponse> update(CustomerUpdateRequest customerRequest) {
        return customerService.update(mapper.customerUpdateRequestToCustomer(customerRequest))
                .map(mapper::customerToCustomerResponse);
    }

    public Uni<CustomerResponse> create(CustomerCreateRequest customerRequest) {
        return customerService.create(mapper.customerCreateRequestToCustomer(customerRequest))
                .map(mapper::customerToCustomerResponse);
    }
}
