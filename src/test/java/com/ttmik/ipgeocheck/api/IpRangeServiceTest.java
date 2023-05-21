package com.ttmik.ipgeocheck.api;

import com.ttmik.ipgeocheck.api.entity.IpRange;
import com.ttmik.ipgeocheck.api.repository.IpRangeRepository;
import com.ttmik.ipgeocheck.api.service.IpRangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * IpRangeServie 단위 테스트
 */
class IpRangeServiceTest {

    private IpRangeService ipRangeService;

    @Mock
    private IpRangeRepository ipRangeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ipRangeService = new IpRangeService(ipRangeRepository);
    }

    @Test
    @DisplayName("유효한 IP 테스트 : 국가코드 리턴")
    void getCountryCodeByIp_ValidIp() {
        // given
        String ip = "223.38.90.72";
        String expectedCountryCode = "KR";
        IpRange ipRange = IpRange.builder().countryCode("KR").startIp("223.38.90.0").endIp("223.38.90.255").build();
        when(ipRangeRepository.findAll()).thenReturn(Collections.singletonList(ipRange));

        // when
        Optional<String> actualCountryCode = ipRangeService.getCountryCodeByIp(ip);

        // then
        assertTrue(actualCountryCode.isPresent());
        assertEquals(expectedCountryCode, actualCountryCode.get());
    }

    @Test
    @DisplayName("유효하지 않은 IP 테스트 : 빈값 리턴")
    void getCountryCodeByIp_InvalidIp() {
        // given
        String ip = "10.0.0.1";

        // when
        Optional<String> actualCountryCode = ipRangeService.getCountryCodeByIp(ip);

        // then
        assertEquals(Optional.empty(), actualCountryCode);
    }
}