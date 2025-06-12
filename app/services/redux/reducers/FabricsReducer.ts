import { Reducer } from 'react';
import { AnyAction } from 'redux';
import { IFabricsReducer } from '../../../interfaces/State/FabricsReducer';
import { OrderByEnum } from '../../../models';

export enum FabricsActionsEnum {
  // eslint-disable-next-line no-unused-vars
  orderBy = 'orderBy',
  // eslint-disable-next-line no-unused-vars
  columSort = 'columnSort',
  // eslint-disable-next-line no-unused-vars
  updateOrderBy = 'updateOrderBy',
  // eslint-disable-next-line no-unused-vars
  updateUses = 'updateUses',
  // eslint-disable-next-line no-unused-vars
  updateFibers = 'updateFibers',
  // eslint-disable-next-line no-unused-vars
  updateLoading = 'updateLoading',
  // eslint-disable-next-line no-unused-vars
  updateCollections = 'updateCollections',
  // eslint-disable-next-line no-unused-vars
  reset = 'reset',
  // eslint-disable-next-line no-unused-vars
  updateSales = 'updateSales'
}

export const FabricsReducer: Reducer<IFabricsReducer | undefined, AnyAction> = (
  state: IFabricsReducer = {
    filters: {
      orderBy: OrderByEnum.asc,
      uses: '',
      loading: false,
      collections: '',
      sales: '',
      fibers: '',
      columnSort: 'name'
    }
  },
  action: AnyAction
) => {
  switch (action.type) {
    case FabricsActionsEnum.orderBy:
      return { ...state, ...action.payload };
    case FabricsActionsEnum.columSort:
      return {
        ...state,
        ...{ filters: { ...state.filters, columnSort: action.payload } }
      };
    case FabricsActionsEnum.updateOrderBy:
      return {
        ...state,
        ...{ filters: { ...state.filters, orderBy: action.payload } }
      };
    case FabricsActionsEnum.updateUses:
      return {
        ...state,
        ...{ filters: { ...state.filters, uses: action.payload } }
      };
    case FabricsActionsEnum.updateSales:
      return {
        ...state,
        ...{ filters: { ...state.filters, sales: action.payload } }
      };
    case FabricsActionsEnum.updateFibers:
      return {
        ...state,
        ...{ filters: { ...state.filters, fibers: action.payload } }
      };
    case FabricsActionsEnum.updateLoading:
      return {
        ...state,
        ...{ filters: { ...state.filters, loading: action.payload } }
      };
    case FabricsActionsEnum.updateCollections:
      return {
        ...state,
        ...{ filters: { ...state.filters, collections: action.payload } }
      };
    case FabricsActionsEnum.reset:
      return {
        ...state,
        ...{
          filters: {
            ...state.filters,
            orderBy: OrderByEnum.asc,
            uses: '',
            sales: '',
            collections: '',
            fibers: ''
          }
        }
      };
    default:
      return { ...state };
  }
};
