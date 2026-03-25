package com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    private LocalDateTime lastActivity;
}
