package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Musteri;
import com.mycompany.myapp.repository.MusteriRepository;
import com.mycompany.myapp.service.MusteriService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Musteri}.
 */
@RestController
@RequestMapping("/api/musteris")
public class MusteriResource {

    private static final Logger LOG = LoggerFactory.getLogger(MusteriResource.class);

    private static final String ENTITY_NAME = "musteri";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MusteriService musteriService;

    private final MusteriRepository musteriRepository;

    public MusteriResource(MusteriService musteriService, MusteriRepository musteriRepository) {
        this.musteriService = musteriService;
        this.musteriRepository = musteriRepository;
    }

    /**
     * {@code POST  /musteris} : Create a new musteri.
     *
     * @param musteri the musteri to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new musteri, or with status {@code 400 (Bad Request)} if the musteri has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Musteri> createMusteri(@Valid @RequestBody Musteri musteri) throws URISyntaxException {
        LOG.debug("REST request to save Musteri : {}", musteri);
        if (musteri.getId() != null) {
            throw new BadRequestAlertException("A new musteri cannot already have an ID", ENTITY_NAME, "idexists");
        }
        musteri = musteriService.save(musteri);
        return ResponseEntity.created(new URI("/api/musteris/" + musteri.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, musteri.getId().toString()))
            .body(musteri);
    }

    /**
     * {@code PUT  /musteris/:id} : Updates an existing musteri.
     *
     * @param id the id of the musteri to save.
     * @param musteri the musteri to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musteri,
     * or with status {@code 400 (Bad Request)} if the musteri is not valid,
     * or with status {@code 500 (Internal Server Error)} if the musteri couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Musteri> updateMusteri(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Musteri musteri
    ) throws URISyntaxException {
        LOG.debug("REST request to update Musteri : {}, {}", id, musteri);
        if (musteri.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musteri.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musteriRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        musteri = musteriService.update(musteri);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, musteri.getId().toString()))
            .body(musteri);
    }

    /**
     * {@code PATCH  /musteris/:id} : Partial updates given fields of an existing musteri, field will ignore if it is null
     *
     * @param id the id of the musteri to save.
     * @param musteri the musteri to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musteri,
     * or with status {@code 400 (Bad Request)} if the musteri is not valid,
     * or with status {@code 404 (Not Found)} if the musteri is not found,
     * or with status {@code 500 (Internal Server Error)} if the musteri couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Musteri> partialUpdateMusteri(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Musteri musteri
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Musteri partially : {}, {}", id, musteri);
        if (musteri.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musteri.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musteriRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Musteri> result = musteriService.partialUpdate(musteri);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, musteri.getId().toString())
        );
    }

    /**
     * {@code GET  /musteris} : get all the musteris.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of musteris in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Musteri>> getAllMusteris(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Musteris");
        Page<Musteri> page = musteriService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /musteris/:id} : get the "id" musteri.
     *
     * @param id the id of the musteri to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the musteri, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Musteri> getMusteri(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Musteri : {}", id);
        Optional<Musteri> musteri = musteriService.findOne(id);
        return ResponseUtil.wrapOrNotFound(musteri);
    }

    /**
     * {@code DELETE  /musteris/:id} : delete the "id" musteri.
     *
     * @param id the id of the musteri to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMusteri(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Musteri : {}", id);
        musteriService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
