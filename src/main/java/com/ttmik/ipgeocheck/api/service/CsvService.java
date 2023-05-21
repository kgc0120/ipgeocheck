package com.ttmik.ipgeocheck.api.service;

import com.ttmik.ipgeocheck.api.entity.IpRange;
import com.ttmik.ipgeocheck.api.repository.IpRangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CsvService {

    private final IpRangeRepository ipRangeRepository;

    /**
     * csv 파일을 읽어서 IP_RANGE 테이블에 저장하는 메소드
     *
     * @param csvFilePath csv 파일 경로
     * */
    @Transactional
    public void saveIpRangesFromCsv(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                IpRange ipRange = IpRange.builder().countryCode(data[1]) // 국가 코드
                        .startIp(data[2])                                // 시작 IP
                        .endIp(data[3])                                  // 끝 IP
                        .build();

                ipRangeRepository.save(ipRange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
