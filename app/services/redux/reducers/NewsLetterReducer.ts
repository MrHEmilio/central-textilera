import { AnyAction, Reducer } from 'redux';
import { getNewsLetterStorage } from '../../utils';
import { CtxActions } from '../actions/SessionActions';

export interface NewsLetterState {
  email?: boolean;
}

export const NewsLetterStateOps = {
  set: 'set-email',
  clear: 'clear-email'
};

const saveOnStorage = (obj: unknown) => {
  if (typeof window !== 'undefined') {
    const str = JSON.stringify(obj);
    localStorage.setItem('newsLetterSave', str);
  }
};

const initialSatate = (): NewsLetterState => {
  return getNewsLetterStorage() || { email: false };
};

export const NewsLetterReducer: Reducer<NewsLetterState, AnyAction> = (
  state = initialSatate(),
  action
) => {
  switch (action.type) {
    case NewsLetterStateOps.set:
      saveOnStorage({ ...state, email: action.payload });
      return { ...state, email: action.payload };
    case NewsLetterStateOps.clear:
      localStorage.removeItem('newsLetterSave');
      return {};
    default:
      return state;
  }
};

export const SetNewsLetterState = (email: boolean): CtxActions => ({
  type: NewsLetterStateOps.set,
  payload: email
});

export const ClearNewsLetterState = (): CtxActions => ({
  type: NewsLetterStateOps.clear,
  payload: undefined
});
