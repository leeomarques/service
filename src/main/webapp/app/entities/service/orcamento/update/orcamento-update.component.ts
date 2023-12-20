import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrcamento } from '../orcamento.model';
import { OrcamentoService } from '../service/orcamento.service';
import { OrcamentoFormService, OrcamentoFormGroup } from './orcamento-form.service';

@Component({
  standalone: true,
  selector: 'jhi-orcamento-update',
  templateUrl: './orcamento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrcamentoUpdateComponent implements OnInit {
  isSaving = false;
  orcamento: IOrcamento | null = null;

  editForm: OrcamentoFormGroup = this.orcamentoFormService.createOrcamentoFormGroup();

  constructor(
    protected orcamentoService: OrcamentoService,
    protected orcamentoFormService: OrcamentoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orcamento }) => {
      this.orcamento = orcamento;
      if (orcamento) {
        this.updateForm(orcamento);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orcamento = this.orcamentoFormService.getOrcamento(this.editForm);
    if (orcamento.id !== null) {
      this.subscribeToSaveResponse(this.orcamentoService.update(orcamento));
    } else {
      this.subscribeToSaveResponse(this.orcamentoService.create(orcamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrcamento>>): void {
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

  protected updateForm(orcamento: IOrcamento): void {
    this.orcamento = orcamento;
    this.orcamentoFormService.resetForm(this.editForm, orcamento);
  }
}
