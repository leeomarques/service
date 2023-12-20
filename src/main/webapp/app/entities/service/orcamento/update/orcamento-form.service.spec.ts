import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../orcamento.test-samples';

import { OrcamentoFormService } from './orcamento-form.service';

describe('Orcamento Form Service', () => {
  let service: OrcamentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrcamentoFormService);
  });

  describe('Service methods', () => {
    describe('createOrcamentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrcamentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ano: expect.any(Object),
            mes: expect.any(Object),
          }),
        );
      });

      it('passing IOrcamento should create a new form with FormGroup', () => {
        const formGroup = service.createOrcamentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ano: expect.any(Object),
            mes: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrcamento', () => {
      it('should return NewOrcamento for default Orcamento initial value', () => {
        const formGroup = service.createOrcamentoFormGroup(sampleWithNewData);

        const orcamento = service.getOrcamento(formGroup) as any;

        expect(orcamento).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrcamento for empty Orcamento initial value', () => {
        const formGroup = service.createOrcamentoFormGroup();

        const orcamento = service.getOrcamento(formGroup) as any;

        expect(orcamento).toMatchObject({});
      });

      it('should return IOrcamento', () => {
        const formGroup = service.createOrcamentoFormGroup(sampleWithRequiredData);

        const orcamento = service.getOrcamento(formGroup) as any;

        expect(orcamento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrcamento should not enable id FormControl', () => {
        const formGroup = service.createOrcamentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrcamento should disable id FormControl', () => {
        const formGroup = service.createOrcamentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
