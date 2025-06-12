import { toast } from 'react-toastify';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import {
  AddClothPost,
  AddClothPostResponse
} from '../../interfaces/Request/Cloth/AddClothRequest';
import { Cloth, Fiber, Price } from '../../interfaces/Response/Cloth/Cloth';
import { Catalog } from '../../interfaces/Utils/IUtils';
import { BaseService } from '../base-service/BaseService';

const baseService = new BaseService();

export const getFeaturedProduct = (): Promise<PaginationResponse<
  Cloth[]
> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<Cloth[]>>(
    'cloth?page=1&size=10&random=true&responseStructure=PRICES,SAMPLER,VARIANTS'
  );
};

export const getOrderedCloths = (
  size = 40,
  page = 1,
  collections?: string,
  search?: string
): Promise<PaginationResponse<Cloth[]> | null> => {
  const baseService = new BaseService();
  let url = `cloth?page=${page}&size=${size}&random=false`;
  if (collections) url += `&collections=${collections || null}`;
  if (search) url += `&search=${search || null}`;
  return baseService.GetRequest<PaginationResponse<Cloth[]>>(url);
};

export const getClothByName = (
  name: string,
  page = 1,
  size = 1
): Promise<PaginationResponse<Cloth[]> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<Cloth[]>>(
    `cloth?page=${page}&size=${size}&search=${name}&responseStructure=ALL`
  );
};

export const getClothBySearchUrl = (
  name: string,
  page = 1,
  size = 1
): Promise<PaginationResponse<Cloth[]> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<Cloth[]>>(
    `cloth?page=${page}&size=${size}&searchUrl=${name}&responseStructure=ALL`
  );
};

export const getClothPricesById = async (
  id: string
): Promise<Price[] | null | undefined> => {
  const baseService = new BaseService();
  return baseService
    .GetRequest<PaginationResponse<Cloth[]>>(
      `cloth?page=1&size=1&search=${id}&responseStructure=ALL`
    )
    .then(r => r?.content[0].prices);
};

export const getClothUsesFilters = async (
  page = 1
): Promise<PaginationResponse<Catalog[]> | undefined> => {
  const baseService = new BaseService();

  return baseService
    .GetRequest<PaginationResponse<Fiber[]>>(`use?page=${page}&size=${20}`)
    .then(res => {
      if (!res) return undefined;
      const content: Catalog[] = res.content.map(i => ({
        id: i.id,
        name: i.name
      }));
      const resMapped: PaginationResponse<Catalog[]> = {
        pagination: res.pagination,
        content
      };
      return resMapped;
    });
};

export const dynamicPaginationService = async (
  url: string
): Promise<PaginationResponse<unknown[]> | null> => {
  const baseService = new BaseService();
  return baseService
    .GetRequest<PaginationResponse<unknown[]>>(url, true)
    .catch(() => {
      return {
        content: [],
        pagination: { page: 1, size: 0, totalPage: 0, totalRecords: 0 }
      };
    });
};

export const addClothService = (
  req: AddClothPost
): Promise<AddClothPostResponse | null> => {
  const base = new BaseService();
  return base
    .PostRequest<AddClothPost, AddClothPostResponse>(req, '/cloth')
    .catch(e => {
      toast.error(e?.message || 'Ha ocurrido un error, intenta de nuevo', {
        theme: 'colored'
      });
      return null;
    });
};

export const editClothService = async (
  req: AddClothPost
): Promise<AddClothPostResponse | null> => {
  const base = new BaseService();
  const r = await base
    .PutRequest<AddClothPost, AddClothPostResponse>(req, '/cloth')
    .catch(e => {
      toast.error(e.message || 'Hubo un errro, intentar de nuevo más tarde', {
        theme: 'colored'
      });
      return null;
    });
  if (r) {
    await revalidateByProductName(r.data.nameUrl);
    setTimeout(() => {
      revalidateByProductName(r.data.nameUrl);
    }, 1000);
  }
  return r;
};

export const deleteClothService = (
  id: string
): Promise<{ data: AddClothPostResponse; message: string } | null> => {
  const base = new BaseService();
  return base.DeleteRequest(`/cloth/${id}`);
};

export const activateClothService = async (
  clothId: string
): Promise<boolean> => {
  return new BaseService()
    .PutRequest<string, { message: string }>('', `/cloth/${clothId}`)
    .then(r => {
      toast.success(r?.message, { theme: 'colored' });
      return !!r?.message;
    })
    .catch(() => false);
};

/**
 * @param productname is the product url name like alaska-extra url
 */
export const revalidateByProductName = async (productname: string) => {
  const baseFront = process.env.NEXT_PUBLIC_BASE_FRONT;
  const secret = process.env.NEXT_PUBLIC_REVALIDATE_TKN;

  if (!baseFront || !secret) return;
  return baseService.GetRequest(
    `${baseFront}/api/revalidate?` +
      `secret=${secret}&` +
      `productname=${productname}`
  );
};
