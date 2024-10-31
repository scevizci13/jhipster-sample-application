import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUrun } from '../urun.model';
import { UrunService } from '../service/urun.service';

const urunResolve = (route: ActivatedRouteSnapshot): Observable<null | IUrun> => {
  const id = route.params.id;
  if (id) {
    return inject(UrunService)
      .find(id)
      .pipe(
        mergeMap((urun: HttpResponse<IUrun>) => {
          if (urun.body) {
            return of(urun.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default urunResolve;
