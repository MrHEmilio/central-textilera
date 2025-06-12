import { BaseService } from '../base-service';
import {
  DataFiber,
  ResponseCreateFiber
} from '../../interfaces/Response/Admin/Fiber';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { toast } from 'react-toastify';

const baseService = new BaseService();
export const newFiber = (payload: {
  name: string;
}): Promise<ResponseCreateFiber | null> => {
  return baseService
    .PostRequest<{ name: string }, ResponseCreateFiber>(payload, 'fiber')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const editFiber = (payload: {
  id: string;
  name: string;
}): Promise<ResponseCreateFiber | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<
      {
        id: string;
        name: string;
      },
      ResponseCreateFiber
    >(payload, 'fiber')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const deleteFiber = (
  id: string
): Promise<ResponseCreateFiber | null> => {
  const baseService = new BaseService();
  return baseService.DeleteRequest<ResponseCreateFiber>(`fiber/${id}`);
};

export const getFiber = (
  active = false
): Promise<PaginationResponse<DataFiber[]> | null> => {
  return baseService.GetRequest<PaginationResponse<DataFiber[]>>(
    `fiber?page=1&size=1000&active=${active}`
  );
};


export const reactivateFiber = (
  id: string
): Promise<void | null> => {
  return baseService
    .PutRequest<null, ResponseCreateFiber>(
      null, `fiber/${id}`
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
};