import { PaginationResponse } from '../../interfaces/paginationResponse';
import { Collection } from '../../interfaces/Response/Collections/CollectionResponses';
import { BaseService } from '../base-service';
import { ResponseCollectionAdmin } from '../../interfaces/Response/Admin/Collection';
import { toast } from 'react-toastify';

const baseService = new BaseService();
export const getCollections = (
  page = 1,
  size = 20,
  active = false
): Promise<PaginationResponse<Collection[]> | null> => {
  return baseService.GetRequest<PaginationResponse<Collection[]>>(
    `collection?page=${page}&size=${size}${active ? '&active=true' : ''}`
  );
};

export const newCollections = (payload: {
  name: string;
  image: string;
}): Promise<ResponseCollectionAdmin | null> => {
  return baseService
    .PostRequest<{ name: string; image: string }, ResponseCollectionAdmin>(
      payload,
      'collection'
    )
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const editCollection = (payload: {
  name: string;
  image?: string;
}): Promise<ResponseCollectionAdmin | null> => {
  return baseService
    .PutRequest<{ name: string; image?: string }, ResponseCollectionAdmin>(
      payload,
      'collection'
    )
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const deleteCollection = (
  id: string
): Promise<ResponseCollectionAdmin | null> => {
  return baseService.DeleteRequest<ResponseCollectionAdmin>(`collection/${id}`);
};

export const reactiveCollection = (id: string): Promise<void | null> => {

  return baseService
    .PutRequest<null, ResponseCollectionAdmin>(
      null, `collection/${id}`
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
