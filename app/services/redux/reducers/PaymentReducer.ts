import { Reducer } from 'react';
import { AnyAction } from 'redux';

export const PaymentReducer: Reducer<unknown, AnyAction> = (
  state = {},
  { type, payload }: AnyAction
) => {
  switch (type) {
    case 'setTypeDelivery':
      return { typeDelivery: payload };
    default:
      return state;
  }
};
