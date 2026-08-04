package com.javaweb.ghn.service;

import com.javaweb.ghn.config.GhnProperties;
import com.javaweb.ghn.dto.GhnMasterDataDTOs.District;
import com.javaweb.ghn.dto.GhnMasterDataDTOs.Province;
import com.javaweb.ghn.dto.GhnMasterDataDTOs.Ward;
import com.javaweb.ghn.dto.GhnResponse;
import com.javaweb.ghn.exception.GhnApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GhnAddressService {

    private final RestTemplate restTemplate;
    private final GhnProperties properties;

    public GhnAddressService(RestTemplate restTemplate, GhnProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public List<Province> getProvinces() {
        String url = buildUrl("/shiip/public-api/master-data/province");
        return execute(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    }

    public List<District> getDistricts(int provinceId) {
        String url = UriComponentsBuilder.fromUriString(buildUrl("/shiip/public-api/master-data/district"))
                .queryParam("province_id", provinceId)
                .build()
                .toUriString();
        return execute(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    public List<Ward> getWards(int districtId) {
        String url = UriComponentsBuilder.fromUriString(buildUrl("/shiip/public-api/master-data/ward"))
                .queryParam("district_id", districtId)
                .build()
                .toUriString();
        return execute(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    private <T> T execute(
            String url,
            HttpMethod method,
            Object body,
            ParameterizedTypeReference<GhnResponse<T>> responseType) {

        HttpEntity<?> requestEntity = body == null
                ? HttpEntity.EMPTY
                : new HttpEntity<>(body);

        try {
            ResponseEntity<GhnResponse<T>> response = restTemplate.exchange(
                    url,
                    method,
                    requestEntity,
                    responseType
            );

            GhnResponse<T> ghnResponse = response.getBody();

            if (ghnResponse == null) {
                throw new GhnApiException("GHN API returned empty response.");
            }

            if (ghnResponse.code() == null || ghnResponse.code() != 200) {
                throw new GhnApiException(
                        "GHN API error: " + ghnResponse.message()
                );
            }

            if (ghnResponse.data() == null) {
                return (T) Collections.emptyList();
            }

            return ghnResponse.data();

        } catch (HttpStatusCodeException e) {
            throw new GhnApiException(
                    "HTTP " + e.getStatusCode() + ": " + e.getResponseBodyAsString(),
                    e
            );
        } catch (RestClientException e) {
            throw new GhnApiException(
                    "Cannot connect to GHN API.",
                    e
            );
        }
    }
    private String buildUrl(String path) {
        String baseUrl = StringUtils.hasText(properties.baseUrl())
                ? properties.baseUrl()
                : "https://online-gateway.ghn.vn";
        return baseUrl.replaceAll("/+$", "") + path;
    }
}
