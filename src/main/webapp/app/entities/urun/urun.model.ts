import dayjs from 'dayjs/esm';

export interface IUrun {
  id: number;
  isim?: string | null;
  fiyat?: number | null;
  lisansAnahtari?: string | null;
  yenilemeUcreti?: number | null;
  yenilemeTarihi?: dayjs.Dayjs | null;
  aciklama?: string | null;
  aktifMi?: boolean | null;
}

export type NewUrun = Omit<IUrun, 'id'> & { id: null };
