import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMusteri } from '../musteri.model';
import { MusteriService } from '../service/musteri.service';

const musteriResolve = (route: ActivatedRouteSnapshot): Observable<null | IMusteri> => {
  const id = route.params.id;
  if (id) {
    return inject(MusteriService)
      .find(id)
      .pipe(
        mergeMap((musteri: HttpResponse<IMusteri>) => {
          if (musteri.body) {
            return of(musteri.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default musteriResolve;
