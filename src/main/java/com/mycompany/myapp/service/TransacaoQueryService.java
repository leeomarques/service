package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Transacao;
import com.mycompany.myapp.repository.TransacaoRepository;
import com.mycompany.myapp.service.criteria.TransacaoCriteria;
import com.mycompany.myapp.service.dto.TransacaoDTO;
import com.mycompany.myapp.service.mapper.TransacaoMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Transacao} entities in the database.
 * The main input is a {@link TransacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransacaoDTO} or a {@link Page} of {@link TransacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransacaoQueryService extends QueryService<Transacao> {

    private final Logger log = LoggerFactory.getLogger(TransacaoQueryService.class);

    private final TransacaoRepository transacaoRepository;

    private final TransacaoMapper transacaoMapper;

    public TransacaoQueryService(TransacaoRepository transacaoRepository, TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.transacaoMapper = transacaoMapper;
    }

    /**
     * Return a {@link List} of {@link TransacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransacaoDTO> findByCriteria(TransacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transacao> specification = createSpecification(criteria);
        return transacaoMapper.toDto(transacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransacaoDTO> findByCriteria(TransacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transacao> specification = createSpecification(criteria);
        return transacaoRepository.findAll(specification, page).map(transacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transacao> specification = createSpecification(criteria);
        return transacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link TransacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transacao> createSpecification(TransacaoCriteria criteria) {
        Specification<Transacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transacao_.id));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), Transacao_.valor));
            }
            if (criteria.getOrcamentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrcamentoId(),
                            root -> root.join(Transacao_.orcamento, JoinType.LEFT).get(Orcamento_.id)
                        )
                    );
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Transacao_.categoria, JoinType.LEFT).get(Categoria_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
