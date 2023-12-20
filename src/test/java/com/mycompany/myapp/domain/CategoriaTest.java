package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoriaTestSamples.*;
import static com.mycompany.myapp.domain.TransacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categoria.class);
        Categoria categoria1 = getCategoriaSample1();
        Categoria categoria2 = new Categoria();
        assertThat(categoria1).isNotEqualTo(categoria2);

        categoria2.setId(categoria1.getId());
        assertThat(categoria1).isEqualTo(categoria2);

        categoria2 = getCategoriaSample2();
        assertThat(categoria1).isNotEqualTo(categoria2);
    }

    @Test
    void transacaoTest() throws Exception {
        Categoria categoria = getCategoriaRandomSampleGenerator();
        Transacao transacaoBack = getTransacaoRandomSampleGenerator();

        categoria.addTransacao(transacaoBack);
        assertThat(categoria.getTransacaos()).containsOnly(transacaoBack);
        assertThat(transacaoBack.getCategoria()).isEqualTo(categoria);

        categoria.removeTransacao(transacaoBack);
        assertThat(categoria.getTransacaos()).doesNotContain(transacaoBack);
        assertThat(transacaoBack.getCategoria()).isNull();

        categoria.transacaos(new HashSet<>(Set.of(transacaoBack)));
        assertThat(categoria.getTransacaos()).containsOnly(transacaoBack);
        assertThat(transacaoBack.getCategoria()).isEqualTo(categoria);

        categoria.setTransacaos(new HashSet<>());
        assertThat(categoria.getTransacaos()).doesNotContain(transacaoBack);
        assertThat(transacaoBack.getCategoria()).isNull();
    }
}
