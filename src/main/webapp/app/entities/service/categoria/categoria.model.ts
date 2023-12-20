import { ITransacao } from 'app/entities/service/transacao/transacao.model';

export interface ICategoria {
  id: number;
  nome?: string | null;
  tipo?: string | null;
  transacaos?: Pick<ITransacao, 'id'>[] | null;
}

export type NewCategoria = Omit<ICategoria, 'id'> & { id: null };
