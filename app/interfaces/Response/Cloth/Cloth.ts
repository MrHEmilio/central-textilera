import { BillingProduct } from '../../Request/Cloth/AddClothRequest';

export interface Cloth {
  id?: string;
  image: string;
  name: string;
  nameUrl: string;
  mainDescription: string;
  descriptions: Description[];
  fiber: Fiber;
  sale: Sale;
  variants: Variant[];
  collections: Fiber[];
  measure?: Measure;
  sampler: Sampler;
  uses: Fiber[];
  prices: Price[];
  active: boolean;
  billing: BillingProduct;
}
export interface OrcerCloth {
  id: string;
  image: string;
  name: string;
  color: string;
  sale: string;
  amount: number;
  sellPrice: number;
  totalSellPrice: number;
  discount: number;
}

export interface Measure {
  id: string;
  dimension: number;
  width: number;
  weight: number;
  yieldPerKilo: number;
  averagePerRoll: number;
}

export interface Fiber {
  id: string;
  name: string;
  image?: string;
  active: boolean;
  code?: string;
}

export interface Sampler {
  id: string;
  image: string;
  description: string;
  price: number;
  quantity?: number;
  amount?: number;
  clothName?: string;
  nameUrl?: string;
  billing: BillingProduct;
}

interface Description {
  name: string;
  automatic: boolean;
}

export interface Price {
  id: string;
  firstAmountRange: number;
  lastAmountRange: number | null;
  price: number;
  active?: boolean;
}

export interface Variant {
  id: string;
  color: Fiber;
  amount: number;
  active?: boolean;
}

export interface Sale {
  active: boolean;
  id: string;
  name: string;
  abbreviation: string;
}
