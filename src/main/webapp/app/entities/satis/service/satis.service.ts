import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISatis, NewSatis } from '../satis.model';

export type PartialUpdateSatis = Partial<ISatis> & Pick<ISatis, 'id'>;

type RestOf<T extends ISatis | NewSatis> = Omit<T, 'satisTarihi'> & {
  satisTarihi?: string | null;
};

export type RestSatis = RestOf<ISatis>;

export type NewRestSatis = RestOf<NewSatis>;

export type PartialUpdateRestSatis = RestOf<PartialUpdateSatis>;

export type EntityResponseType = HttpResponse<ISatis>;
export type EntityArrayResponseType = HttpResponse<ISatis[]>;

@Injectable({ providedIn: 'root' })
export class SatisService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/satis');

  create(satis: NewSatis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(satis);
    return this.http.post<RestSatis>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(satis: ISatis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(satis);
    return this.http
      .put<RestSatis>(`${this.resourceUrl}/${this.getSatisIdentifier(satis)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(satis: PartialUpdateSatis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(satis);
    return this.http
      .patch<RestSatis>(`${this.resourceUrl}/${this.getSatisIdentifier(satis)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSatis>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSatis[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSatisIdentifier(satis: Pick<ISatis, 'id'>): number {
    return satis.id;
  }

  compareSatis(o1: Pick<ISatis, 'id'> | null, o2: Pick<ISatis, 'id'> | null): boolean {
    return o1 && o2 ? this.getSatisIdentifier(o1) === this.getSatisIdentifier(o2) : o1 === o2;
  }

  addSatisToCollectionIfMissing<Type extends Pick<ISatis, 'id'>>(
    satisCollection: Type[],
    ...satisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const satis: Type[] = satisToCheck.filter(isPresent);
    if (satis.length > 0) {
      const satisCollectionIdentifiers = satisCollection.map(satisItem => this.getSatisIdentifier(satisItem));
      const satisToAdd = satis.filter(satisItem => {
        const satisIdentifier = this.getSatisIdentifier(satisItem);
        if (satisCollectionIdentifiers.includes(satisIdentifier)) {
          return false;
        }
        satisCollectionIdentifiers.push(satisIdentifier);
        return true;
      });
      return [...satisToAdd, ...satisCollection];
    }
    return satisCollection;
  }

  protected convertDateFromClient<T extends ISatis | NewSatis | PartialUpdateSatis>(satis: T): RestOf<T> {
    return {
      ...satis,
      satisTarihi: satis.satisTarihi?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSatis: RestSatis): ISatis {
    return {
      ...restSatis,
      satisTarihi: restSatis.satisTarihi ? dayjs(restSatis.satisTarihi) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSatis>): HttpResponse<ISatis> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSatis[]>): HttpResponse<ISatis[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
