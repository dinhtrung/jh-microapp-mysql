package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.service.SmppProfileService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.SmppProfileDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmppProfile.
 */
@RestController
@RequestMapping("/api")
public class SmppProfileResource {

    private final Logger log = LoggerFactory.getLogger(SmppProfileResource.class);

    private static final String ENTITY_NAME = "appSmppProfile";

    private SmppProfileService smppProfileService;

    public SmppProfileResource(SmppProfileService smppProfileService) {
        this.smppProfileService = smppProfileService;
    }

    /**
     * POST  /smpp-profiles : Create a new smppProfile.
     *
     * @param smppProfileDTO the smppProfileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smppProfileDTO, or with status 400 (Bad Request) if the smppProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smpp-profiles")
    @Timed
    public ResponseEntity<SmppProfileDTO> createSmppProfile(@Valid @RequestBody SmppProfileDTO smppProfileDTO) throws URISyntaxException {
        log.debug("REST request to save SmppProfile : {}", smppProfileDTO);
        if (smppProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new smppProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmppProfileDTO result = smppProfileService.save(smppProfileDTO);
        return ResponseEntity.created(new URI("/api/smpp-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smpp-profiles : Updates an existing smppProfile.
     *
     * @param smppProfileDTO the smppProfileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smppProfileDTO,
     * or with status 400 (Bad Request) if the smppProfileDTO is not valid,
     * or with status 500 (Internal Server Error) if the smppProfileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smpp-profiles")
    @Timed
    public ResponseEntity<SmppProfileDTO> updateSmppProfile(@Valid @RequestBody SmppProfileDTO smppProfileDTO) throws URISyntaxException {
        log.debug("REST request to update SmppProfile : {}", smppProfileDTO);
        if (smppProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmppProfileDTO result = smppProfileService.save(smppProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smppProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smpp-profiles : get all the smppProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smppProfiles in body
     */
    @GetMapping("/smpp-profiles")
    @Timed
    public ResponseEntity<List<SmppProfileDTO>> getAllSmppProfiles(Pageable pageable) {
        log.debug("REST request to get a page of SmppProfiles");
        Page<SmppProfileDTO> page = smppProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/smpp-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /smpp-profiles/:id : get the "id" smppProfile.
     *
     * @param id the id of the smppProfileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smppProfileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smpp-profiles/{id}")
    @Timed
    public ResponseEntity<SmppProfileDTO> getSmppProfile(@PathVariable Long id) {
        log.debug("REST request to get SmppProfile : {}", id);
        Optional<SmppProfileDTO> smppProfileDTO = smppProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smppProfileDTO);
    }

    /**
     * DELETE  /smpp-profiles/:id : delete the "id" smppProfile.
     *
     * @param id the id of the smppProfileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smpp-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteSmppProfile(@PathVariable Long id) {
        log.debug("REST request to delete SmppProfile : {}", id);
        smppProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
