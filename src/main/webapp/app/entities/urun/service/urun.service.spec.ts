import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUrun } from '../urun.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../urun.test-samples';

import { RestUrun, UrunService } from './urun.service';

const requireRestSample: RestUrun = {
  ...sampleWithRequiredData,
  yenilemeTarihi: sampleWithRequiredData.yenilemeTarihi?.toJSON(),
};

describe('Urun Service', () => {
  let service: UrunService;
  let httpMock: HttpTestingController;
  let expectedResult: IUrun | IUrun[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UrunService);
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

    it('should create a Urun', () => {
      const urun = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(urun).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Urun', () => {
      const urun = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(urun).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Urun', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Urun', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Urun', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUrunToCollectionIfMissing', () => {
      it('should add a Urun to an empty array', () => {
        const urun: IUrun = sampleWithRequiredData;
        expectedResult = service.addUrunToCollectionIfMissing([], urun);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(urun);
      });

      it('should not add a Urun to an array that contains it', () => {
        const urun: IUrun = sampleWithRequiredData;
        const urunCollection: IUrun[] = [
          {
            ...urun,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUrunToCollectionIfMissing(urunCollection, urun);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Urun to an array that doesn't contain it", () => {
        const urun: IUrun = sampleWithRequiredData;
        const urunCollection: IUrun[] = [sampleWithPartialData];
        expectedResult = service.addUrunToCollectionIfMissing(urunCollection, urun);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(urun);
      });

      it('should add only unique Urun to an array', () => {
        const urunArray: IUrun[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const urunCollection: IUrun[] = [sampleWithRequiredData];
        expectedResult = service.addUrunToCollectionIfMissing(urunCollection, ...urunArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const urun: IUrun = sampleWithRequiredData;
        const urun2: IUrun = sampleWithPartialData;
        expectedResult = service.addUrunToCollectionIfMissing([], urun, urun2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(urun);
        expect(expectedResult).toContain(urun2);
      });

      it('should accept null and undefined values', () => {
        const urun: IUrun = sampleWithRequiredData;
        expectedResult = service.addUrunToCollectionIfMissing([], null, urun, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(urun);
      });

      it('should return initial array if no Urun is added', () => {
        const urunCollection: IUrun[] = [sampleWithRequiredData];
        expectedResult = service.addUrunToCollectionIfMissing(urunCollection, undefined, null);
        expect(expectedResult).toEqual(urunCollection);
      });
    });

    describe('compareUrun', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUrun(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUrun(entity1, entity2);
        const compareResult2 = service.compareUrun(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUrun(entity1, entity2);
        const compareResult2 = service.compareUrun(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUrun(entity1, entity2);
        const compareResult2 = service.compareUrun(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
