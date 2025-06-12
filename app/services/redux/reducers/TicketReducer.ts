import { Reducer } from 'react';
import { AnyAction } from 'redux';

export enum TicketActionsEnum {
  get = 'get',
  set = 'setTicket',
  clear = 'clear'
}

export const TicketReducer: Reducer<unknown, AnyAction> = (
  state = {},
  action: AnyAction
) => {
  switch (action.type) {
    case TicketActionsEnum.get:
      return state;
    case TicketActionsEnum.set:
      state = action.payload;
      return state;
    case TicketActionsEnum.clear:
      return {};
    default:
      return state;
  }
};
