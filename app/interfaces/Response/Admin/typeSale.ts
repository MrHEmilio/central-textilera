export interface ResponseTypeSale {
  data: DataTypeSale;
  message: string;
}

export interface DataTypeSale {
  id: string;
  name: string;
  active: boolean;
  abbreviation: string;
}
