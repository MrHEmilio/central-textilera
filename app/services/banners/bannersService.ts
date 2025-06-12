import { BaseService } from '../base-service';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { getAllBanner } from '../../interfaces/Response/Banner';
import {
  CreateBanner,
  EditBannerInterface
} from '../../interfaces/Request/Admin/Banners';
import { ResponseCreateBanner } from '../../interfaces/Response/Admin/Banner';
import { toast } from 'react-toastify';

export const getBanners = (
  total = 1
): Promise<PaginationResponse<getAllBanner[]> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<getAllBanner[]>>(
    `banner?page=1&size=${total}`
  );
};

export const newBanner = (
  payload: CreateBanner
): Promise<ResponseCreateBanner | null> => {
  const baseService = new BaseService();
  return baseService.PostRequest<CreateBanner, ResponseCreateBanner>(
    payload,
    'banner'
  );
};

export const deleteBanner = (
  id: string
): Promise<ResponseCreateBanner | null> => {
  const baseService = new BaseService();
  return baseService.DeleteRequest<ResponseCreateBanner>(`banner/${id}`);
};

export const editBanner = (
  payload: EditBannerInterface
): Promise<ResponseCreateBanner | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<EditBannerInterface, ResponseCreateBanner>(payload, 'banner')
    .catch(() => null);
};

export const reactiveBanner = (id: string): Promise<void | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<null, ResponseCreateBanner>(
      null, `banner/${id}`
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