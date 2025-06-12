import { AnyAction, Reducer } from 'redux';
import { Collection } from '../../../interfaces/Response/Collections/CollectionResponses';

export enum CollectionSelectOptions {
  // eslint-disable-next-line no-unused-vars
  set = 'setCollectionSelect',
  // eslint-disable-next-line no-unused-vars
  clear = 'clearCollectionSelect'
}

export const CollectionSelectReducer: Reducer<
  Collection | undefined,
  AnyAction
> = (state: Collection | unknown = {}, action: AnyAction) => {
  switch (action.type) {
    case CollectionSelectOptions.set:
      return action.payload;
    case CollectionSelectOptions.clear:
      return {};
    default:
      return state;
  }
};
