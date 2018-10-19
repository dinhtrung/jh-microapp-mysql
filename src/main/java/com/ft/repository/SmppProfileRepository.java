package com.ft.repository;

import com.ft.domain.SmppProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmppProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmppProfileRepository extends JpaRepository<SmppProfile, Long> {

}
