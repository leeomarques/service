import { IOrcamento, NewOrcamento } from './orcamento.model';

export const sampleWithRequiredData: IOrcamento = {
  id: 3683,
  ano: 25862,
  mes: 'mink boohoo',
};

export const sampleWithPartialData: IOrcamento = {
  id: 10708,
  ano: 24596,
  mes: 'after',
};

export const sampleWithFullData: IOrcamento = {
  id: 24777,
  ano: 29966,
  mes: 'generally',
};

export const sampleWithNewData: NewOrcamento = {
  ano: 25198,
  mes: 'whereas encash',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
