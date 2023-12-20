import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrcamento } from 'app/entities/service/orcamento/orcamento.model';
import { OrcamentoService } from 'app/entities/service/orcamento/service/orcamento.service';
import { ICategoria } from 'app/entities/service/categoria/categoria.model';
import { CategoriaService } from 'app/entities/service/categoria/service/categoria.service';
import { TransacaoService } from '../service/transacao.service';
import { ITransacao } from '../transacao.model';
import { TransacaoFormService, TransacaoFormGroup } from './transacao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-transacao-update',
  templateUrl: './transacao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransacaoUpdateComponent implements OnInit {
  isSaving = false;
  transacao: ITransacao | null = null;

  orcamentosSharedCollection: IOrcamento[] = [];
  categoriasSharedCollection: ICategoria[] = [];

  editForm: TransacaoFormGroup = this.transacaoFormService.createTransacaoFormGroup();

  constructor(
    protected transacaoService: TransacaoService,
    protected transacaoFormService: TransacaoFormService,
    protected orcamentoService: OrcamentoService,
    protected categoriaService: CategoriaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareOrcamento = (o1: IOrcamento | null, o2: IOrcamento | null): boolean => this.orcamentoService.compareOrcamento(o1, o2);

  compareCategoria = (o1: ICategoria | null, o2: ICategoria | null): boolean => this.categoriaService.compareCategoria(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transacao }) => {
      this.transacao = transacao;
      if (transacao) {
        this.updateForm(transacao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transacao = this.transacaoFormService.getTransacao(this.editForm);
    if (transacao.id !== null) {
      this.subscribeToSaveResponse(this.transacaoService.update(transacao));
    } else {
      this.subscribeToSaveResponse(this.transacaoService.create(transacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransacao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(transacao: ITransacao): void {
    this.transacao = transacao;
    this.transacaoFormService.resetForm(this.editForm, transacao);

    this.orcamentosSharedCollection = this.orcamentoService.addOrcamentoToCollectionIfMissing<IOrcamento>(
      this.orcamentosSharedCollection,
      transacao.orcamento,
    );
    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(
      this.categoriasSharedCollection,
      transacao.categoria,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orcamentoService
      .query()
      .pipe(map((res: HttpResponse<IOrcamento[]>) => res.body ?? []))
      .pipe(
        map((orcamentos: IOrcamento[]) =>
          this.orcamentoService.addOrcamentoToCollectionIfMissing<IOrcamento>(orcamentos, this.transacao?.orcamento),
        ),
      )
      .subscribe((orcamentos: IOrcamento[]) => (this.orcamentosSharedCollection = orcamentos));

    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(categorias, this.transacao?.categoria),
        ),
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));
  }
}
