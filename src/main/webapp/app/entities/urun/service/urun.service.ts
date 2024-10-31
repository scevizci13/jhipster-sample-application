import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUrun, NewUrun } from '../urun.model';

export type PartialUpdateUrun = Partial<IUrun> & Pick<IUrun, 'id'>;

type RestOf<T extends IUrun | NewUrun> = Omit<T, 'yenilemeTarihi'> & {
  yenilemeTarihi?: string | null;
};

export type RestUrun = RestOf<IUrun>;

export type NewRestUrun = RestOf<NewUrun>;

export type PartialUpdateRestUrun = RestOf<PartialUpdateUrun>;

export type EntityResponseType = HttpResponse<IUrun>;
export type EntityArrayResponseType = HttpResponse<IUrun[]>;

@Injectable({ providedIn: 'root' })
export class UrunService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/uruns');

  create(urun: NewUrun): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(urun);
    return this.http.post<RestUrun>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(urun: IUrun): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(urun);
    return this.http
      .put<RestUrun>(`${this.resourceUrl}/${this.getUrunIdentifier(urun)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(urun: PartialUpdateUrun): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(urun);
    return this.http
      .patch<RestUrun>(`${this.resourceUrl}/${this.getUrunIdentifier(urun)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUrun>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUrun[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUrunIdentifier(urun: Pick<IUrun, 'id'>): number {
    return urun.id;
  }

  compareUrun(o1: Pick<IUrun, 'id'> | null, o2: Pick<IUrun, 'id'> | null): boolean {
    return o1 && o2 ? this.getUrunIdentifier(o1) === this.getUrunIdentifier(o2) : o1 === o2;
  }

  addUrunToCollectionIfMissing<Type extends Pick<IUrun, 'id'>>(
    urunCollection: Type[],
    ...urunsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const uruns: Type[] = urunsToCheck.filter(isPresent);
    if (uruns.length > 0) {
      const urunCollectionIdentifiers = urunCollection.map(urunItem => this.getUrunIdentifier(urunItem));
      const urunsToAdd = uruns.filter(urunItem => {
        const urunIdentifier = this.getUrunIdentifier(urunItem);
        if (urunCollectionIdentifiers.includes(urunIdentifier)) {
          return false;
        }
        urunCollectionIdentifiers.push(urunIdentifier);
        return true;
      });
      return [...urunsToAdd, ...urunCollection];
    }
    return urunCollection;
  }

  protected convertDateFromClient<T extends IUrun | NewUrun | PartialUpdateUrun>(urun: T): RestOf<T> {
    return {
      ...urun,
      yenilemeTarihi: urun.yenilemeTarihi?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUrun: RestUrun): IUrun {
    return {
      ...restUrun,
      yenilemeTarihi: restUrun.yenilemeTarihi ? dayjs(restUrun.yenilemeTarihi) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUrun>): HttpResponse<IUrun> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUrun[]>): HttpResponse<IUrun[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
