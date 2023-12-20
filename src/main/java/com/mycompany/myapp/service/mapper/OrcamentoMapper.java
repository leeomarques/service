package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Orcamento;
import com.mycompany.myapp.service.dto.OrcamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orcamento} and its DTO {@link OrcamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrcamentoMapper extends EntityMapper<OrcamentoDTO, Orcamento> {}
