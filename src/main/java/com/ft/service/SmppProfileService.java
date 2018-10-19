package com.ft.service;

import com.ft.domain.SmppProfile;
import com.ft.repository.SmppProfileRepository;
import com.ft.service.dto.SmppProfileDTO;
import com.ft.service.mapper.SmppProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing SmppProfile.
 */
@Service
@Transactional
public class SmppProfileService {

    private final Logger log = LoggerFactory.getLogger(SmppProfileService.class);

    private SmppProfileRepository smppProfileRepository;

    private SmppProfileMapper smppProfileMapper;

    public SmppProfileService(SmppProfileRepository smppProfileRepository, SmppProfileMapper smppProfileMapper) {
        this.smppProfileRepository = smppProfileRepository;
        this.smppProfileMapper = smppProfileMapper;
    }

    /**
     * Save a smppProfile.
     *
     * @param smppProfileDTO the entity to save
     * @return the persisted entity
     */
    public SmppProfileDTO save(SmppProfileDTO smppProfileDTO) {
        log.debug("Request to save SmppProfile : {}", smppProfileDTO);

        SmppProfile smppProfile = smppProfileMapper.toEntity(smppProfileDTO);
        smppProfile = smppProfileRepository.save(smppProfile);
        return smppProfileMapper.toDto(smppProfile);
    }

    /**
     * Get all the smppProfiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SmppProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SmppProfiles");
        return smppProfileRepository.findAll(pageable)
            .map(smppProfileMapper::toDto);
    }


    /**
     * Get one smppProfile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmppProfileDTO> findOne(Long id) {
        log.debug("Request to get SmppProfile : {}", id);
        return smppProfileRepository.findById(id)
            .map(smppProfileMapper::toDto);
    }

    /**
     * Delete the smppProfile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmppProfile : {}", id);
        smppProfileRepository.deleteById(id);
    }
}
