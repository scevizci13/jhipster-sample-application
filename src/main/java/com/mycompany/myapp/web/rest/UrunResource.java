package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Urun;
import com.mycompany.myapp.repository.UrunRepository;
import com.mycompany.myapp.service.UrunService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Urun}.
 */
@RestController
@RequestMapping("/api/uruns")
public class UrunResource {

    private static final Logger LOG = LoggerFactory.getLogger(UrunResource.class);

    private static final String ENTITY_NAME = "urun";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UrunService urunService;

    private final UrunRepository urunRepository;

    public UrunResource(UrunService urunService, UrunRepository urunRepository) {
        this.urunService = urunService;
        this.urunRepository = urunRepository;
    }

    /**
     * {@code POST  /uruns} : Create a new urun.
     *
     * @param urun the urun to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new urun, or with status {@code 400 (Bad Request)} if the urun has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Urun> createUrun(@Valid @RequestBody Urun urun) throws URISyntaxException {
        LOG.debug("REST request to save Urun : {}", urun);
        if (urun.getId() != null) {
            throw new BadRequestAlertException("A new urun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        urun = urunService.save(urun);
        return ResponseEntity.created(new URI("/api/uruns/" + urun.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, urun.getId().toString()))
            .body(urun);
    }

    /**
     * {@code PUT  /uruns/:id} : Updates an existing urun.
     *
     * @param id the id of the urun to save.
     * @param urun the urun to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urun,
     * or with status {@code 400 (Bad Request)} if the urun is not valid,
     * or with status {@code 500 (Internal Server Error)} if the urun couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Urun> updateUrun(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Urun urun)
        throws URISyntaxException {
        LOG.debug("REST request to update Urun : {}, {}", id, urun);
        if (urun.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, urun.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!urunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        urun = urunService.update(urun);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, urun.getId().toString()))
            .body(urun);
    }

    /**
     * {@code PATCH  /uruns/:id} : Partial updates given fields of an existing urun, field will ignore if it is null
     *
     * @param id the id of the urun to save.
     * @param urun the urun to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urun,
     * or with status {@code 400 (Bad Request)} if the urun is not valid,
     * or with status {@code 404 (Not Found)} if the urun is not found,
     * or with status {@code 500 (Internal Server Error)} if the urun couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Urun> partialUpdateUrun(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Urun urun
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Urun partially : {}, {}", id, urun);
        if (urun.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, urun.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!urunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Urun> result = urunService.partialUpdate(urun);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, urun.getId().toString())
        );
    }

    /**
     * {@code GET  /uruns} : get all the uruns.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uruns in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Urun>> getAllUruns(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Uruns");
        Page<Urun> page = urunService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /uruns/:id} : get the "id" urun.
     *
     * @param id the id of the urun to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the urun, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Urun> getUrun(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Urun : {}", id);
        Optional<Urun> urun = urunService.findOne(id);
        return ResponseUtil.wrapOrNotFound(urun);
    }

    /**
     * {@code DELETE  /uruns/:id} : delete the "id" urun.
     *
     * @param id the id of the urun to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrun(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Urun : {}", id);
        urunService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
