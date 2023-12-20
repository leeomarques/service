import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IOrcamento } from 'app/entities/service/orcamento/orcamento.model';
import { OrcamentoService } from 'app/entities/service/orcamento/service/orcamento.service';
import { ICategoria } from 'app/entities/service/categoria/categoria.model';
import { CategoriaService } from 'app/entities/service/categoria/service/categoria.service';
import { ITransacao } from '../transacao.model';
import { TransacaoService } from '../service/transacao.service';
import { TransacaoFormService } from './transacao-form.service';

import { TransacaoUpdateComponent } from './transacao-update.component';

describe('Transacao Management Update Component', () => {
  let comp: TransacaoUpdateComponent;
  let fixture: ComponentFixture<TransacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transacaoFormService: TransacaoFormService;
  let transacaoService: TransacaoService;
  let orcamentoService: OrcamentoService;
  let categoriaService: CategoriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TransacaoUpdateComponent],
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
      .overrideTemplate(TransacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transacaoFormService = TestBed.inject(TransacaoFormService);
    transacaoService = TestBed.inject(TransacaoService);
    orcamentoService = TestBed.inject(OrcamentoService);
    categoriaService = TestBed.inject(CategoriaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Orcamento query and add missing value', () => {
      const transacao: ITransacao = { id: 456 };
      const orcamento: IOrcamento = { id: 14328 };
      transacao.orcamento = orcamento;

      const orcamentoCollection: IOrcamento[] = [{ id: 2552 }];
      jest.spyOn(orcamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: orcamentoCollection })));
      const additionalOrcamentos = [orcamento];
      const expectedCollection: IOrcamento[] = [...additionalOrcamentos, ...orcamentoCollection];
      jest.spyOn(orcamentoService, 'addOrcamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transacao });
      comp.ngOnInit();

      expect(orcamentoService.query).toHaveBeenCalled();
      expect(orcamentoService.addOrcamentoToCollectionIfMissing).toHaveBeenCalledWith(
        orcamentoCollection,
        ...additionalOrcamentos.map(expect.objectContaining),
      );
      expect(comp.orcamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Categoria query and add missing value', () => {
      const transacao: ITransacao = { id: 456 };
      const categoria: ICategoria = { id: 4388 };
      transacao.categoria = categoria;

      const categoriaCollection: ICategoria[] = [{ id: 2618 }];
      jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
      const additionalCategorias = [categoria];
      const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
      jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transacao });
      comp.ngOnInit();

      expect(categoriaService.query).toHaveBeenCalled();
      expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaCollection,
        ...additionalCategorias.map(expect.objectContaining),
      );
      expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transacao: ITransacao = { id: 456 };
      const orcamento: IOrcamento = { id: 16755 };
      transacao.orcamento = orcamento;
      const categoria: ICategoria = { id: 25494 };
      transacao.categoria = categoria;

      activatedRoute.data = of({ transacao });
      comp.ngOnInit();

      expect(comp.orcamentosSharedCollection).toContain(orcamento);
      expect(comp.categoriasSharedCollection).toContain(categoria);
      expect(comp.transacao).toEqual(transacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransacao>>();
      const transacao = { id: 123 };
      jest.spyOn(transacaoFormService, 'getTransacao').mockReturnValue(transacao);
      jest.spyOn(transacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transacao }));
      saveSubject.complete();

      // THEN
      expect(transacaoFormService.getTransacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transacaoService.update).toHaveBeenCalledWith(expect.objectContaining(transacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransacao>>();
      const transacao = { id: 123 };
      jest.spyOn(transacaoFormService, 'getTransacao').mockReturnValue({ id: null });
      jest.spyOn(transacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transacao }));
      saveSubject.complete();

      // THEN
      expect(transacaoFormService.getTransacao).toHaveBeenCalled();
      expect(transacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransacao>>();
      const transacao = { id: 123 };
      jest.spyOn(transacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transacaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrcamento', () => {
      it('Should forward to orcamentoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(orcamentoService, 'compareOrcamento');
        comp.compareOrcamento(entity, entity2);
        expect(orcamentoService.compareOrcamento).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategoria', () => {
      it('Should forward to categoriaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoriaService, 'compareCategoria');
        comp.compareCategoria(entity, entity2);
        expect(categoriaService.compareCategoria).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
