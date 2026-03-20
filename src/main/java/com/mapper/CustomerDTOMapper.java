package com.mapper;

import com.dto.CustomerDTO;
import com.proto.service.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface CustomerDTOMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> customerListToCustomerDTOList(List<Customer> customerList);
}
