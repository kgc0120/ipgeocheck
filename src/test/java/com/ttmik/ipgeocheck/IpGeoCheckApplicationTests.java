package com.ttmik.ipgeocheck;

import com.ttmik.ipgeocheck.api.dto.RequestIpRangeDto;
import com.ttmik.ipgeocheck.api.dto.ResponseIpRangeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 통합 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IpGeoCheckApplicationTests {

    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        restTemplate = new TestRestTemplate();
    }

    @Test
    @DisplayName("통합 테스트 : 유효한 IP")
    void getCountryCodeByIp_ValidIp() {
        // given
        String ip = "223.38.90.10";
        String countryCode = "KR";
        String url = "http://localhost:" + port + "/ip-to-country";

        // when
        ResponseEntity<ResponseIpRangeDto> response = restTemplate.postForEntity(url, createRequestIpRangeDto(ip), ResponseIpRangeDto.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryCode, response.getBody().getCountryCode());
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertEquals(ip, response.getBody().getIp());
    }

    @Test
    @DisplayName("통합 테스트 : 유효하지 않은 IP")
    void getCountryCodeByIp_UnknownIp() {
        // given
        String ip = "10.10.10.10";
        String url = "http://localhost:" + port + "/ip-to-country";

        // when
        ResponseEntity<ResponseIpRangeDto> response = restTemplate.postForEntity(url, createRequestIpRangeDto(ip), ResponseIpRangeDto.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("UnKnown", response.getBody().getCountryCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(ip, response.getBody().getIp());
    }

    private RequestIpRangeDto createRequestIpRangeDto(String ip) {
        RequestIpRangeDto requestIpRangeDto = new RequestIpRangeDto();
        requestIpRangeDto.setIp(ip);
        return requestIpRangeDto;
    }

}
