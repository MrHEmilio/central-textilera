import { AddClothPost } from '../../../interfaces/Request/Cloth/AddClothRequest';
import { ClothFormReducerTypes } from '../reducers';
import { CtxActions } from './SessionActions';

export const SetClothForm = (data: AddClothPost): CtxActions => ({
  type: ClothFormReducerTypes.set,
  payload: data
});

export const CleanClothForm = (): CtxActions => ({
  type: ClothFormReducerTypes.cleanClothForm,
  payload: null
});
