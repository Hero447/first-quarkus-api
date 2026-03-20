package com.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class CustomerRequestMessage {
    private MessageType messageType;
    private CustomerDTO customer;
}

