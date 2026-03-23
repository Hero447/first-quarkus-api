package com.service.websocket;

import com.dto.CustomerDTO;
import com.dto.CustomerRequestMessage;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.CustomerDTOMapper;
import com.proto.service.CustomerList;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CustomerWebSocketService {

    @GrpcClient("customer-service")
    com.proto.service.CustomerService customerService;
    @Inject
    CustomerDTOMapper mapper;

    public Uni<List<CustomerDTO>> list() {
        return customerService.list(Empty.newBuilder().build())
                .onItem().transform(CustomerList::getResultListList)
                .map(mapper::customerListToCustomerDTOList);
    }

    public Uni<List<CustomerDTO>>  delete(Long id) {
        return customerService.delete(Int64Value.of(id)).onItem()
                .transformToUni(boolValue ->
                        boolValue.getValue() ?
                                Uni.createFrom().item(new ArrayList<CustomerDTO>()):
                                Uni.createFrom().failure(new NotFoundException("No Customer with given id")));
    }

    public Uni<List<CustomerDTO>>  findById(Long id) {
        return Uni.join().all(customerService.findById(Int64Value.of(id)).map(mapper::customerToCustomerDTO))
                .andCollectFailures();
    }

    public Uni<List<CustomerDTO>>  update(CustomerRequestMessage message) {
        return Uni.join().all(customerService.update(mapper.customerDTOToCustomer(message.getCustomer()))
                        .map(mapper::customerToCustomerDTO))
                .andCollectFailures();
    }

    public Uni<List<CustomerDTO>>  create(CustomerRequestMessage message) {
        return Uni.join().all(
                customerService.create(mapper.customerDTOToCustomer(message.getCustomer()))
                        .map(mapper::customerToCustomerDTO))
                .andCollectFailures();
    }
}
