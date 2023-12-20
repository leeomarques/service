import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrcamento } from '../orcamento.model';
import { OrcamentoService } from '../service/orcamento.service';

export const orcamentoResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrcamento> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrcamentoService)
      .find(id)
      .pipe(
        mergeMap((orcamento: HttpResponse<IOrcamento>) => {
          if (orcamento.body) {
            return of(orcamento.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default orcamentoResolve;
