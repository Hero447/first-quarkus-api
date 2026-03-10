package com.mapper;

import com.dto.CustomerRequest;
import com.dto.CustomerResponse;
import com.proto.service.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CustomerMapper {
    public Customer customerRequestToCustomer(CustomerRequest customerRequest) {
        if (customerRequest == null) {
            return null;
        }

        return Customer.newBuilder()
                .setId(customerRequest.getId())
                .setName(customerRequest.getName())
                .setEmail(customerRequest.getEmail())
                .build();
    }

    public CustomerResponse customerToCustomerResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }


    public List<CustomerResponse> customerListToCustomerResponseList(List<Customer> customerList) {
        if (customerList == null) {
            return null;
        }

        List<CustomerResponse> list = new ArrayList<CustomerResponse>( customerList.size() );
        for (Customer customer : customerList) {
            list.add(customerToCustomerResponse(customer));
        }

        return list;
    }
}
