import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrcamentoComponent } from './list/orcamento.component';
import { OrcamentoDetailComponent } from './detail/orcamento-detail.component';
import { OrcamentoUpdateComponent } from './update/orcamento-update.component';
import OrcamentoResolve from './route/orcamento-routing-resolve.service';

const orcamentoRoute: Routes = [
  {
    path: '',
    component: OrcamentoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrcamentoDetailComponent,
    resolve: {
      orcamento: OrcamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrcamentoUpdateComponent,
    resolve: {
      orcamento: OrcamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrcamentoUpdateComponent,
    resolve: {
      orcamento: OrcamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orcamentoRoute;
