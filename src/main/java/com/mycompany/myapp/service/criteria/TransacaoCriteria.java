package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Transacao} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TransacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter valor;

    private LongFilter orcamentoId;

    private LongFilter categoriaId;

    private Boolean distinct;

    public TransacaoCriteria() {}

    public TransacaoCriteria(TransacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.orcamentoId = other.orcamentoId == null ? null : other.orcamentoId.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransacaoCriteria copy() {
        return new TransacaoCriteria(this);
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

    public DoubleFilter getValor() {
        return valor;
    }

    public DoubleFilter valor() {
        if (valor == null) {
            valor = new DoubleFilter();
        }
        return valor;
    }

    public void setValor(DoubleFilter valor) {
        this.valor = valor;
    }

    public LongFilter getOrcamentoId() {
        return orcamentoId;
    }

    public LongFilter orcamentoId() {
        if (orcamentoId == null) {
            orcamentoId = new LongFilter();
        }
        return orcamentoId;
    }

    public void setOrcamentoId(LongFilter orcamentoId) {
        this.orcamentoId = orcamentoId;
    }

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            categoriaId = new LongFilter();
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
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
        final TransacaoCriteria that = (TransacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(orcamentoId, that.orcamentoId) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, orcamentoId, categoriaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (orcamentoId != null ? "orcamentoId=" + orcamentoId + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
