package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Orcamento.
 */
@Entity
@Table(name = "orcamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orcamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ano", nullable = false)
    private Integer ano;

    @NotNull
    @Column(name = "mes", nullable = false)
    private String mes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orcamento")
    @JsonIgnoreProperties(value = { "orcamento", "categoria" }, allowSetters = true)
    private Set<Transacao> transacaos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orcamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAno() {
        return this.ano;
    }

    public Orcamento ano(Integer ano) {
        this.setAno(ano);
        return this;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getMes() {
        return this.mes;
    }

    public Orcamento mes(String mes) {
        this.setMes(mes);
        return this;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Set<Transacao> getTransacaos() {
        return this.transacaos;
    }

    public void setTransacaos(Set<Transacao> transacaos) {
        if (this.transacaos != null) {
            this.transacaos.forEach(i -> i.setOrcamento(null));
        }
        if (transacaos != null) {
            transacaos.forEach(i -> i.setOrcamento(this));
        }
        this.transacaos = transacaos;
    }

    public Orcamento transacaos(Set<Transacao> transacaos) {
        this.setTransacaos(transacaos);
        return this;
    }

    public Orcamento addTransacao(Transacao transacao) {
        this.transacaos.add(transacao);
        transacao.setOrcamento(this);
        return this;
    }

    public Orcamento removeTransacao(Transacao transacao) {
        this.transacaos.remove(transacao);
        transacao.setOrcamento(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orcamento)) {
            return false;
        }
        return getId() != null && getId().equals(((Orcamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orcamento{" +
            "id=" + getId() +
            ", ano=" + getAno() +
            ", mes='" + getMes() + "'" +
            "}";
    }
}
