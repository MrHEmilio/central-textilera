import { OrderByEnum } from '../../models';

export interface IFabricsFilters {
  orderBy: OrderByEnum;
  uses: string;
  fibers: string;
  collections: string;
  sales: string;
  columnSort: string;
  loading: boolean;
}
export interface IFabricsReducer {
  filters: IFabricsFilters;
}
