import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SatisResolve from './route/satis-routing-resolve.service';

const satisRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/satis.component').then(m => m.SatisComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/satis-detail.component').then(m => m.SatisDetailComponent),
    resolve: {
      satis: SatisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/satis-update.component').then(m => m.SatisUpdateComponent),
    resolve: {
      satis: SatisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/satis-update.component').then(m => m.SatisUpdateComponent),
    resolve: {
      satis: SatisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default satisRoute;
