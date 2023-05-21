package com.ttmik.ipgeocheck.api.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class IpRange {

    /**
     * IP 대역에 대한 정보를 가지고 있는 객체
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryCode;
    private String startIp;
    private String endIp;

}
