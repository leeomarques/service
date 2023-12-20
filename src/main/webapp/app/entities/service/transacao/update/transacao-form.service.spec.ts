import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../transacao.test-samples';

import { TransacaoFormService } from './transacao-form.service';

describe('Transacao Form Service', () => {
  let service: TransacaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransacaoFormService);
  });

  describe('Service methods', () => {
    describe('createTransacaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransacaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valor: expect.any(Object),
            orcamento: expect.any(Object),
            categoria: expect.any(Object),
          }),
        );
      });

      it('passing ITransacao should create a new form with FormGroup', () => {
        const formGroup = service.createTransacaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valor: expect.any(Object),
            orcamento: expect.any(Object),
            categoria: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransacao', () => {
      it('should return NewTransacao for default Transacao initial value', () => {
        const formGroup = service.createTransacaoFormGroup(sampleWithNewData);

        const transacao = service.getTransacao(formGroup) as any;

        expect(transacao).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransacao for empty Transacao initial value', () => {
        const formGroup = service.createTransacaoFormGroup();

        const transacao = service.getTransacao(formGroup) as any;

        expect(transacao).toMatchObject({});
      });

      it('should return ITransacao', () => {
        const formGroup = service.createTransacaoFormGroup(sampleWithRequiredData);

        const transacao = service.getTransacao(formGroup) as any;

        expect(transacao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransacao should not enable id FormControl', () => {
        const formGroup = service.createTransacaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransacao should disable id FormControl', () => {
        const formGroup = service.createTransacaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
