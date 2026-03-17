package com.mapper;

import com.proto.service.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapi.quarkus.openapi_yaml.model.CustomerRequest;
import org.openapi.quarkus.openapi_yaml.model.CustomerResponse;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface CustomerMapper {
    Customer customerRequestToCustomer(CustomerRequest customerRequest);

    CustomerResponse customerToCustomerResponse(Customer customer);

    List<CustomerResponse> customerListToCustomerResponseList(List<Customer> customerList);
}
