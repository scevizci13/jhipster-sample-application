import dayjs from 'dayjs/esm';

import { IUrun, NewUrun } from './urun.model';

export const sampleWithRequiredData: IUrun = {
  id: 20240,
  isim: 'accountability',
  fiyat: 10763.16,
};

export const sampleWithPartialData: IUrun = {
  id: 20418,
  isim: 'unto',
  fiyat: 472.79,
  yenilemeUcreti: 5763.14,
  aciklama: 'pigpen concerning',
  aktifMi: true,
};

export const sampleWithFullData: IUrun = {
  id: 15371,
  isim: 'mothball',
  fiyat: 2262.89,
  lisansAnahtari: 'fray',
  yenilemeUcreti: 1807,
  yenilemeTarihi: dayjs('2024-10-30T09:49'),
  aciklama: 'warmly duh whoa',
  aktifMi: true,
};

export const sampleWithNewData: NewUrun = {
  isim: 'violent',
  fiyat: 28299.27,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
