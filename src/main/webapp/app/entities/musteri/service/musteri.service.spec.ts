import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMusteri } from '../musteri.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../musteri.test-samples';

import { MusteriService, RestMusteri } from './musteri.service';

const requireRestSample: RestMusteri = {
  ...sampleWithRequiredData,
  kayitTarihi: sampleWithRequiredData.kayitTarihi?.toJSON(),
};

describe('Musteri Service', () => {
  let service: MusteriService;
  let httpMock: HttpTestingController;
  let expectedResult: IMusteri | IMusteri[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MusteriService);
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

    it('should create a Musteri', () => {
      const musteri = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(musteri).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Musteri', () => {
      const musteri = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(musteri).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Musteri', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Musteri', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Musteri', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMusteriToCollectionIfMissing', () => {
      it('should add a Musteri to an empty array', () => {
        const musteri: IMusteri = sampleWithRequiredData;
        expectedResult = service.addMusteriToCollectionIfMissing([], musteri);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musteri);
      });

      it('should not add a Musteri to an array that contains it', () => {
        const musteri: IMusteri = sampleWithRequiredData;
        const musteriCollection: IMusteri[] = [
          {
            ...musteri,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMusteriToCollectionIfMissing(musteriCollection, musteri);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Musteri to an array that doesn't contain it", () => {
        const musteri: IMusteri = sampleWithRequiredData;
        const musteriCollection: IMusteri[] = [sampleWithPartialData];
        expectedResult = service.addMusteriToCollectionIfMissing(musteriCollection, musteri);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musteri);
      });

      it('should add only unique Musteri to an array', () => {
        const musteriArray: IMusteri[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const musteriCollection: IMusteri[] = [sampleWithRequiredData];
        expectedResult = service.addMusteriToCollectionIfMissing(musteriCollection, ...musteriArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const musteri: IMusteri = sampleWithRequiredData;
        const musteri2: IMusteri = sampleWithPartialData;
        expectedResult = service.addMusteriToCollectionIfMissing([], musteri, musteri2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musteri);
        expect(expectedResult).toContain(musteri2);
      });

      it('should accept null and undefined values', () => {
        const musteri: IMusteri = sampleWithRequiredData;
        expectedResult = service.addMusteriToCollectionIfMissing([], null, musteri, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musteri);
      });

      it('should return initial array if no Musteri is added', () => {
        const musteriCollection: IMusteri[] = [sampleWithRequiredData];
        expectedResult = service.addMusteriToCollectionIfMissing(musteriCollection, undefined, null);
        expect(expectedResult).toEqual(musteriCollection);
      });
    });

    describe('compareMusteri', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMusteri(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMusteri(entity1, entity2);
        const compareResult2 = service.compareMusteri(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMusteri(entity1, entity2);
        const compareResult2 = service.compareMusteri(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMusteri(entity1, entity2);
        const compareResult2 = service.compareMusteri(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
