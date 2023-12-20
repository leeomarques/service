package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Orcamento;
import com.mycompany.myapp.repository.OrcamentoRepository;
import com.mycompany.myapp.service.OrcamentoService;
import com.mycompany.myapp.service.dto.OrcamentoDTO;
import com.mycompany.myapp.service.mapper.OrcamentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Orcamento}.
 */
@Service
@Transactional
public class OrcamentoServiceImpl implements OrcamentoService {

    private final Logger log = LoggerFactory.getLogger(OrcamentoServiceImpl.class);

    private final OrcamentoRepository orcamentoRepository;

    private final OrcamentoMapper orcamentoMapper;

    public OrcamentoServiceImpl(OrcamentoRepository orcamentoRepository, OrcamentoMapper orcamentoMapper) {
        this.orcamentoRepository = orcamentoRepository;
        this.orcamentoMapper = orcamentoMapper;
    }

    @Override
    public OrcamentoDTO save(OrcamentoDTO orcamentoDTO) {
        log.debug("Request to save Orcamento : {}", orcamentoDTO);
        Orcamento orcamento = orcamentoMapper.toEntity(orcamentoDTO);
        orcamento = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toDto(orcamento);
    }

    @Override
    public OrcamentoDTO update(OrcamentoDTO orcamentoDTO) {
        log.debug("Request to update Orcamento : {}", orcamentoDTO);
        Orcamento orcamento = orcamentoMapper.toEntity(orcamentoDTO);
        orcamento = orcamentoRepository.save(orcamento);
        return orcamentoMapper.toDto(orcamento);
    }

    @Override
    public Optional<OrcamentoDTO> partialUpdate(OrcamentoDTO orcamentoDTO) {
        log.debug("Request to partially update Orcamento : {}", orcamentoDTO);

        return orcamentoRepository
            .findById(orcamentoDTO.getId())
            .map(existingOrcamento -> {
                orcamentoMapper.partialUpdate(existingOrcamento, orcamentoDTO);

                return existingOrcamento;
            })
            .map(orcamentoRepository::save)
            .map(orcamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrcamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orcamentos");
        return orcamentoRepository.findAll(pageable).map(orcamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrcamentoDTO> findOne(Long id) {
        log.debug("Request to get Orcamento : {}", id);
        return orcamentoRepository.findById(id).map(orcamentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Orcamento : {}", id);
        orcamentoRepository.deleteById(id);
    }
}
