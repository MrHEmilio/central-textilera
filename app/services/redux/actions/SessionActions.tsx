import { SingleUserInfo } from '../../../interfaces/Response/Client/CreateClient';
import { SessionReducerT } from '../reducers';

export interface CtxActions<> {
  type: string;
  payload?: any;
}

export const SessionActions = (data: SessionReducerT): CtxActions => ({
  type: 'login',
  payload: data
});

export const UpdateSessionInfo = (data: SingleUserInfo): CtxActions => ({
  type: 'sessionInfo',
  payload: data
});

export const LogOut = (): CtxActions => ({
  type: 'logOut'
});

export const newInfoAdmin = (data: any): CtxActions => ({
  type: 'newInfoAdmin',
  payload: data
});
