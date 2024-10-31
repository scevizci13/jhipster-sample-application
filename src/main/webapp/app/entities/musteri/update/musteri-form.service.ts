import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMusteri, NewMusteri } from '../musteri.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMusteri for edit and NewMusteriFormGroupInput for create.
 */
type MusteriFormGroupInput = IMusteri | PartialWithRequiredKeyOf<NewMusteri>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMusteri | NewMusteri> = Omit<T, 'kayitTarihi'> & {
  kayitTarihi?: string | null;
};

type MusteriFormRawValue = FormValueOf<IMusteri>;

type NewMusteriFormRawValue = FormValueOf<NewMusteri>;

type MusteriFormDefaults = Pick<NewMusteri, 'id' | 'kayitTarihi'>;

type MusteriFormGroupContent = {
  id: FormControl<MusteriFormRawValue['id'] | NewMusteri['id']>;
  ad: FormControl<MusteriFormRawValue['ad']>;
  soyad: FormControl<MusteriFormRawValue['soyad']>;
  email: FormControl<MusteriFormRawValue['email']>;
  telefon: FormControl<MusteriFormRawValue['telefon']>;
  adres: FormControl<MusteriFormRawValue['adres']>;
  kayitTarihi: FormControl<MusteriFormRawValue['kayitTarihi']>;
};

export type MusteriFormGroup = FormGroup<MusteriFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MusteriFormService {
  createMusteriFormGroup(musteri: MusteriFormGroupInput = { id: null }): MusteriFormGroup {
    const musteriRawValue = this.convertMusteriToMusteriRawValue({
      ...this.getFormDefaults(),
      ...musteri,
    });
    return new FormGroup<MusteriFormGroupContent>({
      id: new FormControl(
        { value: musteriRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ad: new FormControl(musteriRawValue.ad, {
        validators: [Validators.required],
      }),
      soyad: new FormControl(musteriRawValue.soyad),
      email: new FormControl(musteriRawValue.email, {
        validators: [Validators.required],
      }),
      telefon: new FormControl(musteriRawValue.telefon),
      adres: new FormControl(musteriRawValue.adres),
      kayitTarihi: new FormControl(musteriRawValue.kayitTarihi),
    });
  }

  getMusteri(form: MusteriFormGroup): IMusteri | NewMusteri {
    return this.convertMusteriRawValueToMusteri(form.getRawValue() as MusteriFormRawValue | NewMusteriFormRawValue);
  }

  resetForm(form: MusteriFormGroup, musteri: MusteriFormGroupInput): void {
    const musteriRawValue = this.convertMusteriToMusteriRawValue({ ...this.getFormDefaults(), ...musteri });
    form.reset(
      {
        ...musteriRawValue,
        id: { value: musteriRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MusteriFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      kayitTarihi: currentTime,
    };
  }

  private convertMusteriRawValueToMusteri(rawMusteri: MusteriFormRawValue | NewMusteriFormRawValue): IMusteri | NewMusteri {
    return {
      ...rawMusteri,
      kayitTarihi: dayjs(rawMusteri.kayitTarihi, DATE_TIME_FORMAT),
    };
  }

  private convertMusteriToMusteriRawValue(
    musteri: IMusteri | (Partial<NewMusteri> & MusteriFormDefaults),
  ): MusteriFormRawValue | PartialWithRequiredKeyOf<NewMusteriFormRawValue> {
    return {
      ...musteri,
      kayitTarihi: musteri.kayitTarihi ? musteri.kayitTarihi.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
