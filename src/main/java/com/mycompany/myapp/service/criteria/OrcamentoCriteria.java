package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Orcamento} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.OrcamentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orcamentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrcamentoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter ano;

    private StringFilter mes;

    private LongFilter transacaoId;

    private Boolean distinct;

    public OrcamentoCriteria() {}

    public OrcamentoCriteria(OrcamentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ano = other.ano == null ? null : other.ano.copy();
        this.mes = other.mes == null ? null : other.mes.copy();
        this.transacaoId = other.transacaoId == null ? null : other.transacaoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrcamentoCriteria copy() {
        return new OrcamentoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getAno() {
        return ano;
    }

    public IntegerFilter ano() {
        if (ano == null) {
            ano = new IntegerFilter();
        }
        return ano;
    }

    public void setAno(IntegerFilter ano) {
        this.ano = ano;
    }

    public StringFilter getMes() {
        return mes;
    }

    public StringFilter mes() {
        if (mes == null) {
            mes = new StringFilter();
        }
        return mes;
    }

    public void setMes(StringFilter mes) {
        this.mes = mes;
    }

    public LongFilter getTransacaoId() {
        return transacaoId;
    }

    public LongFilter transacaoId() {
        if (transacaoId == null) {
            transacaoId = new LongFilter();
        }
        return transacaoId;
    }

    public void setTransacaoId(LongFilter transacaoId) {
        this.transacaoId = transacaoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrcamentoCriteria that = (OrcamentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ano, that.ano) &&
            Objects.equals(mes, that.mes) &&
            Objects.equals(transacaoId, that.transacaoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ano, mes, transacaoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrcamentoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ano != null ? "ano=" + ano + ", " : "") +
            (mes != null ? "mes=" + mes + ", " : "") +
            (transacaoId != null ? "transacaoId=" + transacaoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
