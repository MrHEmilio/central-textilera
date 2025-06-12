/* eslint-disable no-unused-vars */
export enum SHIPPING_PROVIDERS {
  ctxSamler = 'CENTRAL_TEXTILERA_SAMPLER',
  ctx = 'CENTRAL_TEXTILERA_FREIGHTER',
  pqtx = 'PQTX',
  skydrop = 'SKYDROP',
}

export interface ShippmentProviderResponse {
  provider: string;
  serviceCode: string;
  serviceName: string;
  shippingMethod: SHIPPING_PROVIDERS;
  rateId: string;
  image?: string;
  price: number;
  date: Date;
}

export interface CalculateDelMethCont {
  key: string;
  label: string;
}
