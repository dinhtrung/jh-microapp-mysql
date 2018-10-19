package com.ft.service.mapper;

import com.ft.domain.*;
import com.ft.service.dto.SmppProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SmppProfile and its DTO SmppProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmppProfileMapper extends EntityMapper<SmppProfileDTO, SmppProfile> {


    @Mapping(target = "routes", ignore = true)
    SmppProfile toEntity(SmppProfileDTO smppProfileDTO);

    default SmppProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmppProfile smppProfile = new SmppProfile();
        smppProfile.setId(id);
        return smppProfile;
    }
}
