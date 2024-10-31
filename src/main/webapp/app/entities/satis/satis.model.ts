import dayjs from 'dayjs/esm';
import { IMusteri } from 'app/entities/musteri/musteri.model';
import { IUrun } from 'app/entities/urun/urun.model';

export interface ISatis {
  id: number;
  satisTarihi?: dayjs.Dayjs | null;
  miktar?: number | null;
  toplamFiyat?: number | null;
  odemeDurumu?: string | null;
  teslimatAdresi?: string | null;
  siparisNotu?: string | null;
  musteri?: IMusteri | null;
  urun?: IUrun | null;
}

export type NewSatis = Omit<ISatis, 'id'> & { id: null };
