package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Transacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private Double valor;

    private OrcamentoDTO orcamento;

    private CategoriaDTO categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public OrcamentoDTO getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(OrcamentoDTO orcamento) {
        this.orcamento = orcamento;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransacaoDTO)) {
            return false;
        }

        TransacaoDTO transacaoDTO = (TransacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransacaoDTO{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", orcamento=" + getOrcamento() +
            ", categoria=" + getCategoria() +
            "}";
    }
}
