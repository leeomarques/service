import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransacao, NewTransacao } from '../transacao.model';

export type PartialUpdateTransacao = Partial<ITransacao> & Pick<ITransacao, 'id'>;

export type EntityResponseType = HttpResponse<ITransacao>;
export type EntityArrayResponseType = HttpResponse<ITransacao[]>;

@Injectable({ providedIn: 'root' })
export class TransacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transacaos', 'service');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(transacao: NewTransacao): Observable<EntityResponseType> {
    return this.http.post<ITransacao>(this.resourceUrl, transacao, { observe: 'response' });
  }

  update(transacao: ITransacao): Observable<EntityResponseType> {
    return this.http.put<ITransacao>(`${this.resourceUrl}/${this.getTransacaoIdentifier(transacao)}`, transacao, { observe: 'response' });
  }

  partialUpdate(transacao: PartialUpdateTransacao): Observable<EntityResponseType> {
    return this.http.patch<ITransacao>(`${this.resourceUrl}/${this.getTransacaoIdentifier(transacao)}`, transacao, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransacao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransacao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTransacaoIdentifier(transacao: Pick<ITransacao, 'id'>): number {
    return transacao.id;
  }

  compareTransacao(o1: Pick<ITransacao, 'id'> | null, o2: Pick<ITransacao, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransacaoIdentifier(o1) === this.getTransacaoIdentifier(o2) : o1 === o2;
  }

  addTransacaoToCollectionIfMissing<Type extends Pick<ITransacao, 'id'>>(
    transacaoCollection: Type[],
    ...transacaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transacaos: Type[] = transacaosToCheck.filter(isPresent);
    if (transacaos.length > 0) {
      const transacaoCollectionIdentifiers = transacaoCollection.map(transacaoItem => this.getTransacaoIdentifier(transacaoItem)!);
      const transacaosToAdd = transacaos.filter(transacaoItem => {
        const transacaoIdentifier = this.getTransacaoIdentifier(transacaoItem);
        if (transacaoCollectionIdentifiers.includes(transacaoIdentifier)) {
          return false;
        }
        transacaoCollectionIdentifiers.push(transacaoIdentifier);
        return true;
      });
      return [...transacaosToAdd, ...transacaoCollection];
    }
    return transacaoCollection;
  }
}
