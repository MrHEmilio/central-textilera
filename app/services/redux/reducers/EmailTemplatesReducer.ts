/* eslint-disable no-unused-vars */
import { Reducer } from 'react';
import { AnyAction } from 'redux';
import { SingleEmailTemplate } from '../../../interfaces/Response/Admin/EmailServicesResponses';

export enum EmailTemplateActions {
  set = 'set',
  clean = 'clean',
  loading = 'loading'
}

export const EmailTemplateReducer: Reducer<
  (SingleEmailTemplate & { loading: boolean }) | undefined,
  AnyAction
> = (
  state: (SingleEmailTemplate & { loading: boolean }) | null = null,
  action: AnyAction
) => {
  switch (action.type) {
    case EmailTemplateActions.set:
      return action.payload;
    case EmailTemplateActions.clean:
      return null;
    case EmailTemplateActions.loading:
      return { ...state, loading: action.payload };
    default:
      return state;
  }
};
