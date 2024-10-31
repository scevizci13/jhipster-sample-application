import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISatis } from '../satis.model';
import { SatisService } from '../service/satis.service';

const satisResolve = (route: ActivatedRouteSnapshot): Observable<null | ISatis> => {
  const id = route.params.id;
  if (id) {
    return inject(SatisService)
      .find(id)
      .pipe(
        mergeMap((satis: HttpResponse<ISatis>) => {
          if (satis.body) {
            return of(satis.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default satisResolve;
