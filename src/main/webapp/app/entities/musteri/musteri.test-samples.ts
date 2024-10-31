import dayjs from 'dayjs/esm';

import { IMusteri, NewMusteri } from './musteri.model';

export const sampleWithRequiredData: IMusteri = {
  id: 5844,
  ad: 'lavish',
  email: 'Simeon_Dare7@yahoo.com',
};

export const sampleWithPartialData: IMusteri = {
  id: 20136,
  ad: 'whup er',
  email: 'Ian56@yahoo.com',
  telefon: 'editor',
  adres: 'strait inveigle',
  kayitTarihi: dayjs('2024-10-30T21:59'),
};

export const sampleWithFullData: IMusteri = {
  id: 15584,
  ad: 'formation painfully',
  soyad: 'whoa',
  email: 'Noel.Barrows@yahoo.com',
  telefon: 'because',
  adres: 'excellent',
  kayitTarihi: dayjs('2024-10-30T20:06'),
};

export const sampleWithNewData: NewMusteri = {
  ad: 'verbally any',
  email: 'Loraine.Terry28@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
