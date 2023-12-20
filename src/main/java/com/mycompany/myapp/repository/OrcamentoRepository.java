package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Orcamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orcamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long>, JpaSpecificationExecutor<Orcamento> {}
