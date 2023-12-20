import { ITransacao } from 'app/entities/service/transacao/transacao.model';

export interface IOrcamento {
  id: number;
  ano?: number | null;
  mes?: string | null;
  transacaos?: Pick<ITransacao, 'id'>[] | null;
}

export type NewOrcamento = Omit<IOrcamento, 'id'> & { id: null };
