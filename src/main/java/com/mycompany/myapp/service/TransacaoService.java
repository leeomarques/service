package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TransacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Transacao}.
 */
public interface TransacaoService {
    /**
     * Save a transacao.
     *
     * @param transacaoDTO the entity to save.
     * @return the persisted entity.
     */
    TransacaoDTO save(TransacaoDTO transacaoDTO);

    /**
     * Updates a transacao.
     *
     * @param transacaoDTO the entity to update.
     * @return the persisted entity.
     */
    TransacaoDTO update(TransacaoDTO transacaoDTO);

    /**
     * Partially updates a transacao.
     *
     * @param transacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransacaoDTO> partialUpdate(TransacaoDTO transacaoDTO);

    /**
     * Get all the transacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransacaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransacaoDTO> findOne(Long id);

    /**
     * Delete the "id" transacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
