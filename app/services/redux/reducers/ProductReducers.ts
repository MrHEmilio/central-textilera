import { Reducer } from 'react';
import { AnyAction } from 'redux';

export const ProductDetailReducer: Reducer<unknown, AnyAction> = (
  state = {},
  { type, payload }: AnyAction
) => {
  switch (type) {
    case 'addProductDetail':
      return { ...payload };
    case 'cleanProductDetail':
      return {};
    default:
      return state;
  }
};
