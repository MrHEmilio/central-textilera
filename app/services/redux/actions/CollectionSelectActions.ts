import { Collection } from '../../../interfaces/Response/Collections/CollectionResponses';
import { CollectionSelectOptions } from '../reducers/CollectionSelectReducer';
import { CtxActions } from './SessionActions';

export const SetCollectionSelectState = (
  collection: Collection
): CtxActions => ({
  type: CollectionSelectOptions.set,
  payload: collection
});

export const ClearCollectionSelectState = (): CtxActions => ({
  type: CollectionSelectOptions.clear,
  payload: null
});
