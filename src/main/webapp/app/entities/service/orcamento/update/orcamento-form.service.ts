import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrcamento, NewOrcamento } from '../orcamento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrcamento for edit and NewOrcamentoFormGroupInput for create.
 */
type OrcamentoFormGroupInput = IOrcamento | PartialWithRequiredKeyOf<NewOrcamento>;

type OrcamentoFormDefaults = Pick<NewOrcamento, 'id'>;

type OrcamentoFormGroupContent = {
  id: FormControl<IOrcamento['id'] | NewOrcamento['id']>;
  ano: FormControl<IOrcamento['ano']>;
  mes: FormControl<IOrcamento['mes']>;
};

export type OrcamentoFormGroup = FormGroup<OrcamentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrcamentoFormService {
  createOrcamentoFormGroup(orcamento: OrcamentoFormGroupInput = { id: null }): OrcamentoFormGroup {
    const orcamentoRawValue = {
      ...this.getFormDefaults(),
      ...orcamento,
    };
    return new FormGroup<OrcamentoFormGroupContent>({
      id: new FormControl(
        { value: orcamentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ano: new FormControl(orcamentoRawValue.ano, {
        validators: [Validators.required],
      }),
      mes: new FormControl(orcamentoRawValue.mes, {
        validators: [Validators.required],
      }),
    });
  }

  getOrcamento(form: OrcamentoFormGroup): IOrcamento | NewOrcamento {
    return form.getRawValue() as IOrcamento | NewOrcamento;
  }

  resetForm(form: OrcamentoFormGroup, orcamento: OrcamentoFormGroupInput): void {
    const orcamentoRawValue = { ...this.getFormDefaults(), ...orcamento };
    form.reset(
      {
        ...orcamentoRawValue,
        id: { value: orcamentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrcamentoFormDefaults {
    return {
      id: null,
    };
  }
}
