import { toast } from 'react-toastify';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { getOrderClientAdmin } from '../../interfaces/Response/Admin/OrderClient';
import { Variant } from '../../interfaces/Response/Cloth/Cloth';
import { GetOrders } from '../../interfaces/Response/Orders/Orders';
import { BaseService } from '../base-service';
import { ClothSimple, SamplerSimple } from '../orders/OrderService';

export interface payloadCart {
  cloths: itemCart[];
  samplers: itemSampler[];
}

export interface itemCart {
  variant: string;
  amount: number;
}

export interface itemSampler {
  sampler: string;
  amount: number;
}

export interface GetPricesResponse {
  product: string;
  amount: number;
  sellPrice: number;
  totalSellPrice: number;
  priceNormal: number;
  totalSellPriceNormal: number;
  discount: number;
}

export type CalculateInventory = {
  cloths: ClothSimple[];
  samplers: SamplerSimple[];
};

const baseService = new BaseService();

export const getPricesCart = (payload: payloadCart) => {
  return baseService.PostRequest<payloadCart, GetPricesResponse[]>(
    payload,
    'payment/calculate/price/product'
  );
};

export const getAllOrder = (
  size = 20,
  orderStatus = ''
): Promise<PaginationResponse<GetOrders[]> | null> => {
  return baseService.GetRequest<PaginationResponse<GetOrders[]>>(
    `order?filterDate=ALWAYS&page=1&size=${size}&typeSort=DESC${
      orderStatus ? `&orderStatus=${orderStatus} ` : ''
    }`
  );
};

export const getAllOrderId = (
  id: string,
  size = 1000
): Promise<PaginationResponse<getOrderClientAdmin[]> | null> => {
  return baseService.GetRequest<PaginationResponse<getOrderClientAdmin[]>>(
    `order/client/${id}?filterDate=ALWAYS&page=1&typeSort=DESC&size=${size}`
  );
};

export const editOrderAdmin = (
  id: string,
  orderStatus: string
): Promise<{ data: getOrderClientAdmin; message: string } | null> => {
  return baseService
    .PutRequest<any, any>({ order: id, orderStatus }, `order`)
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const getOrderById = (
  order: string
): Promise<getOrderClientAdmin | null> => {
  return baseService.GetRequest<getOrderClientAdmin>(`order/${order}`);
};

export const calculateInventory = (req: CalculateInventory) =>
  baseService
    .PostRequest<CalculateInventory, boolean>(
      req,
      '/payment/calculate/inventory/product'
    )
    .catch(e => {
      toast.error(
        'No contamos con el inventario suficiente para continuar con tu compra.'
      );
      return e;
    });
