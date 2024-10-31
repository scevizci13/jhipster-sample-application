import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../musteri.test-samples';

import { MusteriFormService } from './musteri-form.service';

describe('Musteri Form Service', () => {
  let service: MusteriFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MusteriFormService);
  });

  describe('Service methods', () => {
    describe('createMusteriFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMusteriFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ad: expect.any(Object),
            soyad: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
            adres: expect.any(Object),
            kayitTarihi: expect.any(Object),
          }),
        );
      });

      it('passing IMusteri should create a new form with FormGroup', () => {
        const formGroup = service.createMusteriFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ad: expect.any(Object),
            soyad: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
            adres: expect.any(Object),
            kayitTarihi: expect.any(Object),
          }),
        );
      });
    });

    describe('getMusteri', () => {
      it('should return NewMusteri for default Musteri initial value', () => {
        const formGroup = service.createMusteriFormGroup(sampleWithNewData);

        const musteri = service.getMusteri(formGroup) as any;

        expect(musteri).toMatchObject(sampleWithNewData);
      });

      it('should return NewMusteri for empty Musteri initial value', () => {
        const formGroup = service.createMusteriFormGroup();

        const musteri = service.getMusteri(formGroup) as any;

        expect(musteri).toMatchObject({});
      });

      it('should return IMusteri', () => {
        const formGroup = service.createMusteriFormGroup(sampleWithRequiredData);

        const musteri = service.getMusteri(formGroup) as any;

        expect(musteri).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMusteri should not enable id FormControl', () => {
        const formGroup = service.createMusteriFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMusteri should disable id FormControl', () => {
        const formGroup = service.createMusteriFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
