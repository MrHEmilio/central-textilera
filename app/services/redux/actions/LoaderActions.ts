import { LoaderActionsEnum } from '../reducers';
import { CtxActions } from './SessionActions';

export const LoaderActionsShow = (): CtxActions => ({
  type: LoaderActionsEnum.show,
  payload: null
});

export const LoaderActionsHide = (): CtxActions => ({
  type: LoaderActionsEnum.hide,
  payload: null
});
