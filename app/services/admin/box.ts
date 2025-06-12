import { toast } from 'react-toastify';
import { RequestCreateBox } from '../../interfaces/Request/Admin/Box';
import { BoxCaculateResponse, ResponseCreateBox } from '../../interfaces/Response/Admin/Box';
import { BaseService } from '../base-service';

const baseService = new BaseService();
export const newBox = (
  payload: RequestCreateBox
): Promise<ResponseCreateBox | null> => {
  return baseService
    .PostRequest<RequestCreateBox, ResponseCreateBox>(payload, 'box')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const editBox = (
  payload: RequestCreateBox
): Promise<ResponseCreateBox | null> => {
  return baseService
    .PutRequest<RequestCreateBox, ResponseCreateBox>(payload, 'box')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const deleteBox = (id: string): Promise<ResponseCreateBox | null> => {
  return baseService.DeleteRequest<ResponseCreateBox>(`box/${id}`);
};

export const boxCalculate = (order: string ): Promise<BoxCaculateResponse[] | null> => {
  return baseService.PostRequest<{order: string}, BoxCaculateResponse[]>({order}, 'box/calculate').catch(e => {
    toast.error(e.message, { theme: 'colored' });
    return null;
  });
}


export const reactivateBox = (id: string): Promise<void | null> => {
  return baseService
    .PutRequest<null, ResponseCreateBox>(
      null, `box/${id}`
    )
    .then((r: any) => {
      toast.success(
        r?.message,
        { theme: 'colored' }
      );
    })
    .catch((r: any) => {
      toast.error(r.message, { theme: 'colored' });
    });
  }