import { AnyAction, Reducer } from 'redux';

export enum LoaderActionsEnum {
  show = 'set',
  hide = 'hide'
}

export interface LoaderState {
  visible: boolean;
}
export const LoaderReducer: Reducer<LoaderState, AnyAction> = (
  state = { visible: false },
  action: AnyAction
) => {
  switch (action.type) {
    case LoaderActionsEnum.show:
      return { visible: true };
    case LoaderActionsEnum.hide:
      return { visible: false };
    default:
      return state;
  }
};
