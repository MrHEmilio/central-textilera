import { IFabricsReducer } from '../../../interfaces/State/FabricsReducer';
import { OrderByEnum } from '../../../models';
import { FabricsActionsEnum } from '../reducers/FabricsReducer';
import { CtxActions } from './SessionActions';

export const FabricsActions = (
  state: IFabricsReducer = {
    filters: {
      orderBy: OrderByEnum.asc,
      uses: '',
      sales: '',
      fibers: '',
      collections: '',
      columnSort: 'name',
      loading: false
    }
  }
): CtxActions => ({
  type: FabricsActionsEnum.orderBy.toString(),
  payload: state
});

export const FabricsActionsUpdateOrder = (
  newOrderBy: OrderByEnum
): CtxActions => {
  return {
    type: FabricsActionsEnum.updateOrderBy.toString(),
    payload: newOrderBy
  };
};

export const FabricsActionsColumSort = (newColumSort: string): CtxActions => {
  return {
    type: FabricsActionsEnum.columSort.toString(),
    payload: newColumSort
  };
};

export const FabricsActionsUsesFilter = (newUses: string): CtxActions => ({
  type: FabricsActionsEnum.updateUses.toString(),
  payload: newUses
});

export const FabricsActionsFibersFilter = (newFibers: string): CtxActions => ({
  type: FabricsActionsEnum.updateFibers.toString(),
  payload: newFibers
});

export const FabricsActionLoading = (load: boolean): CtxActions => ({
  type: FabricsActionsEnum.updateLoading.toString(),
  payload: load
});

export const FabricsActionsCollectionFilter = (id: string): CtxActions => ({
  type: FabricsActionsEnum.updateCollections.toString(),
  payload: id
});

export const FabricsActionsReset = (): CtxActions => ({
  type: FabricsActionsEnum.reset.toString(),
  payload: null
});

export const FabricsActionsSalesFilter = (sales: string): CtxActions => ({
  type: FabricsActionsEnum.updateSales.toString(),
  payload: sales
});
