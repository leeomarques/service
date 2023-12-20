import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrcamentoService } from '../service/orcamento.service';
import { IOrcamento } from '../orcamento.model';
import { OrcamentoFormService } from './orcamento-form.service';

import { OrcamentoUpdateComponent } from './orcamento-update.component';

describe('Orcamento Management Update Component', () => {
  let comp: OrcamentoUpdateComponent;
  let fixture: ComponentFixture<OrcamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orcamentoFormService: OrcamentoFormService;
  let orcamentoService: OrcamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrcamentoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrcamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrcamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orcamentoFormService = TestBed.inject(OrcamentoFormService);
    orcamentoService = TestBed.inject(OrcamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orcamento: IOrcamento = { id: 456 };

      activatedRoute.data = of({ orcamento });
      comp.ngOnInit();

      expect(comp.orcamento).toEqual(orcamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrcamento>>();
      const orcamento = { id: 123 };
      jest.spyOn(orcamentoFormService, 'getOrcamento').mockReturnValue(orcamento);
      jest.spyOn(orcamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orcamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orcamento }));
      saveSubject.complete();

      // THEN
      expect(orcamentoFormService.getOrcamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orcamentoService.update).toHaveBeenCalledWith(expect.objectContaining(orcamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrcamento>>();
      const orcamento = { id: 123 };
      jest.spyOn(orcamentoFormService, 'getOrcamento').mockReturnValue({ id: null });
      jest.spyOn(orcamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orcamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orcamento }));
      saveSubject.complete();

      // THEN
      expect(orcamentoFormService.getOrcamento).toHaveBeenCalled();
      expect(orcamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrcamento>>();
      const orcamento = { id: 123 };
      jest.spyOn(orcamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orcamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orcamentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
