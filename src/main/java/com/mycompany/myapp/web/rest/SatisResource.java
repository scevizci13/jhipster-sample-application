package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Satis;
import com.mycompany.myapp.repository.SatisRepository;
import com.mycompany.myapp.service.SatisService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Satis}.
 */
@RestController
@RequestMapping("/api/satis")
public class SatisResource {

    private static final Logger LOG = LoggerFactory.getLogger(SatisResource.class);

    private static final String ENTITY_NAME = "satis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SatisService satisService;

    private final SatisRepository satisRepository;

    public SatisResource(SatisService satisService, SatisRepository satisRepository) {
        this.satisService = satisService;
        this.satisRepository = satisRepository;
    }

    /**
     * {@code POST  /satis} : Create a new satis.
     *
     * @param satis the satis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new satis, or with status {@code 400 (Bad Request)} if the satis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Satis> createSatis(@Valid @RequestBody Satis satis) throws URISyntaxException {
        LOG.debug("REST request to save Satis : {}", satis);
        if (satis.getId() != null) {
            throw new BadRequestAlertException("A new satis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        satis = satisService.save(satis);
        return ResponseEntity.created(new URI("/api/satis/" + satis.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, satis.getId().toString()))
            .body(satis);
    }

    /**
     * {@code PUT  /satis/:id} : Updates an existing satis.
     *
     * @param id the id of the satis to save.
     * @param satis the satis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated satis,
     * or with status {@code 400 (Bad Request)} if the satis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the satis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Satis> updateSatis(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Satis satis)
        throws URISyntaxException {
        LOG.debug("REST request to update Satis : {}, {}", id, satis);
        if (satis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, satis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!satisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        satis = satisService.update(satis);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, satis.getId().toString()))
            .body(satis);
    }

    /**
     * {@code PATCH  /satis/:id} : Partial updates given fields of an existing satis, field will ignore if it is null
     *
     * @param id the id of the satis to save.
     * @param satis the satis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated satis,
     * or with status {@code 400 (Bad Request)} if the satis is not valid,
     * or with status {@code 404 (Not Found)} if the satis is not found,
     * or with status {@code 500 (Internal Server Error)} if the satis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Satis> partialUpdateSatis(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Satis satis
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Satis partially : {}, {}", id, satis);
        if (satis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, satis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!satisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Satis> result = satisService.partialUpdate(satis);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, satis.getId().toString())
        );
    }

    /**
     * {@code GET  /satis} : get all the satis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of satis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Satis>> getAllSatis(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Satis");
        Page<Satis> page = satisService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /satis/:id} : get the "id" satis.
     *
     * @param id the id of the satis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the satis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Satis> getSatis(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Satis : {}", id);
        Optional<Satis> satis = satisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(satis);
    }

    /**
     * {@code DELETE  /satis/:id} : delete the "id" satis.
     *
     * @param id the id of the satis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSatis(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Satis : {}", id);
        satisService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
