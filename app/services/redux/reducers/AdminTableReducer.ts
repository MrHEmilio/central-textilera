import { Reducer } from 'react';
import { AnyAction } from 'redux';

export const AdminTableReducer: Reducer<unknown, AnyAction> = (
  state = { recharge: false },
  action: AnyAction
) => {
  switch (action.type) {
    case 'rechargeTableAction':
      return { recharge: action.payload };
    default:
      return state;
  }
};
