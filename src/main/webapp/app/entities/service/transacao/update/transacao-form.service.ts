import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITransacao, NewTransacao } from '../transacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransacao for edit and NewTransacaoFormGroupInput for create.
 */
type TransacaoFormGroupInput = ITransacao | PartialWithRequiredKeyOf<NewTransacao>;

type TransacaoFormDefaults = Pick<NewTransacao, 'id'>;

type TransacaoFormGroupContent = {
  id: FormControl<ITransacao['id'] | NewTransacao['id']>;
  valor: FormControl<ITransacao['valor']>;
  orcamento: FormControl<ITransacao['orcamento']>;
  categoria: FormControl<ITransacao['categoria']>;
};

export type TransacaoFormGroup = FormGroup<TransacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransacaoFormService {
  createTransacaoFormGroup(transacao: TransacaoFormGroupInput = { id: null }): TransacaoFormGroup {
    const transacaoRawValue = {
      ...this.getFormDefaults(),
      ...transacao,
    };
    return new FormGroup<TransacaoFormGroupContent>({
      id: new FormControl(
        { value: transacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      valor: new FormControl(transacaoRawValue.valor, {
        validators: [Validators.required],
      }),
      orcamento: new FormControl(transacaoRawValue.orcamento),
      categoria: new FormControl(transacaoRawValue.categoria),
    });
  }

  getTransacao(form: TransacaoFormGroup): ITransacao | NewTransacao {
    return form.getRawValue() as ITransacao | NewTransacao;
  }

  resetForm(form: TransacaoFormGroup, transacao: TransacaoFormGroupInput): void {
    const transacaoRawValue = { ...this.getFormDefaults(), ...transacao };
    form.reset(
      {
        ...transacaoRawValue,
        id: { value: transacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransacaoFormDefaults {
    return {
      id: null,
    };
  }
}
