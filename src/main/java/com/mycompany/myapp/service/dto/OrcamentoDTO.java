package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Orcamento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrcamentoDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer ano;

    @NotNull
    private String mes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrcamentoDTO)) {
            return false;
        }

        OrcamentoDTO orcamentoDTO = (OrcamentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orcamentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrcamentoDTO{" +
            "id=" + getId() +
            ", ano=" + getAno() +
            ", mes='" + getMes() + "'" +
            "}";
    }
}
