package com.ttmik.ipgeocheck.api;


import com.ttmik.ipgeocheck.api.restcontroller.IpRangeController;
import com.ttmik.ipgeocheck.api.service.IpRangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * IpRangeController 단위 테스트
 */
class IpRangeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IpRangeService ipRangeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        IpRangeController ipRangeController = new IpRangeController(ipRangeService);
        mockMvc = MockMvcBuilders.standaloneSetup(ipRangeController).build();
    }

    @Test
    @DisplayName("유효한 IP 테스트 : 국가코드, 상태코드(200), 클라이언트 IP 리턴")
    void getCountryCodeByIp_ValidIp_OK() throws Exception {
        // given
        String clientIp = "223.38.90.72";
        String countryCode = "KR";
        when(ipRangeService.getCountryCodeByIp(clientIp)).thenReturn(Optional.of(countryCode));

        // when & then
        MvcResult result = mockMvc.perform(post("/ip-to-country")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"ip\": \"" + clientIp + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value(countryCode))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.ip").value(clientIp))
                .andReturn();

        System.out.println(new String(result.getResponse().getContentAsString().getBytes("ISO-8859-1"), "UTF-8"));
    }

    @Test
    @DisplayName("유효하지 않는 IP 테스트 : 국가코드(UnKnown), 상태코드(404), 클라이언트 IP 리턴")
    void getCountryCodeByIp_InValidIp_NOT_FOUND() throws Exception {
        // given
        String clientIp = "10.0.0.1";

        // when & then
        MvcResult result = mockMvc.perform(post("/ip-to-country")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"ip\": \"" + clientIp + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.ip").value(clientIp))
                .andReturn();

        System.out.println(new String(result.getResponse().getContentAsString().getBytes("ISO-8859-1"), "UTF-8"));
    }

    @Test
    @DisplayName("잘못된 요청 테스트 : 국가코드(UnKnown), 상태코드(400) 리턴")
    void getCountryCodeByIp_BAD_REQUEST() throws Exception {
        // given
        String clientIp = "223.38.90.72";

        // when & then
        MvcResult result = mockMvc.perform(post("/ip-to-country")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"BAD\": \"" + clientIp + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.ip").value(""))
                .andReturn();

        System.out.println(new String(result.getResponse().getContentAsString().getBytes("ISO-8859-1"), "UTF-8"));
    }

    @Test
    @DisplayName("서버 에러 테스트 : 국가코드(ERROR), 상태코드(500), 클라이언트 IP 리턴")
    void getCountryCodeByIp_INTERNAL_SERVER_ERROR() throws Exception {
        // given
        String clientIp = "223.38.90.72";
        when(ipRangeService.getCountryCodeByIp(clientIp)).thenThrow(new RuntimeException("server error"));

        // when & then
        MvcResult result = mockMvc.perform(post("/ip-to-country")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"ip\": \"" + clientIp + "\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andReturn();

        System.out.println(new String(result.getResponse().getContentAsString().getBytes("ISO-8859-1"), "UTF-8"));
    }

}