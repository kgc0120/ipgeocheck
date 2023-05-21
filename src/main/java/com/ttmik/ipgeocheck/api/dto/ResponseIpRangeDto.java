package com.ttmik.ipgeocheck.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseIpRangeDto {

    /**
     * IpRange 객체 응답 DTO
     */
    private String countryCode;
    private int status;
    private String ip;

}
