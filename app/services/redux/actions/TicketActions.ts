import { OrderDetail } from '../../../interfaces/Response/Orders/Orders';
import { TicketActionsEnum } from '../reducers/TicketReducer';
import { CtxActions } from './SessionActions';

export const TicketActionsSet = (ticket: OrderDetail): CtxActions => ({
  type: TicketActionsEnum.set,
  payload: ticket
});

export const TicketActionsClear = (): CtxActions => ({
  type: TicketActionsEnum.clear,
  payload: null
});
