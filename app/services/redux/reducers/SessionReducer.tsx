import { Reducer } from 'react';
import { AnyAction } from 'redux';
import { Menu } from '../../../interfaces/Response/auth/ClienInfo';
import { UserInfo } from '../../../interfaces/Response/User/UserInfo';

export type SessionReducerT = {
  auth: boolean;
  role?: 'ADMIN' | 'CLIENT';
  info?: UserInfo;
  permission?: string[];
  menus?: Menu[];
};

const saveStorageObj = (obj: unknown) => {
  if (typeof window !== undefined) {
    const str = JSON.stringify(obj);
    localStorage.setItem('info', str);
  }
};
const getInitial = () => {
  if (typeof window === 'undefined') {
    return { auth: false };
  }
  const ls = localStorage.getItem('info');
  if (!ls) {
    return { auth: false };
  }
  return JSON.parse(ls) as SessionReducerT;
};

export const SessionReducer: Reducer<SessionReducerT | undefined, AnyAction> = (
  state = getInitial(),
  action: AnyAction
) => {
  switch (action.type) {
    case 'login':
      saveStorageObj({ auth: true, ...action.payload });
      return { auth: true, ...action.payload };
    case 'sessionInfo':
      saveStorageObj({ ...state, info: { ...action.payload } });
      return { ...state, info: { ...action.payload } };
    case 'logOut':
      saveStorageObj({ auth: false });
      return { auth: false };
    case 'newInfoAdmin':
      saveStorageObj({ ...state, info: { ...action.payload } });
      return { ...state, info: { ...action.payload } };
    default:
      return state;
  }
};
