import dayjs from 'dayjs/esm';

export interface IMusteri {
  id: number;
  ad?: string | null;
  soyad?: string | null;
  email?: string | null;
  telefon?: string | null;
  adres?: string | null;
  kayitTarihi?: dayjs.Dayjs | null;
}

export type NewMusteri = Omit<IMusteri, 'id'> & { id: null };
