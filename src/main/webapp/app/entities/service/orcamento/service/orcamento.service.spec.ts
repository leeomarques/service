import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrcamento } from '../orcamento.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../orcamento.test-samples';

import { OrcamentoService } from './orcamento.service';

const requireRestSample: IOrcamento = {
  ...sampleWithRequiredData,
};

describe('Orcamento Service', () => {
  let service: OrcamentoService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrcamento | IOrcamento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrcamentoService);
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

    it('should create a Orcamento', () => {
      const orcamento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orcamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Orcamento', () => {
      const orcamento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orcamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Orcamento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Orcamento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Orcamento', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrcamentoToCollectionIfMissing', () => {
      it('should add a Orcamento to an empty array', () => {
        const orcamento: IOrcamento = sampleWithRequiredData;
        expectedResult = service.addOrcamentoToCollectionIfMissing([], orcamento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orcamento);
      });

      it('should not add a Orcamento to an array that contains it', () => {
        const orcamento: IOrcamento = sampleWithRequiredData;
        const orcamentoCollection: IOrcamento[] = [
          {
            ...orcamento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrcamentoToCollectionIfMissing(orcamentoCollection, orcamento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Orcamento to an array that doesn't contain it", () => {
        const orcamento: IOrcamento = sampleWithRequiredData;
        const orcamentoCollection: IOrcamento[] = [sampleWithPartialData];
        expectedResult = service.addOrcamentoToCollectionIfMissing(orcamentoCollection, orcamento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orcamento);
      });

      it('should add only unique Orcamento to an array', () => {
        const orcamentoArray: IOrcamento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orcamentoCollection: IOrcamento[] = [sampleWithRequiredData];
        expectedResult = service.addOrcamentoToCollectionIfMissing(orcamentoCollection, ...orcamentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orcamento: IOrcamento = sampleWithRequiredData;
        const orcamento2: IOrcamento = sampleWithPartialData;
        expectedResult = service.addOrcamentoToCollectionIfMissing([], orcamento, orcamento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orcamento);
        expect(expectedResult).toContain(orcamento2);
      });

      it('should accept null and undefined values', () => {
        const orcamento: IOrcamento = sampleWithRequiredData;
        expectedResult = service.addOrcamentoToCollectionIfMissing([], null, orcamento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orcamento);
      });

      it('should return initial array if no Orcamento is added', () => {
        const orcamentoCollection: IOrcamento[] = [sampleWithRequiredData];
        expectedResult = service.addOrcamentoToCollectionIfMissing(orcamentoCollection, undefined, null);
        expect(expectedResult).toEqual(orcamentoCollection);
      });
    });

    describe('compareOrcamento', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrcamento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrcamento(entity1, entity2);
        const compareResult2 = service.compareOrcamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrcamento(entity1, entity2);
        const compareResult2 = service.compareOrcamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrcamento(entity1, entity2);
        const compareResult2 = service.compareOrcamento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
