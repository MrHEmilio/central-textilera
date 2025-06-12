import { Price, Sale } from '../Cloth/Cloth';

export interface Collection {
  id: string;
  name: string;
  nameUrl: string;
  image: string;
  mainDescription?: string;
  active?: boolean;
  prices?: Price[];
  descriptions?: Description[];
  variants: Variant[];
  sale: Sale;
}

interface Description {
  name: string;
  automatic: boolean;
}

interface Variant {
  id: string;
  color: Fiber;
  amount: number;
}
export interface Fiber {
  id: string;
  name: string;
  image?: string;
  active: boolean;
  code?: string;
}
