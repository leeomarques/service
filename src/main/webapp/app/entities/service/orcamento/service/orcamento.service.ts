import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrcamento, NewOrcamento } from '../orcamento.model';

export type PartialUpdateOrcamento = Partial<IOrcamento> & Pick<IOrcamento, 'id'>;

export type EntityResponseType = HttpResponse<IOrcamento>;
export type EntityArrayResponseType = HttpResponse<IOrcamento[]>;

@Injectable({ providedIn: 'root' })
export class OrcamentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orcamentos', 'service');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(orcamento: NewOrcamento): Observable<EntityResponseType> {
    return this.http.post<IOrcamento>(this.resourceUrl, orcamento, { observe: 'response' });
  }

  update(orcamento: IOrcamento): Observable<EntityResponseType> {
    return this.http.put<IOrcamento>(`${this.resourceUrl}/${this.getOrcamentoIdentifier(orcamento)}`, orcamento, { observe: 'response' });
  }

  partialUpdate(orcamento: PartialUpdateOrcamento): Observable<EntityResponseType> {
    return this.http.patch<IOrcamento>(`${this.resourceUrl}/${this.getOrcamentoIdentifier(orcamento)}`, orcamento, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrcamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrcamento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrcamentoIdentifier(orcamento: Pick<IOrcamento, 'id'>): number {
    return orcamento.id;
  }

  compareOrcamento(o1: Pick<IOrcamento, 'id'> | null, o2: Pick<IOrcamento, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrcamentoIdentifier(o1) === this.getOrcamentoIdentifier(o2) : o1 === o2;
  }

  addOrcamentoToCollectionIfMissing<Type extends Pick<IOrcamento, 'id'>>(
    orcamentoCollection: Type[],
    ...orcamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orcamentos: Type[] = orcamentosToCheck.filter(isPresent);
    if (orcamentos.length > 0) {
      const orcamentoCollectionIdentifiers = orcamentoCollection.map(orcamentoItem => this.getOrcamentoIdentifier(orcamentoItem)!);
      const orcamentosToAdd = orcamentos.filter(orcamentoItem => {
        const orcamentoIdentifier = this.getOrcamentoIdentifier(orcamentoItem);
        if (orcamentoCollectionIdentifiers.includes(orcamentoIdentifier)) {
          return false;
        }
        orcamentoCollectionIdentifiers.push(orcamentoIdentifier);
        return true;
      });
      return [...orcamentosToAdd, ...orcamentoCollection];
    }
    return orcamentoCollection;
  }
}
