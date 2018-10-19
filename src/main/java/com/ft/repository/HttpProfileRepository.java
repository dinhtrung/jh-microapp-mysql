package com.ft.repository;

import com.ft.domain.HttpProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HttpProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HttpProfileRepository extends JpaRepository<HttpProfile, Long> {

}
