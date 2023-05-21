package com.ttmik.ipgeocheck.api.repository;

import com.ttmik.ipgeocheck.api.entity.IpRange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpRangeRepository extends JpaRepository<IpRange, Long> {
}
