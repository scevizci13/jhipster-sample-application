import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'jhipsterSampleApplicationApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'musteri',
    data: { pageTitle: 'jhipsterSampleApplicationApp.musteri.home.title' },
    loadChildren: () => import('./musteri/musteri.routes'),
  },
  {
    path: 'urun',
    data: { pageTitle: 'jhipsterSampleApplicationApp.urun.home.title' },
    loadChildren: () => import('./urun/urun.routes'),
  },
  {
    path: 'satis',
    data: { pageTitle: 'jhipsterSampleApplicationApp.satis.home.title' },
    loadChildren: () => import('./satis/satis.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
