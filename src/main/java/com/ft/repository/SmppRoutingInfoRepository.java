package com.ft.repository;

import com.ft.domain.SmppRoutingInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmppRoutingInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmppRoutingInfoRepository extends JpaRepository<SmppRoutingInfo, Long> {

}
