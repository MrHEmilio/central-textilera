import { Cloth } from '../../../interfaces/Response/Cloth/Cloth';
export interface CtxActions<> {
  type: string;
  payload?: any;
}
export const addProductDetail = (data: Cloth): CtxActions => ({
  type: 'addProductDetail',
  payload: data
});

export const cleanProductDetail = (): CtxActions => ({
  type: 'cleanProductDetail'
});
