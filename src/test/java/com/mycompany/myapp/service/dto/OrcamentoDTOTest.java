package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrcamentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrcamentoDTO.class);
        OrcamentoDTO orcamentoDTO1 = new OrcamentoDTO();
        orcamentoDTO1.setId(1L);
        OrcamentoDTO orcamentoDTO2 = new OrcamentoDTO();
        assertThat(orcamentoDTO1).isNotEqualTo(orcamentoDTO2);
        orcamentoDTO2.setId(orcamentoDTO1.getId());
        assertThat(orcamentoDTO1).isEqualTo(orcamentoDTO2);
        orcamentoDTO2.setId(2L);
        assertThat(orcamentoDTO1).isNotEqualTo(orcamentoDTO2);
        orcamentoDTO1.setId(null);
        assertThat(orcamentoDTO1).isNotEqualTo(orcamentoDTO2);
    }
}
