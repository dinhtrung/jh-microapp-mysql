package com.ft.service.mapper;

import com.ft.domain.*;
import com.ft.service.dto.SmppRoutingInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SmppRoutingInfo and its DTO SmppRoutingInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {SmppProfileMapper.class, HttpProfileMapper.class})
public interface SmppRoutingInfoMapper extends EntityMapper<SmppRoutingInfoDTO, SmppRoutingInfo> {

    @Mapping(source = "smppProfiles.id", target = "smppProfilesId")
    @Mapping(source = "smppProfiles.name", target = "smppProfilesName")
    @Mapping(source = "httpProfiles.id", target = "httpProfilesId")
    @Mapping(source = "httpProfiles.name", target = "httpProfilesName")
    SmppRoutingInfoDTO toDto(SmppRoutingInfo smppRoutingInfo);

    @Mapping(source = "smppProfilesId", target = "smppProfiles")
    @Mapping(source = "httpProfilesId", target = "httpProfiles")
    SmppRoutingInfo toEntity(SmppRoutingInfoDTO smppRoutingInfoDTO);

    default SmppRoutingInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmppRoutingInfo smppRoutingInfo = new SmppRoutingInfo();
        smppRoutingInfo.setId(id);
        return smppRoutingInfo;
    }
}
