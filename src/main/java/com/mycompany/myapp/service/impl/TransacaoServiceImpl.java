package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Transacao;
import com.mycompany.myapp.repository.TransacaoRepository;
import com.mycompany.myapp.service.TransacaoService;
import com.mycompany.myapp.service.dto.TransacaoDTO;
import com.mycompany.myapp.service.mapper.TransacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Transacao}.
 */
@Service
@Transactional
public class TransacaoServiceImpl implements TransacaoService {

    private final Logger log = LoggerFactory.getLogger(TransacaoServiceImpl.class);

    private final TransacaoRepository transacaoRepository;

    private final TransacaoMapper transacaoMapper;

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository, TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.transacaoMapper = transacaoMapper;
    }

    @Override
    public TransacaoDTO save(TransacaoDTO transacaoDTO) {
        log.debug("Request to save Transacao : {}", transacaoDTO);
        Transacao transacao = transacaoMapper.toEntity(transacaoDTO);
        transacao = transacaoRepository.save(transacao);
        return transacaoMapper.toDto(transacao);
    }

    @Override
    public TransacaoDTO update(TransacaoDTO transacaoDTO) {
        log.debug("Request to update Transacao : {}", transacaoDTO);
        Transacao transacao = transacaoMapper.toEntity(transacaoDTO);
        transacao = transacaoRepository.save(transacao);
        return transacaoMapper.toDto(transacao);
    }

    @Override
    public Optional<TransacaoDTO> partialUpdate(TransacaoDTO transacaoDTO) {
        log.debug("Request to partially update Transacao : {}", transacaoDTO);

        return transacaoRepository
            .findById(transacaoDTO.getId())
            .map(existingTransacao -> {
                transacaoMapper.partialUpdate(existingTransacao, transacaoDTO);

                return existingTransacao;
            })
            .map(transacaoRepository::save)
            .map(transacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transacaos");
        return transacaoRepository.findAll(pageable).map(transacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransacaoDTO> findOne(Long id) {
        log.debug("Request to get Transacao : {}", id);
        return transacaoRepository.findById(id).map(transacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transacao : {}", id);
        transacaoRepository.deleteById(id);
    }
}
