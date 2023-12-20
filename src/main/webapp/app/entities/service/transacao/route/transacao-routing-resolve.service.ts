import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransacao } from '../transacao.model';
import { TransacaoService } from '../service/transacao.service';

export const transacaoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransacao> => {
  const id = route.params['id'];
  if (id) {
    return inject(TransacaoService)
      .find(id)
      .pipe(
        mergeMap((transacao: HttpResponse<ITransacao>) => {
          if (transacao.body) {
            return of(transacao.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default transacaoResolve;
