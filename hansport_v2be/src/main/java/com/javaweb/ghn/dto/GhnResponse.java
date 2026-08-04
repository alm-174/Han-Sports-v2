package com.javaweb.ghn.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GhnResponse<T>(
        @JsonProperty("code") Integer code,
        @JsonProperty("message") String message,
        @JsonProperty("data") T data
) {
}
