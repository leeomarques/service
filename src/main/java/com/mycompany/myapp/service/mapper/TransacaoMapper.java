package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Categoria;
import com.mycompany.myapp.domain.Orcamento;
import com.mycompany.myapp.domain.Transacao;
import com.mycompany.myapp.service.dto.CategoriaDTO;
import com.mycompany.myapp.service.dto.OrcamentoDTO;
import com.mycompany.myapp.service.dto.TransacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transacao} and its DTO {@link TransacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransacaoMapper extends EntityMapper<TransacaoDTO, Transacao> {
    @Mapping(target = "orcamento", source = "orcamento", qualifiedByName = "orcamentoId")
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "categoriaId")
    TransacaoDTO toDto(Transacao s);

    @Named("orcamentoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrcamentoDTO toDtoOrcamentoId(Orcamento orcamento);

    @Named("categoriaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoriaDTO toDtoCategoriaId(Categoria categoria);
}
