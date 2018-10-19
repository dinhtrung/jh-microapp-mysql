package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.service.HttpProfileService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.HttpProfileDTO;
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
 * REST controller for managing HttpProfile.
 */
@RestController
@RequestMapping("/api")
public class HttpProfileResource {

    private final Logger log = LoggerFactory.getLogger(HttpProfileResource.class);

    private static final String ENTITY_NAME = "appHttpProfile";

    private HttpProfileService httpProfileService;

    public HttpProfileResource(HttpProfileService httpProfileService) {
        this.httpProfileService = httpProfileService;
    }

    /**
     * POST  /http-profiles : Create a new httpProfile.
     *
     * @param httpProfileDTO the httpProfileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new httpProfileDTO, or with status 400 (Bad Request) if the httpProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/http-profiles")
    @Timed
    public ResponseEntity<HttpProfileDTO> createHttpProfile(@Valid @RequestBody HttpProfileDTO httpProfileDTO) throws URISyntaxException {
        log.debug("REST request to save HttpProfile : {}", httpProfileDTO);
        if (httpProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new httpProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HttpProfileDTO result = httpProfileService.save(httpProfileDTO);
        return ResponseEntity.created(new URI("/api/http-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /http-profiles : Updates an existing httpProfile.
     *
     * @param httpProfileDTO the httpProfileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated httpProfileDTO,
     * or with status 400 (Bad Request) if the httpProfileDTO is not valid,
     * or with status 500 (Internal Server Error) if the httpProfileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/http-profiles")
    @Timed
    public ResponseEntity<HttpProfileDTO> updateHttpProfile(@Valid @RequestBody HttpProfileDTO httpProfileDTO) throws URISyntaxException {
        log.debug("REST request to update HttpProfile : {}", httpProfileDTO);
        if (httpProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HttpProfileDTO result = httpProfileService.save(httpProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, httpProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /http-profiles : get all the httpProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of httpProfiles in body
     */
    @GetMapping("/http-profiles")
    @Timed
    public ResponseEntity<List<HttpProfileDTO>> getAllHttpProfiles(Pageable pageable) {
        log.debug("REST request to get a page of HttpProfiles");
        Page<HttpProfileDTO> page = httpProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/http-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /http-profiles/:id : get the "id" httpProfile.
     *
     * @param id the id of the httpProfileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the httpProfileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/http-profiles/{id}")
    @Timed
    public ResponseEntity<HttpProfileDTO> getHttpProfile(@PathVariable Long id) {
        log.debug("REST request to get HttpProfile : {}", id);
        Optional<HttpProfileDTO> httpProfileDTO = httpProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(httpProfileDTO);
    }

    /**
     * DELETE  /http-profiles/:id : delete the "id" httpProfile.
     *
     * @param id the id of the httpProfileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/http-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteHttpProfile(@PathVariable Long id) {
        log.debug("REST request to delete HttpProfile : {}", id);
        httpProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
