import dayjs from 'dayjs/esm';

import { ISatis, NewSatis } from './satis.model';

export const sampleWithRequiredData: ISatis = {
  id: 1280,
  satisTarihi: dayjs('2024-10-30T10:18'),
  miktar: 32412,
  toplamFiyat: 16543.17,
};

export const sampleWithPartialData: ISatis = {
  id: 4268,
  satisTarihi: dayjs('2024-10-30T16:04'),
  miktar: 3725,
  toplamFiyat: 8679.75,
  odemeDurumu: 'annually',
};

export const sampleWithFullData: ISatis = {
  id: 25611,
  satisTarihi: dayjs('2024-10-30T18:44'),
  miktar: 14278,
  toplamFiyat: 3261.45,
  odemeDurumu: 'safe',
  teslimatAdresi: 'lazily after grandiose',
  siparisNotu: 'upliftingly',
};

export const sampleWithNewData: NewSatis = {
  satisTarihi: dayjs('2024-10-30T16:34'),
  miktar: 20906,
  toplamFiyat: 12467.92,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
