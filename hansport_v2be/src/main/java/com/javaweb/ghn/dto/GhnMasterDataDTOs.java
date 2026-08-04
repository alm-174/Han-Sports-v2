package com.javaweb.ghn.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GhnMasterDataDTOs {

    public record Province(
            @JsonProperty("ProvinceID") Integer provinceId,
            @JsonProperty("ProvinceName") String provinceName
    ) {
    }

    public record District(
            @JsonProperty("DistrictID") Integer districtId,
            @JsonProperty("DistrictName") String districtName,
            @JsonProperty("ProvinceID") Integer provinceId
    ) {
    }

    public record Ward(
            @JsonProperty("WardCode") String wardCode,
            @JsonProperty("WardName") String wardName,
            @JsonProperty("DistrictID") Integer districtId
    ) {
    }
}
