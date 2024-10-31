import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../satis.test-samples';

import { SatisFormService } from './satis-form.service';

describe('Satis Form Service', () => {
  let service: SatisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SatisFormService);
  });

  describe('Service methods', () => {
    describe('createSatisFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSatisFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            satisTarihi: expect.any(Object),
            miktar: expect.any(Object),
            toplamFiyat: expect.any(Object),
            odemeDurumu: expect.any(Object),
            teslimatAdresi: expect.any(Object),
            siparisNotu: expect.any(Object),
            musteri: expect.any(Object),
            urun: expect.any(Object),
          }),
        );
      });

      it('passing ISatis should create a new form with FormGroup', () => {
        const formGroup = service.createSatisFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            satisTarihi: expect.any(Object),
            miktar: expect.any(Object),
            toplamFiyat: expect.any(Object),
            odemeDurumu: expect.any(Object),
            teslimatAdresi: expect.any(Object),
            siparisNotu: expect.any(Object),
            musteri: expect.any(Object),
            urun: expect.any(Object),
          }),
        );
      });
    });

    describe('getSatis', () => {
      it('should return NewSatis for default Satis initial value', () => {
        const formGroup = service.createSatisFormGroup(sampleWithNewData);

        const satis = service.getSatis(formGroup) as any;

        expect(satis).toMatchObject(sampleWithNewData);
      });

      it('should return NewSatis for empty Satis initial value', () => {
        const formGroup = service.createSatisFormGroup();

        const satis = service.getSatis(formGroup) as any;

        expect(satis).toMatchObject({});
      });

      it('should return ISatis', () => {
        const formGroup = service.createSatisFormGroup(sampleWithRequiredData);

        const satis = service.getSatis(formGroup) as any;

        expect(satis).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISatis should not enable id FormControl', () => {
        const formGroup = service.createSatisFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSatis should disable id FormControl', () => {
        const formGroup = service.createSatisFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
