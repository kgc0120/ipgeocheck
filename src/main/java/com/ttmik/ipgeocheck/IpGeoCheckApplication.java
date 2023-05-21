package com.ttmik.ipgeocheck;

import com.ttmik.ipgeocheck.api.service.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringBootApplication
public class IpGeoCheckApplication {

    @Value("${csv.path}")
    private String csvPath;

    private final CsvService csvService;

    // 서버 yml 경로
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/ipgeocheck/application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(IpGeoCheckApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

    @PostConstruct
    public void init() {
        csvService.saveIpRangesFromCsv(csvPath);
    }

}
