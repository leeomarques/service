import { ITransacao, NewTransacao } from './transacao.model';

export const sampleWithRequiredData: ITransacao = {
  id: 24965,
  valor: 6858.86,
};

export const sampleWithPartialData: ITransacao = {
  id: 15335,
  valor: 12930.99,
};

export const sampleWithFullData: ITransacao = {
  id: 19018,
  valor: 14801.78,
};

export const sampleWithNewData: NewTransacao = {
  valor: 11751.79,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
