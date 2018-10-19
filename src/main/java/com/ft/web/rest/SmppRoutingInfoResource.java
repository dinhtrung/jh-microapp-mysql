package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.service.SmppRoutingInfoService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.SmppRoutingInfoDTO;
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
 * REST controller for managing SmppRoutingInfo.
 */
@RestController
@RequestMapping("/api")
public class SmppRoutingInfoResource {

    private final Logger log = LoggerFactory.getLogger(SmppRoutingInfoResource.class);

    private static final String ENTITY_NAME = "appSmppRoutingInfo";

    private SmppRoutingInfoService smppRoutingInfoService;

    public SmppRoutingInfoResource(SmppRoutingInfoService smppRoutingInfoService) {
        this.smppRoutingInfoService = smppRoutingInfoService;
    }

    /**
     * POST  /smpp-routing-infos : Create a new smppRoutingInfo.
     *
     * @param smppRoutingInfoDTO the smppRoutingInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smppRoutingInfoDTO, or with status 400 (Bad Request) if the smppRoutingInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smpp-routing-infos")
    @Timed
    public ResponseEntity<SmppRoutingInfoDTO> createSmppRoutingInfo(@Valid @RequestBody SmppRoutingInfoDTO smppRoutingInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SmppRoutingInfo : {}", smppRoutingInfoDTO);
        if (smppRoutingInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new smppRoutingInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmppRoutingInfoDTO result = smppRoutingInfoService.save(smppRoutingInfoDTO);
        return ResponseEntity.created(new URI("/api/smpp-routing-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smpp-routing-infos : Updates an existing smppRoutingInfo.
     *
     * @param smppRoutingInfoDTO the smppRoutingInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smppRoutingInfoDTO,
     * or with status 400 (Bad Request) if the smppRoutingInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the smppRoutingInfoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smpp-routing-infos")
    @Timed
    public ResponseEntity<SmppRoutingInfoDTO> updateSmppRoutingInfo(@Valid @RequestBody SmppRoutingInfoDTO smppRoutingInfoDTO) throws URISyntaxException {
        log.debug("REST request to update SmppRoutingInfo : {}", smppRoutingInfoDTO);
        if (smppRoutingInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmppRoutingInfoDTO result = smppRoutingInfoService.save(smppRoutingInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smppRoutingInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smpp-routing-infos : get all the smppRoutingInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smppRoutingInfos in body
     */
    @GetMapping("/smpp-routing-infos")
    @Timed
    public ResponseEntity<List<SmppRoutingInfoDTO>> getAllSmppRoutingInfos(Pageable pageable) {
        log.debug("REST request to get a page of SmppRoutingInfos");
        Page<SmppRoutingInfoDTO> page = smppRoutingInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/smpp-routing-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /smpp-routing-infos/:id : get the "id" smppRoutingInfo.
     *
     * @param id the id of the smppRoutingInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smppRoutingInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smpp-routing-infos/{id}")
    @Timed
    public ResponseEntity<SmppRoutingInfoDTO> getSmppRoutingInfo(@PathVariable Long id) {
        log.debug("REST request to get SmppRoutingInfo : {}", id);
        Optional<SmppRoutingInfoDTO> smppRoutingInfoDTO = smppRoutingInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smppRoutingInfoDTO);
    }

    /**
     * DELETE  /smpp-routing-infos/:id : delete the "id" smppRoutingInfo.
     *
     * @param id the id of the smppRoutingInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smpp-routing-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteSmppRoutingInfo(@PathVariable Long id) {
        log.debug("REST request to delete SmppRoutingInfo : {}", id);
        smppRoutingInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
