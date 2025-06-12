import { Reducer } from 'react';
import { AnyAction } from 'redux';
import { Cloth } from '../../../interfaces/Response/Cloth/Cloth';

export enum ClothFormReducerTypes {
  // eslint-disable-next-line no-unused-vars
  set = 'setClothForm',
  // eslint-disable-next-line no-unused-vars
  clothToEdit = 'clothEdit',
  // eslint-disable-next-line no-unused-vars
  cleanClothForm = 'cleanClothForm'
}

const initial: Cloth = {
  collections: [],
  nameUrl: '',
  fiber: { name: '', active: false, id: '' },
  image: '',
  mainDescription: '',
  measure: {
    id: '',
    dimension: 0,
    weight: 0,
    width: 0,
    yieldPerKilo: 0,
    averagePerRoll: 0
  },
  name: '',
  sampler: {
    image: '',
    description: '',
    price: 0,
    id: '',
    billing: {
      unitLabel: '',
      unitCode: '',
      productCode: '',
      productLabel: ''
    }
  },
  sale: { id: '', name: '', active: false, abbreviation: '' },
  prices: [],
  uses: [],
  variants: [],
  active: false,
  descriptions: [],
  id: '',
  billing: {
    productCode: '',
    productLabel: '',
    unitCode: '',
    unitLabel: ''
  }
};

export const ClothFormReducer: Reducer<Cloth | undefined, AnyAction> = (
  state: Cloth = initial,
  action: AnyAction
) => {
  switch (action.type) {
    case ClothFormReducerTypes.set:
      return { ...state, ...action.payload };
    case ClothFormReducerTypes.cleanClothForm:
      return initial;
    default:
      return state;
  }
};
