package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.OrcamentoRepository;
import com.mycompany.myapp.service.OrcamentoQueryService;
import com.mycompany.myapp.service.OrcamentoService;
import com.mycompany.myapp.service.criteria.OrcamentoCriteria;
import com.mycompany.myapp.service.dto.OrcamentoDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Orcamento}.
 */
@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoResource {

    private final Logger log = LoggerFactory.getLogger(OrcamentoResource.class);

    private static final String ENTITY_NAME = "serviceOrcamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrcamentoService orcamentoService;

    private final OrcamentoRepository orcamentoRepository;

    private final OrcamentoQueryService orcamentoQueryService;

    public OrcamentoResource(
        OrcamentoService orcamentoService,
        OrcamentoRepository orcamentoRepository,
        OrcamentoQueryService orcamentoQueryService
    ) {
        this.orcamentoService = orcamentoService;
        this.orcamentoRepository = orcamentoRepository;
        this.orcamentoQueryService = orcamentoQueryService;
    }

    /**
     * {@code POST  /orcamentos} : Create a new orcamento.
     *
     * @param orcamentoDTO the orcamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orcamentoDTO, or with status {@code 400 (Bad Request)} if the orcamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrcamentoDTO> createOrcamento(@Valid @RequestBody OrcamentoDTO orcamentoDTO) throws URISyntaxException {
        log.debug("REST request to save Orcamento : {}", orcamentoDTO);
        if (orcamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new orcamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrcamentoDTO result = orcamentoService.save(orcamentoDTO);
        return ResponseEntity
            .created(new URI("/api/orcamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orcamentos/:id} : Updates an existing orcamento.
     *
     * @param id the id of the orcamentoDTO to save.
     * @param orcamentoDTO the orcamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orcamentoDTO,
     * or with status {@code 400 (Bad Request)} if the orcamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orcamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoDTO> updateOrcamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrcamentoDTO orcamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Orcamento : {}, {}", id, orcamentoDTO);
        if (orcamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orcamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orcamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrcamentoDTO result = orcamentoService.update(orcamentoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orcamentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orcamentos/:id} : Partial updates given fields of an existing orcamento, field will ignore if it is null
     *
     * @param id the id of the orcamentoDTO to save.
     * @param orcamentoDTO the orcamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orcamentoDTO,
     * or with status {@code 400 (Bad Request)} if the orcamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orcamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orcamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrcamentoDTO> partialUpdateOrcamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrcamentoDTO orcamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Orcamento partially : {}, {}", id, orcamentoDTO);
        if (orcamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orcamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orcamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrcamentoDTO> result = orcamentoService.partialUpdate(orcamentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orcamentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /orcamentos} : get all the orcamentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orcamentos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrcamentoDTO>> getAllOrcamentos(
        OrcamentoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Orcamentos by criteria: {}", criteria);

        Page<OrcamentoDTO> page = orcamentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orcamentos/count} : count all the orcamentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrcamentos(OrcamentoCriteria criteria) {
        log.debug("REST request to count Orcamentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(orcamentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /orcamentos/:id} : get the "id" orcamento.
     *
     * @param id the id of the orcamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orcamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoDTO> getOrcamento(@PathVariable("id") Long id) {
        log.debug("REST request to get Orcamento : {}", id);
        Optional<OrcamentoDTO> orcamentoDTO = orcamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orcamentoDTO);
    }

    /**
     * {@code DELETE  /orcamentos/:id} : delete the "id" orcamento.
     *
     * @param id the id of the orcamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrcamento(@PathVariable("id") Long id) {
        log.debug("REST request to delete Orcamento : {}", id);
        orcamentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
