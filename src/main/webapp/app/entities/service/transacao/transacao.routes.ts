import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TransacaoComponent } from './list/transacao.component';
import { TransacaoDetailComponent } from './detail/transacao-detail.component';
import { TransacaoUpdateComponent } from './update/transacao-update.component';
import TransacaoResolve from './route/transacao-routing-resolve.service';

const transacaoRoute: Routes = [
  {
    path: '',
    component: TransacaoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransacaoDetailComponent,
    resolve: {
      transacao: TransacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransacaoUpdateComponent,
    resolve: {
      transacao: TransacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransacaoUpdateComponent,
    resolve: {
      transacao: TransacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transacaoRoute;
