package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {
    @JsonProperty("create") CREATE,
    @JsonProperty("read") READ,
    @JsonProperty("update") UPDATE,
    @JsonProperty("delete") DELETE,
    @JsonProperty("readAll") READ_ALL;
}
