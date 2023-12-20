package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.OrcamentoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Orcamento}.
 */
public interface OrcamentoService {
    /**
     * Save a orcamento.
     *
     * @param orcamentoDTO the entity to save.
     * @return the persisted entity.
     */
    OrcamentoDTO save(OrcamentoDTO orcamentoDTO);

    /**
     * Updates a orcamento.
     *
     * @param orcamentoDTO the entity to update.
     * @return the persisted entity.
     */
    OrcamentoDTO update(OrcamentoDTO orcamentoDTO);

    /**
     * Partially updates a orcamento.
     *
     * @param orcamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrcamentoDTO> partialUpdate(OrcamentoDTO orcamentoDTO);

    /**
     * Get all the orcamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrcamentoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orcamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrcamentoDTO> findOne(Long id);

    /**
     * Delete the "id" orcamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
