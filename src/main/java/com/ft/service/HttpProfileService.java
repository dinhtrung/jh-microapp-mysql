package com.ft.service;

import com.ft.domain.HttpProfile;
import com.ft.repository.HttpProfileRepository;
import com.ft.service.dto.HttpProfileDTO;
import com.ft.service.mapper.HttpProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing HttpProfile.
 */
@Service
@Transactional
public class HttpProfileService {

    private final Logger log = LoggerFactory.getLogger(HttpProfileService.class);

    private HttpProfileRepository httpProfileRepository;

    private HttpProfileMapper httpProfileMapper;

    public HttpProfileService(HttpProfileRepository httpProfileRepository, HttpProfileMapper httpProfileMapper) {
        this.httpProfileRepository = httpProfileRepository;
        this.httpProfileMapper = httpProfileMapper;
    }

    /**
     * Save a httpProfile.
     *
     * @param httpProfileDTO the entity to save
     * @return the persisted entity
     */
    public HttpProfileDTO save(HttpProfileDTO httpProfileDTO) {
        log.debug("Request to save HttpProfile : {}", httpProfileDTO);

        HttpProfile httpProfile = httpProfileMapper.toEntity(httpProfileDTO);
        httpProfile = httpProfileRepository.save(httpProfile);
        return httpProfileMapper.toDto(httpProfile);
    }

    /**
     * Get all the httpProfiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HttpProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HttpProfiles");
        return httpProfileRepository.findAll(pageable)
            .map(httpProfileMapper::toDto);
    }


    /**
     * Get one httpProfile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HttpProfileDTO> findOne(Long id) {
        log.debug("Request to get HttpProfile : {}", id);
        return httpProfileRepository.findById(id)
            .map(httpProfileMapper::toDto);
    }

    /**
     * Delete the httpProfile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HttpProfile : {}", id);
        httpProfileRepository.deleteById(id);
    }
}
