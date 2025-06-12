import { BaseService } from '../base-service';
import { PaginationResponse } from '../../interfaces/paginationResponse';

export const getDataTable = (
  size = 1,
  page = 1,
  baseUrl: string
): Promise<PaginationResponse<any> | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<PaginationResponse<any>>(
    `${baseUrl}page=${page}&size=${size}`
  );
};
