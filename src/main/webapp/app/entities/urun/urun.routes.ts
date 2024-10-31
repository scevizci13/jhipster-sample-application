import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UrunResolve from './route/urun-routing-resolve.service';

const urunRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/urun.component').then(m => m.UrunComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/urun-detail.component').then(m => m.UrunDetailComponent),
    resolve: {
      urun: UrunResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/urun-update.component').then(m => m.UrunUpdateComponent),
    resolve: {
      urun: UrunResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/urun-update.component').then(m => m.UrunUpdateComponent),
    resolve: {
      urun: UrunResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default urunRoute;
