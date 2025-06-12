import { CtxActions } from './SessionActions';

export const rechargeTableAction = (data: any): CtxActions => ({
  type: 'rechargeTableAction',
  payload: data
});
