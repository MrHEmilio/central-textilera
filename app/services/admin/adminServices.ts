import { BaseService } from '../base-service';
import {
  AdminResponseAdmin,
  ResponseCreateAdmin
} from '../../interfaces/Response/Admin/AdminResposeAdmin';
import { CreateRequestAdmin } from '../../interfaces/Request/Admin/AdminResponseAdmin';

const baseService = new BaseService();

export const getIdAdmin = (id: string): Promise<AdminResponseAdmin | null> => {
  return baseService.GetRequest<AdminResponseAdmin>(`/admin/info/${id}`);
};

export const createAdmin = (
  payload: CreateRequestAdmin
): Promise<ResponseCreateAdmin | null> => {
  return baseService
    .PostRequest<CreateRequestAdmin, ResponseCreateAdmin>(payload, '/admin')
    .catch(() => null);
};

export const editSelfAdmin = async (payload: CreateRequestAdmin) => {
  return baseService
    .PutRequest<CreateRequestAdmin, ResponseCreateAdmin>(
      {
        name: payload.name,
        firstLastname: payload.firstLastname,
        secondLastname: payload.secondLastname
      },
      `/admin`
    )
    .catch(() => null);
};

export const editAdmin = (
  payload: CreateRequestAdmin
): Promise<ResponseCreateAdmin | null> => {
  return baseService
    .PutRequest<CreateRequestAdmin, ResponseCreateAdmin>(
      {
        name: payload.name,
        firstLastname: payload.firstLastname,
        secondLastname: payload.secondLastname
      },
      `/admin/${payload.id}`
    )
    .catch(() => null);
};

export const deleteAdmin = (
  id: string
): Promise<ResponseCreateAdmin | null> => {
  return baseService.DeleteRequest<ResponseCreateAdmin>(`/admin/${id}`);
};
