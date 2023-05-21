package com.ttmik.ipgeocheck.api.service;

import com.ttmik.ipgeocheck.api.entity.IpRange;
import com.ttmik.ipgeocheck.api.repository.IpRangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IpRangeService {

    private final IpRangeRepository ipRangeRepository;

    /**
    * IP를 확인해서 국가 코드 리턴해주는 메소드
    * - IP를 정수로 변환하여 비교
    *
    * @param ip client IP
    * @retrun Optional 타입의 국가 코드 반환
    * */
    public Optional<String> getCountryCodeByIp(String ip) {
        try {
            byte[] ipAddress = InetAddress.getByName(ip).getAddress();
            return ipRangeRepository.findAll()
                    .stream()
                    .filter(range -> isIpInRange(ipAddress, range.getStartIp(), range.getEndIp()))
                    .findFirst()
                    .map(IpRange::getCountryCode);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * IP가 범위(start, end) 내에 포함되는지 체크 메소드
     *
     * @param ipToCheck client IP
     * @param startIp   start IP
     * @param endIp     end IP
     * @retrun client IP가 범위(start IP, end IP) 내에 포함 여부 반환
     * */
    private boolean isIpInRange(byte[] ipToCheck, String startIp, String endIp) {
        try {
            byte[] startIpBytes = InetAddress.getByName(startIp).getAddress();
            byte[] endIpBytes = InetAddress.getByName(endIp).getAddress();

            for (int i = 0; i < 4; i++) {
                int ipByte = (ipToCheck[i] + 256) % 256;
                int startIpByte = (startIpBytes[i] + 256) % 256;
                int endIpByte = (endIpBytes[i] + 256) % 256;

                if (ipByte < startIpByte || ipByte > endIpByte) {
                    return false;
                }
            }

            return true;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return false;
    }
}
