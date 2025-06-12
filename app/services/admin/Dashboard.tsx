import { BaseService } from '../base-service';
import { PaginationResponse } from '../../interfaces/paginationResponse';

const baseService = new BaseService();

export const getReportSale = (
  filterDate: string,
  dateStart: string,
  dateEnd: string
): Promise<{ total: number } | null> => {
  return baseService.GetRequest<{ total: number }>(
    `/report/sale?filterDate=${filterDate}${
      filterDate === 'RANGE' ? `&dateStart=${dateStart}&dateEnd=${dateEnd}` : ''
    }`
  );
};

export const getReportClient = (): Promise<{ total: number } | null> => {
  return baseService.GetRequest<{ total: number }>(`/report/client`);
};

export const getReportCloth = (): Promise<{ total: number } | null> => {
  return baseService.GetRequest<{ total: number }>(`/report/cloth`);
};

export const getClothSoldOut = (): Promise<PaginationResponse<any> | null> => {
  return baseService.GetRequest<PaginationResponse<any>>(
    `/report/cloth/sold/out?page=1&size=1&filterDate=ALWAYS`
  );
};
