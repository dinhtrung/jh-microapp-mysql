package com.ft.service;

import com.ft.domain.SmppRoutingInfo;
import com.ft.repository.SmppRoutingInfoRepository;
import com.ft.service.dto.SmppRoutingInfoDTO;
import com.ft.service.mapper.SmppRoutingInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing SmppRoutingInfo.
 */
@Service
@Transactional
public class SmppRoutingInfoService {

    private final Logger log = LoggerFactory.getLogger(SmppRoutingInfoService.class);

    private SmppRoutingInfoRepository smppRoutingInfoRepository;

    private SmppRoutingInfoMapper smppRoutingInfoMapper;

    public SmppRoutingInfoService(SmppRoutingInfoRepository smppRoutingInfoRepository, SmppRoutingInfoMapper smppRoutingInfoMapper) {
        this.smppRoutingInfoRepository = smppRoutingInfoRepository;
        this.smppRoutingInfoMapper = smppRoutingInfoMapper;
    }

    /**
     * Save a smppRoutingInfo.
     *
     * @param smppRoutingInfoDTO the entity to save
     * @return the persisted entity
     */
    public SmppRoutingInfoDTO save(SmppRoutingInfoDTO smppRoutingInfoDTO) {
        log.debug("Request to save SmppRoutingInfo : {}", smppRoutingInfoDTO);

        SmppRoutingInfo smppRoutingInfo = smppRoutingInfoMapper.toEntity(smppRoutingInfoDTO);
        smppRoutingInfo = smppRoutingInfoRepository.save(smppRoutingInfo);
        return smppRoutingInfoMapper.toDto(smppRoutingInfo);
    }

    /**
     * Get all the smppRoutingInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SmppRoutingInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SmppRoutingInfos");
        return smppRoutingInfoRepository.findAll(pageable)
            .map(smppRoutingInfoMapper::toDto);
    }


    /**
     * Get one smppRoutingInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmppRoutingInfoDTO> findOne(Long id) {
        log.debug("Request to get SmppRoutingInfo : {}", id);
        return smppRoutingInfoRepository.findById(id)
            .map(smppRoutingInfoMapper::toDto);
    }

    /**
     * Delete the smppRoutingInfo by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmppRoutingInfo : {}", id);
        smppRoutingInfoRepository.deleteById(id);
    }
}
