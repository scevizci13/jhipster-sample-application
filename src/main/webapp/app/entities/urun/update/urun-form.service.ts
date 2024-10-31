import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUrun, NewUrun } from '../urun.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUrun for edit and NewUrunFormGroupInput for create.
 */
type UrunFormGroupInput = IUrun | PartialWithRequiredKeyOf<NewUrun>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUrun | NewUrun> = Omit<T, 'yenilemeTarihi'> & {
  yenilemeTarihi?: string | null;
};

type UrunFormRawValue = FormValueOf<IUrun>;

type NewUrunFormRawValue = FormValueOf<NewUrun>;

type UrunFormDefaults = Pick<NewUrun, 'id' | 'yenilemeTarihi' | 'aktifMi'>;

type UrunFormGroupContent = {
  id: FormControl<UrunFormRawValue['id'] | NewUrun['id']>;
  isim: FormControl<UrunFormRawValue['isim']>;
  fiyat: FormControl<UrunFormRawValue['fiyat']>;
  lisansAnahtari: FormControl<UrunFormRawValue['lisansAnahtari']>;
  yenilemeUcreti: FormControl<UrunFormRawValue['yenilemeUcreti']>;
  yenilemeTarihi: FormControl<UrunFormRawValue['yenilemeTarihi']>;
  aciklama: FormControl<UrunFormRawValue['aciklama']>;
  aktifMi: FormControl<UrunFormRawValue['aktifMi']>;
};

export type UrunFormGroup = FormGroup<UrunFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UrunFormService {
  createUrunFormGroup(urun: UrunFormGroupInput = { id: null }): UrunFormGroup {
    const urunRawValue = this.convertUrunToUrunRawValue({
      ...this.getFormDefaults(),
      ...urun,
    });
    return new FormGroup<UrunFormGroupContent>({
      id: new FormControl(
        { value: urunRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      isim: new FormControl(urunRawValue.isim, {
        validators: [Validators.required],
      }),
      fiyat: new FormControl(urunRawValue.fiyat, {
        validators: [Validators.required],
      }),
      lisansAnahtari: new FormControl(urunRawValue.lisansAnahtari),
      yenilemeUcreti: new FormControl(urunRawValue.yenilemeUcreti),
      yenilemeTarihi: new FormControl(urunRawValue.yenilemeTarihi),
      aciklama: new FormControl(urunRawValue.aciklama),
      aktifMi: new FormControl(urunRawValue.aktifMi),
    });
  }

  getUrun(form: UrunFormGroup): IUrun | NewUrun {
    return this.convertUrunRawValueToUrun(form.getRawValue() as UrunFormRawValue | NewUrunFormRawValue);
  }

  resetForm(form: UrunFormGroup, urun: UrunFormGroupInput): void {
    const urunRawValue = this.convertUrunToUrunRawValue({ ...this.getFormDefaults(), ...urun });
    form.reset(
      {
        ...urunRawValue,
        id: { value: urunRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UrunFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      yenilemeTarihi: currentTime,
      aktifMi: false,
    };
  }

  private convertUrunRawValueToUrun(rawUrun: UrunFormRawValue | NewUrunFormRawValue): IUrun | NewUrun {
    return {
      ...rawUrun,
      yenilemeTarihi: dayjs(rawUrun.yenilemeTarihi, DATE_TIME_FORMAT),
    };
  }

  private convertUrunToUrunRawValue(
    urun: IUrun | (Partial<NewUrun> & UrunFormDefaults),
  ): UrunFormRawValue | PartialWithRequiredKeyOf<NewUrunFormRawValue> {
    return {
      ...urun,
      yenilemeTarihi: urun.yenilemeTarihi ? urun.yenilemeTarihi.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
