package com.ttmik.ipgeocheck.api.restcontroller;

import com.ttmik.ipgeocheck.api.dto.RequestIpRangeDto;
import com.ttmik.ipgeocheck.api.dto.ResponseIpRangeDto;
import com.ttmik.ipgeocheck.api.service.IpRangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IpRangeController {

    private final IpRangeService ipRangeService;

    /**
     * IP를 확인해서 국가 코드, HTTP 상태 코드, 요청 IP 리턴해주는 메서드
     *
     * @param requestIpDto client IP { "ip" : "223.38.90.72"}
     * @retrun ResponseEntity
     * {
     *     "countryCode": "KR",
     *     "status": 200,
     *     "ip": "223.38.90.72"
     * }
     * */
    @PostMapping("/ip-to-country")
    public ResponseEntity<ResponseIpRangeDto> getCountryCodeByIp(@RequestBody RequestIpRangeDto requestIpDto) {
        String clientIp = requestIpDto.getIp();
        log.info("clientIp : {}",  clientIp);

        if(clientIp == null || clientIp.isBlank()) {
            ResponseIpRangeDto errorResponse = new ResponseIpRangeDto("UnKnown", HttpStatus.BAD_REQUEST.value(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
        }

        try {
            String countryCode = ipRangeService.getCountryCodeByIp(requestIpDto.getIp())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UnKnown IP"));

            ResponseIpRangeDto response = new ResponseIpRangeDto(countryCode, HttpStatus.OK.value(), clientIp);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            ResponseIpRangeDto errorResponse = new ResponseIpRangeDto("UnKnown", HttpStatus.NOT_FOUND.value(), clientIp);
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseIpRangeDto errorResponse = new ResponseIpRangeDto("ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(), clientIp);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }
}
