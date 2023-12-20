package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoriaTestSamples.*;
import static com.mycompany.myapp.domain.OrcamentoTestSamples.*;
import static com.mycompany.myapp.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transacao.class);
        Transacao transacao1 = getTransacaoSample1();
        Transacao transacao2 = new Transacao();
        assertThat(transacao1).isNotEqualTo(transacao2);

        transacao2.setId(transacao1.getId());
        assertThat(transacao1).isEqualTo(transacao2);

        transacao2 = getTransacaoSample2();
        assertThat(transacao1).isNotEqualTo(transacao2);
    }

    @Test
    void orcamentoTest() throws Exception {
        Transacao transacao = getTransacaoRandomSampleGenerator();
        Orcamento orcamentoBack = getOrcamentoRandomSampleGenerator();

        transacao.setOrcamento(orcamentoBack);
        assertThat(transacao.getOrcamento()).isEqualTo(orcamentoBack);

        transacao.orcamento(null);
        assertThat(transacao.getOrcamento()).isNull();
    }

    @Test
    void categoriaTest() throws Exception {
        Transacao transacao = getTransacaoRandomSampleGenerator();
        Categoria categoriaBack = getCategoriaRandomSampleGenerator();

        transacao.setCategoria(categoriaBack);
        assertThat(transacao.getCategoria()).isEqualTo(categoriaBack);

        transacao.categoria(null);
        assertThat(transacao.getCategoria()).isNull();
    }
}
