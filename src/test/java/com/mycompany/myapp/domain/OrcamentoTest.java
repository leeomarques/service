package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.OrcamentoTestSamples.*;
import static com.mycompany.myapp.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrcamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orcamento.class);
        Orcamento orcamento1 = getOrcamentoSample1();
        Orcamento orcamento2 = new Orcamento();
        assertThat(orcamento1).isNotEqualTo(orcamento2);

        orcamento2.setId(orcamento1.getId());
        assertThat(orcamento1).isEqualTo(orcamento2);

        orcamento2 = getOrcamentoSample2();
        assertThat(orcamento1).isNotEqualTo(orcamento2);
    }

    @Test
    void transacaoTest() throws Exception {
        Orcamento orcamento = getOrcamentoRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        orcamento.addTransacao(transacaoBack);
        assertThat(orcamento.getTransacaos()).containsOnly(transacaoBack);
        assertThat(transacaoBack.getOrcamento()).isEqualTo(orcamento);

        orcamento.removeTransacao(transacaoBack);
        assertThat(orcamento.getTransacaos()).doesNotContain(transacaoBack);
        assertThat(transacaoBack.getOrcamento()).isNull();

        orcamento.transacaos(new HashSet<>(Set.of(transacaoBack)));
        assertThat(orcamento.getTransacaos()).containsOnly(transacaoBack);
        assertThat(transacaoBack.getOrcamento()).isEqualTo(orcamento);

        orcamento.setTransacaos(new HashSet<>());
        assertThat(orcamento.getTransacaos()).doesNotContain(transacaoBack);
        assertThat(transacaoBack.getOrcamento()).isNull();
    }
}
