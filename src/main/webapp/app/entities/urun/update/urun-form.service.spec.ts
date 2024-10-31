import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../urun.test-samples';

import { UrunFormService } from './urun-form.service';

describe('Urun Form Service', () => {
  let service: UrunFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UrunFormService);
  });

  describe('Service methods', () => {
    describe('createUrunFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUrunFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isim: expect.any(Object),
            fiyat: expect.any(Object),
            lisansAnahtari: expect.any(Object),
            yenilemeUcreti: expect.any(Object),
            yenilemeTarihi: expect.any(Object),
            aciklama: expect.any(Object),
            aktifMi: expect.any(Object),
          }),
        );
      });

      it('passing IUrun should create a new form with FormGroup', () => {
        const formGroup = service.createUrunFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isim: expect.any(Object),
            fiyat: expect.any(Object),
            lisansAnahtari: expect.any(Object),
            yenilemeUcreti: expect.any(Object),
            yenilemeTarihi: expect.any(Object),
            aciklama: expect.any(Object),
            aktifMi: expect.any(Object),
          }),
        );
      });
    });

    describe('getUrun', () => {
      it('should return NewUrun for default Urun initial value', () => {
        const formGroup = service.createUrunFormGroup(sampleWithNewData);

        const urun = service.getUrun(formGroup) as any;

        expect(urun).toMatchObject(sampleWithNewData);
      });

      it('should return NewUrun for empty Urun initial value', () => {
        const formGroup = service.createUrunFormGroup();

        const urun = service.getUrun(formGroup) as any;

        expect(urun).toMatchObject({});
      });

      it('should return IUrun', () => {
        const formGroup = service.createUrunFormGroup(sampleWithRequiredData);

        const urun = service.getUrun(formGroup) as any;

        expect(urun).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUrun should not enable id FormControl', () => {
        const formGroup = service.createUrunFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUrun should disable id FormControl', () => {
        const formGroup = service.createUrunFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
