import { BaseService } from '../base-service';
import {
  ResponseTypeSale,
  DataTypeSale
} from '../../interfaces/Response/Admin/typeSale';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { toast } from 'react-toastify';

const baseService = new BaseService();

export const newTypeSale = (payload: {
  name: string;
  abbreviation: string;
}): Promise<ResponseTypeSale | null> => {
  return baseService
    .PostRequest<{ name: string }, ResponseTypeSale>(payload, 'sale')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const editTypeSale = (payload: {
  id: string;
  name: string;
  abbreviation: string;
}): Promise<ResponseTypeSale | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<
      {
        id: string;
        name: string;
      },
      ResponseTypeSale
    >(payload, 'sale')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const deleteTypeSale = (
  id: string
): Promise<ResponseTypeSale | null> => {
  const baseService = new BaseService();
  return baseService.DeleteRequest<ResponseTypeSale>(`sale/${id}`);
};

export const getTypeSale = (
  active = false
): Promise<PaginationResponse<DataTypeSale[]> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<DataTypeSale[]>>(
    `sale?page=1&size=1000&active=${active}`
  );
};

export const reactivateSale = (id: string): Promise<void | null> => {

  return baseService
    .PutRequest<null, ResponseTypeSale>(
      null, `sale/${id}`
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