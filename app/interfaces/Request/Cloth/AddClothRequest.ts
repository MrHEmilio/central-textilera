export interface AddClothPost {
  id?: string;
  name: string;
  image?: string;
  mainDescription: string;
  sampler: SamplerClothReq;
  measure: Measure;
  descriptions?: string[];
  fiber: string;
  sale: string;
  variants: VariantClothReq[];
  collections: string[];
  uses: string[];
  prices: PriceClothReq[];
  active?: boolean;
  billing: BillingProduct;
}

export interface BillingProduct {
  id?: string;
  productCode: string;
  productLabel: string;
  unitCode: string;
  unitLabel: string;
}

export interface Measure {
  id?: string;
  dimension: number;
  width: number;
  weight: number;
  yieldPerKilo?: number | null;
  averagePerRoll: number;
}

export interface PriceClothReq {
  id?: string;
  firstAmountRange: number;
  lastAmountRange: number | null;
  order: number;
  price: number;
  active?: boolean;
}

export interface SamplerClothReq {
  id?: string;
  description: string;
  price: number;
  amount?: number;
  billing: BillingProduct;
}

export interface VariantClothReq {
  id?: string;
  color: string;
  amount: number;
  active?: boolean;
}

export interface AddClothPostResponse {
  data: AddClothPost & { nameUrl: string };
  message: string;
}
