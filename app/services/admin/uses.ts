import {
  DataUse,
  ResponseCreateUse
} from '../../interfaces/Response/Admin/Use';
import { BaseService } from '../base-service';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { toast } from 'react-toastify';

export const newUse = (payload: {
  name: string;
}): Promise<ResponseCreateUse | null> => {
  const baseService = new BaseService();
  return baseService
    .PostRequest<{ name: string }, ResponseCreateUse>(payload, 'use')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const editUse = (payload: {
  id: string;
  name: string;
}): Promise<ResponseCreateUse | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<
      {
        id: string;
        name: string;
      },
      ResponseCreateUse
    >(payload, 'use')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const deleteUse = (id: string): Promise<ResponseCreateUse | null> => {
  const baseService = new BaseService();
  return baseService.DeleteRequest<ResponseCreateUse>(`use/${id}`);
};

export const getUse = (
  active = false
): Promise<PaginationResponse<DataUse[]> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<DataUse[]>>(
    `use?page=1&size=1000${active ? '&active=true' : ''}`
  );
};
