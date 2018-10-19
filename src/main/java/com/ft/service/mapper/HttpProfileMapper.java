package com.ft.service.mapper;

import com.ft.domain.*;
import com.ft.service.dto.HttpProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HttpProfile and its DTO HttpProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HttpProfileMapper extends EntityMapper<HttpProfileDTO, HttpProfile> {


    @Mapping(target = "routes", ignore = true)
    HttpProfile toEntity(HttpProfileDTO httpProfileDTO);

    default HttpProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setId(id);
        return httpProfile;
    }
}
