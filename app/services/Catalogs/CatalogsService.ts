import { PaginationResponse } from '../../interfaces/paginationResponse';
import { BaseService } from '../base-service';
import { CatalogStatus } from '../../interfaces/Response/Catalog/Catalog';

const baseService = new BaseService();
export const getStatusCatalog = (
  page = 1,
  size = 100
): Promise<PaginationResponse<CatalogStatus[]> | null> => {
  return baseService.GetRequest<PaginationResponse<CatalogStatus[]>>(
    `catalog/order/status?page=${page}&size=${size}`
  );
};

export const getPaymentMethod = (
  page = 1,
  size = 100
): Promise<PaginationResponse<CatalogStatus[]> | null> => {
  return baseService.GetRequest<PaginationResponse<CatalogStatus[]>>(
    `catalog/payment/method?page=${page}&size=${size}`
  );
};

export const getDeliveryMethod = (
  page = 1,
  size = 100
): Promise<PaginationResponse<CatalogStatus[]> | null> => {
  return baseService.GetRequest<PaginationResponse<CatalogStatus[]>>(
    `catalog/delivery/method?page=${page}&size=${size}`
  );
};
