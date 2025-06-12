import {
  ResponseAdminClient,
  CountryCode
} from '../../interfaces/Response/Admin/Client';
import { BaseService } from '../base-service';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { OrderByEnum } from '../../models';
import { EditClientAdminRequest } from '../../interfaces/Request/Admin/Cliente';
import {
  EditClientAdminResponse,
  VerifyClientExistsResponse
} from '../../interfaces/Response/Admin/EditClient';

const base = new BaseService();
export const clientVerifyService = (
  tkn: string
): Promise<{ message: string } | null> => {
  return base.PutRequest<unknown, { message: string }>(
    undefined,
    `/client/verify/${tkn}`
  );
};

export const getIdCliente = (
  id: string
): Promise<ResponseAdminClient | null> => {
  return base.GetRequest<ResponseAdminClient>(`/client/info/${id}`);
};

export const getCountryCode = (
  search?: string
): Promise<PaginationResponse<CountryCode[]> | null> => {
  if (search) {
    return base.GetRequest<PaginationResponse<CountryCode[]>>(
      `/catalog/country/code?search=${search}&active=true&page=1&size=1000&columSort=name&typeSort=${OrderByEnum.asc}`
    );
  }
  return base.GetRequest<PaginationResponse<CountryCode[]>>(
    `/catalog/country/code?page=1&size=1000&columSort=name&typeSort=${OrderByEnum.asc}`
  );
};

export const editClientAdmin = (
  payload: EditClientAdminRequest
): Promise<EditClientAdminResponse | null> => {
  return base
    .PutRequest<EditClientAdminRequest, EditClientAdminResponse>(
      payload,
      '/client'
    )
    .catch(() => null);
};

export const deleteClientAdmin = (
  id: string
): Promise<EditClientAdminResponse | null> => {
  return base.DeleteRequest<EditClientAdminResponse>(`/client/${id}`);
};

export const reactiveClient = (
  id: string
): Promise<{ message: string } | null> => {
  return base.PostRequestQ(`/send/email/reactive/client/${id}`, false);
};

export const putClientReactive = (tkn: string) => {
  return base.PutRequest<string, { message: string }>(
    '',
    `/client/reactive/${tkn}`
  );
};

export const verifyClientExists = async (
  email: string,
  countryCode = '',
  phone = '',
  rfc = ''
) => {
  return base.GetRequest<VerifyClientExistsResponse>(
    `client/verify/${email}?phone=${phone}&countryCode=${countryCode}&rfc=${rfc}`
  );
};
