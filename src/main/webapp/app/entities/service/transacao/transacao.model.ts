import { IOrcamento } from 'app/entities/service/orcamento/orcamento.model';
import { ICategoria } from 'app/entities/service/categoria/categoria.model';

export interface ITransacao {
  id: number;
  valor?: number | null;
  orcamento?: Pick<IOrcamento, 'id'> | null;
  categoria?: Pick<ICategoria, 'id'> | null;
}

export type NewTransacao = Omit<ITransacao, 'id'> & { id: null };
