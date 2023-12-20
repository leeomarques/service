import { ICategoria, NewCategoria } from './categoria.model';

export const sampleWithRequiredData: ICategoria = {
  id: 7512,
  nome: 'blindly yawning punctual',
  tipo: 'spry tremendously',
};

export const sampleWithPartialData: ICategoria = {
  id: 31235,
  nome: 'realign for',
  tipo: 'miscast bleakly',
};

export const sampleWithFullData: ICategoria = {
  id: 12261,
  nome: 'freely yippee upon',
  tipo: 'daily',
};

export const sampleWithNewData: NewCategoria = {
  nome: 'exactly surprisingly sector',
  tipo: 'jubilant collaboration near',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
