import { toast } from 'react-toastify';
import {
  CreateUser,
  UpdateClient
} from '../../interfaces/Request/Client/CreateUser';
import {
  CreateClientResponse,
  SingleUserInfo,
  UpdateClientResponse
} from '../../interfaces/Response/Client/CreateClient';
import { PostResponse } from '../../interfaces/Response/PostResponse';
import { BaseService } from '../base-service';

export const createClient = (
  payload: CreateUser
): Promise<CreateClientResponse | null> => {
  const baseService = new BaseService();
  return baseService
    .PostRequest<CreateUser, CreateClientResponse>(payload, 'client')
    .catch(() => null);
};

export const updateClient = async (
  payload: CreateUser
): Promise<PostResponse<SingleUserInfo> | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<CreateUser, PostResponse<SingleUserInfo>>(payload, '/client')
    .catch(() => null);
};

export const updateNotSignedClient = async (
  payload: UpdateClient
): Promise<PostResponse<UpdateClientResponse> | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<UpdateClient, PostResponse<UpdateClientResponse>>(
      payload,
      '/client'
    )
    .then(r => {
      // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
      toast.success(r!.message, { theme: 'colored' });
      return null;
    })
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};
