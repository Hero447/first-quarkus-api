package com.mapper;

import com.dto.CustomerRequest;
import com.dto.CustomerResponse;
import com.proto.service.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface CustomerMapper {
    Customer customerRequestToCustomer(CustomerRequest customerRequest);

    CustomerResponse customerToCustomerResponse(Customer customer);

    List<CustomerResponse> customerListToCustomerResponseList(List<Customer> customerList);
}
