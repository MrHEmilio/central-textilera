import { Reducer } from 'react';
import { AnyAction } from 'redux';

export const EditDirections: Reducer<unknown, AnyAction> = (
  state = {},
  action: AnyAction
) => {
  switch (action.type) {
    case 'editDirection':
      return { ...action.payload };

    default:
      return state;
  }
};
