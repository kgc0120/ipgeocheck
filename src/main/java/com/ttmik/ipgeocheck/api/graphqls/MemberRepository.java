package com.ttmik.ipgeocheck.api.graphqls;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

/**
 * @author bumblebee
 */
@GraphQlRepository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
