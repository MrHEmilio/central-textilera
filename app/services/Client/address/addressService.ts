import { BaseService } from '../../base-service';
import { ZipCode } from '../../../interfaces/Response/Client/ZipCode';
import { NewDirection } from '../../../interfaces/Response/Client/NewDirection';
import { NewAddress } from '../../../interfaces/Request/Client/Address';
import { PaginationResponse } from '../../../interfaces/paginationResponse';

export const getAllClientAddress = (): Promise<PaginationResponse<
  NewAddress[]
> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<NewAddress[]>>(
    `/client/address?page=1&size=1000`
  );
};

export const newAddressClient = (
  payload: NewAddress
): Promise<NewDirection | null> => {
  const baseService = new BaseService();
  return baseService.PostRequest<NewAddress, NewDirection>(
    payload,
    'client/address'
  );
};

export const getInfoZipCode = (zipCode: string, response: boolean) => {
  const baseService = new BaseService();
  return baseService.GetRequest<ZipCode>(
    `catalog/address/info/${zipCode}`,
    response
  );
};

export const deleteAddressService = (
  addressId: string
): Promise<NewDirection | null> => {
  const baseService = new BaseService();
  return baseService.DeleteRequest<NewDirection>(`client/address/${addressId}`);
};

export const editAddressService = (
  payload: NewAddress
): Promise<NewDirection | null> => {
  const baseService = new BaseService();
  return baseService.PutRequest<NewAddress, NewDirection>(
    payload,
    'client/address'
  );
};

export const getAllClientAddressId = (
  id: string
): Promise<PaginationResponse<NewAddress[]> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<NewAddress[]>>(
    `/client/address/${id}?page=1&size=1000`
  );
};
