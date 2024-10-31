import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MusteriResolve from './route/musteri-routing-resolve.service';

const musteriRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/musteri.component').then(m => m.MusteriComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/musteri-detail.component').then(m => m.MusteriDetailComponent),
    resolve: {
      musteri: MusteriResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/musteri-update.component').then(m => m.MusteriUpdateComponent),
    resolve: {
      musteri: MusteriResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/musteri-update.component').then(m => m.MusteriUpdateComponent),
    resolve: {
      musteri: MusteriResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default musteriRoute;
