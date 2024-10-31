import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISatis, NewSatis } from '../satis.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISatis for edit and NewSatisFormGroupInput for create.
 */
type SatisFormGroupInput = ISatis | PartialWithRequiredKeyOf<NewSatis>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISatis | NewSatis> = Omit<T, 'satisTarihi'> & {
  satisTarihi?: string | null;
};

type SatisFormRawValue = FormValueOf<ISatis>;

type NewSatisFormRawValue = FormValueOf<NewSatis>;

type SatisFormDefaults = Pick<NewSatis, 'id' | 'satisTarihi'>;

type SatisFormGroupContent = {
  id: FormControl<SatisFormRawValue['id'] | NewSatis['id']>;
  satisTarihi: FormControl<SatisFormRawValue['satisTarihi']>;
  miktar: FormControl<SatisFormRawValue['miktar']>;
  toplamFiyat: FormControl<SatisFormRawValue['toplamFiyat']>;
  odemeDurumu: FormControl<SatisFormRawValue['odemeDurumu']>;
  teslimatAdresi: FormControl<SatisFormRawValue['teslimatAdresi']>;
  siparisNotu: FormControl<SatisFormRawValue['siparisNotu']>;
  musteri: FormControl<SatisFormRawValue['musteri']>;
  urun: FormControl<SatisFormRawValue['urun']>;
};

export type SatisFormGroup = FormGroup<SatisFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SatisFormService {
  createSatisFormGroup(satis: SatisFormGroupInput = { id: null }): SatisFormGroup {
    const satisRawValue = this.convertSatisToSatisRawValue({
      ...this.getFormDefaults(),
      ...satis,
    });
    return new FormGroup<SatisFormGroupContent>({
      id: new FormControl(
        { value: satisRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      satisTarihi: new FormControl(satisRawValue.satisTarihi, {
        validators: [Validators.required],
      }),
      miktar: new FormControl(satisRawValue.miktar, {
        validators: [Validators.required],
      }),
      toplamFiyat: new FormControl(satisRawValue.toplamFiyat, {
        validators: [Validators.required],
      }),
      odemeDurumu: new FormControl(satisRawValue.odemeDurumu),
      teslimatAdresi: new FormControl(satisRawValue.teslimatAdresi),
      siparisNotu: new FormControl(satisRawValue.siparisNotu),
      musteri: new FormControl(satisRawValue.musteri),
      urun: new FormControl(satisRawValue.urun),
    });
  }

  getSatis(form: SatisFormGroup): ISatis | NewSatis {
    return this.convertSatisRawValueToSatis(form.getRawValue() as SatisFormRawValue | NewSatisFormRawValue);
  }

  resetForm(form: SatisFormGroup, satis: SatisFormGroupInput): void {
    const satisRawValue = this.convertSatisToSatisRawValue({ ...this.getFormDefaults(), ...satis });
    form.reset(
      {
        ...satisRawValue,
        id: { value: satisRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SatisFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      satisTarihi: currentTime,
    };
  }

  private convertSatisRawValueToSatis(rawSatis: SatisFormRawValue | NewSatisFormRawValue): ISatis | NewSatis {
    return {
      ...rawSatis,
      satisTarihi: dayjs(rawSatis.satisTarihi, DATE_TIME_FORMAT),
    };
  }

  private convertSatisToSatisRawValue(
    satis: ISatis | (Partial<NewSatis> & SatisFormDefaults),
  ): SatisFormRawValue | PartialWithRequiredKeyOf<NewSatisFormRawValue> {
    return {
      ...satis,
      satisTarihi: satis.satisTarihi ? satis.satisTarihi.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
