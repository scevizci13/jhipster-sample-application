import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMusteri, NewMusteri } from '../musteri.model';

export type PartialUpdateMusteri = Partial<IMusteri> & Pick<IMusteri, 'id'>;

type RestOf<T extends IMusteri | NewMusteri> = Omit<T, 'kayitTarihi'> & {
  kayitTarihi?: string | null;
};

export type RestMusteri = RestOf<IMusteri>;

export type NewRestMusteri = RestOf<NewMusteri>;

export type PartialUpdateRestMusteri = RestOf<PartialUpdateMusteri>;

export type EntityResponseType = HttpResponse<IMusteri>;
export type EntityArrayResponseType = HttpResponse<IMusteri[]>;

@Injectable({ providedIn: 'root' })
export class MusteriService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/musteris');

  create(musteri: NewMusteri): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(musteri);
    return this.http
      .post<RestMusteri>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(musteri: IMusteri): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(musteri);
    return this.http
      .put<RestMusteri>(`${this.resourceUrl}/${this.getMusteriIdentifier(musteri)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(musteri: PartialUpdateMusteri): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(musteri);
    return this.http
      .patch<RestMusteri>(`${this.resourceUrl}/${this.getMusteriIdentifier(musteri)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMusteri>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMusteri[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMusteriIdentifier(musteri: Pick<IMusteri, 'id'>): number {
    return musteri.id;
  }

  compareMusteri(o1: Pick<IMusteri, 'id'> | null, o2: Pick<IMusteri, 'id'> | null): boolean {
    return o1 && o2 ? this.getMusteriIdentifier(o1) === this.getMusteriIdentifier(o2) : o1 === o2;
  }

  addMusteriToCollectionIfMissing<Type extends Pick<IMusteri, 'id'>>(
    musteriCollection: Type[],
    ...musterisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const musteris: Type[] = musterisToCheck.filter(isPresent);
    if (musteris.length > 0) {
      const musteriCollectionIdentifiers = musteriCollection.map(musteriItem => this.getMusteriIdentifier(musteriItem));
      const musterisToAdd = musteris.filter(musteriItem => {
        const musteriIdentifier = this.getMusteriIdentifier(musteriItem);
        if (musteriCollectionIdentifiers.includes(musteriIdentifier)) {
          return false;
        }
        musteriCollectionIdentifiers.push(musteriIdentifier);
        return true;
      });
      return [...musterisToAdd, ...musteriCollection];
    }
    return musteriCollection;
  }

  protected convertDateFromClient<T extends IMusteri | NewMusteri | PartialUpdateMusteri>(musteri: T): RestOf<T> {
    return {
      ...musteri,
      kayitTarihi: musteri.kayitTarihi?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMusteri: RestMusteri): IMusteri {
    return {
      ...restMusteri,
      kayitTarihi: restMusteri.kayitTarihi ? dayjs(restMusteri.kayitTarihi) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMusteri>): HttpResponse<IMusteri> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMusteri[]>): HttpResponse<IMusteri[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
