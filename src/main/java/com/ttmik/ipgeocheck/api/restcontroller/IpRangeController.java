package com.ttmik.ipgeocheck.api.restcontroller;

import com.ttmik.ipgeocheck.api.dto.RequestIpRangeDto;
import com.ttmik.ipgeocheck.api.dto.ResponseIpRangeDto;
import com.ttmik.ipgeocheck.api.entity.IpRange;
import com.ttmik.ipgeocheck.api.service.IpRangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Api(tags = "IP를 확인해서 국가 코드 제공하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class IpRangeController {

    private final IpRangeService ipRangeService;

    @QueryMapping
    public List<IpRange> getIps(){
        return ipRangeService.findAll();
    }

    @QueryMapping
    public ResponseIpRangeDto getIpInfo(@Argument RequestIpRangeDto requestIpRangeDto){
        String clientIp = requestIpRangeDto.getIp();
        log.info("clientIp : {}",  clientIp);

        if(clientIp == null || clientIp.isBlank()) {
            ResponseIpRangeDto errorResponse = new ResponseIpRangeDto("UnKnown", HttpStatus.BAD_REQUEST.value(), "");
            return errorResponse;
        }

        String countryCode = ipRangeService.getCountryCodeByIp(requestIpRangeDto.getIp())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UnKnown IP"));

        ResponseIpRangeDto response = new ResponseIpRangeDto(countryCode, HttpStatus.OK.value(), clientIp);
        return response;
    }

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
    @ApiOperation(value = "Country Code by IP")
    @PostMapping("/ip-to-country")
    public ResponseEntity<ResponseIpRangeDto> getCountryCodeByIp(@RequestBody RequestIpRangeDto requestIpDto) {
        String clientIp = requestIpDto.getIp();
        log.info("clientIp : {}",  clientIp);

        if(clientIp == null || clientIp.isBlank()) {
            ResponseIpRangeDto errorResponse = new ResponseIpRangeDto("UnKnown", HttpStatus.BAD_REQUEST.value(), "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
        }

//        try {
            String countryCode = ipRangeService.getCountryCodeByIp(requestIpDto.getIp())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UnKnown IP"));

            ResponseIpRangeDto response = new ResponseIpRangeDto(countryCode, HttpStatus.OK.value(), clientIp);
            return ResponseEntity.ok(response);
//        } catch (ResponseStatusException e) {
//            ResponseIpRangeDto errorResponse = new ResponseIpRangeDto("UnKnown", HttpStatus.NOT_FOUND.value(), clientIp);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ResponseIpRangeDto errorResponse = new ResponseIpRangeDto("ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(), clientIp);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
//        }
    }

//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<String> handleNoSuchElementFoundException(ResponseStatusException exception) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
//    }
}
