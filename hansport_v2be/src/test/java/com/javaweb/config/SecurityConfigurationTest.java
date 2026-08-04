package com.javaweb.config;

import com.javaweb.ghn.service.GhnAddressService;
import com.javaweb.ghn.service.GhnShippingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "hansport.jwt.base64-secret=HI5cUdfIjjCOvFVro6jMHzivhOlWrpCFYpYasERImNc1zi784B2X1O7+zWyNBJahCgjyMEtvOCCQzxGdefT5OQ=="
})
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GhnAddressService ghnAddressService;

    @MockBean
    private GhnShippingService ghnShippingService;

    @Test
    void ghnProvincesEndpointShouldBeAccessibleWithoutAuthentication() throws Exception {
        when(ghnAddressService.getProvinces()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/ghn/provinces"))
                .andExpect(status().isOk());
    }
}
