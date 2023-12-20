import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITransacao } from '../transacao.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../transacao.test-samples';

import { TransacaoService } from './transacao.service';

const requireRestSample: ITransacao = {
  ...sampleWithRequiredData,
};

describe('Transacao Service', () => {
  let service: TransacaoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransacao | ITransacao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransacaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Transacao', () => {
      const transacao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Transacao', () => {
      const transacao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Transacao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Transacao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Transacao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTransacaoToCollectionIfMissing', () => {
      it('should add a Transacao to an empty array', () => {
        const transacao: ITransacao = sampleWithRequiredData;
        expectedResult = service.addTransacaoToCollectionIfMissing([], transacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transacao);
      });

      it('should not add a Transacao to an array that contains it', () => {
        const transacao: ITransacao = sampleWithRequiredData;
        const transacaoCollection: ITransacao[] = [
          {
            ...transacao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransacaoToCollectionIfMissing(transacaoCollection, transacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Transacao to an array that doesn't contain it", () => {
        const transacao: ITransacao = sampleWithRequiredData;
        const transacaoCollection: ITransacao[] = [sampleWithPartialData];
        expectedResult = service.addTransacaoToCollectionIfMissing(transacaoCollection, transacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transacao);
      });

      it('should add only unique Transacao to an array', () => {
        const transacaoArray: ITransacao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transacaoCollection: ITransacao[] = [sampleWithRequiredData];
        expectedResult = service.addTransacaoToCollectionIfMissing(transacaoCollection, ...transacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transacao: ITransacao = sampleWithRequiredData;
        const transacao2: ITransacao = sampleWithPartialData;
        expectedResult = service.addTransacaoToCollectionIfMissing([], transacao, transacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transacao);
        expect(expectedResult).toContain(transacao2);
      });

      it('should accept null and undefined values', () => {
        const transacao: ITransacao = sampleWithRequiredData;
        expectedResult = service.addTransacaoToCollectionIfMissing([], null, transacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transacao);
      });

      it('should return initial array if no Transacao is added', () => {
        const transacaoCollection: ITransacao[] = [sampleWithRequiredData];
        expectedResult = service.addTransacaoToCollectionIfMissing(transacaoCollection, undefined, null);
        expect(expectedResult).toEqual(transacaoCollection);
      });
    });

    describe('compareTransacao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransacao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTransacao(entity1, entity2);
        const compareResult2 = service.compareTransacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTransacao(entity1, entity2);
        const compareResult2 = service.compareTransacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTransacao(entity1, entity2);
        const compareResult2 = service.compareTransacao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
