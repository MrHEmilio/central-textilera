import { BaseService } from '../base-service';
import {
  ResponseColors,
  DataColors
} from '../../interfaces/Response/Admin/Colors';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { toast } from 'react-toastify';

const baseService = new BaseService();

export const newColor = (payload: {
  name: string;
  code: string;
}): Promise<ResponseColors | null> => {
  return baseService
    .PostRequest<
      {
        name: string;
        code: string;
      },
      ResponseColors
    >(payload, 'color')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const editColor = (payload: {
  id: string;
  name: string;
  code: string;
}): Promise<ResponseColors | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<
      {
        id: string;
        name: string;
        code: string;
      },
      ResponseColors
    >(payload, 'color')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const deleteColor = (id: string): Promise<ResponseColors | null> => {
  const baseService = new BaseService();
  return baseService.DeleteRequest<ResponseColors>(`color/${id}`);
};

export const getColorAdmin = (
  active = false
): Promise<PaginationResponse<DataColors[]> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<DataColors[]>>(
    `color?page=1&size=1000${active ? '&active=true' : ''}`
  );
};

export const reactivateColor = (id: string): Promise<void | null> => {

  return baseService
    .PutRequest<null, ResponseColors>(
      null, `color/${id}`
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