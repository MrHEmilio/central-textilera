import { SHIPPING_PROVIDERS } from "../Shippment";

export interface getOrderClientAdmin {
  id: string;
  client: ClientgetOrderClientAdmin;
  number: number;
  total: number;
  products: OrderClothgetOrderClientAdmin[];
  deliveryMethod: string;
  paymentMethod: string;
  statusHistory: StatusHistorygetOrderClientAdmin[];
  orderShipping?: OrderShippinggetOrderClientAdmin;
  orderBilling: OrderBillinggetOrderClientAdmin;
}

export interface ClientgetOrderClientAdmin {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: null;
  countryCode: CountryCodegetOrderClientAdmin;
  phone: string;
  email: string;
  emailValidated: boolean;
  active: boolean;
  rfc?: string;
  companyName?: string;
  fiscalRegimen?: string;
}

export interface CountryCodegetOrderClientAdmin {
  id: string;
  name: string;
  code: string;
  active?: boolean;
}

export interface OrderBillinggetOrderClientAdmin {
  id: string;
  clientAddress: ClientAddressgetOrderClientAdmin;
}

export interface ClientAddressgetOrderClientAdmin {
  id: string;
  name: string;
  company: null;
  streetName: string;
  numExt: string;
  numInt: null;
  address: AddressgetOrderClientAdmin;
  predetermined: boolean;
  active: boolean;
}

export interface AddressgetOrderClientAdmin {
  state: string;
  zipCode: string;
  name: string;
  municipality: string;
}

export interface OrderClothgetOrderClientAdmin {
  amount: number;
  color: string;
  id: string;
  image: string;
  name: string;
  sale: string;
  sellPrice: number;
}

export interface ClothgetOrderClientAdmin {
  id: string;
  image: string;
  name: string;
  mainDescription: string;
  sampler: SamplergetOrderClientAdmin;
  measure: MeasuregetOrderClientAdmin;
  descriptions: DescriptiongetOrderClientAdmin[];
  fiber: FibergetOrderClientAdmin;
  sale: FibergetOrderClientAdmin;
  variants: VariantgetOrderClientAdmin[];
  collections: FibergetOrderClientAdmin[];
  uses: FibergetOrderClientAdmin[];
  prices: PricegetOrderClientAdmin[];
  active: boolean;
}

export interface FibergetOrderClientAdmin {
  id: string;
  name: string;
  image?: string;
  active: boolean;
}

export interface DescriptiongetOrderClientAdmin {
  id: string;
  name: string;
}

export interface MeasuregetOrderClientAdmin {
  id: string;
  dimension: number;
  width: number;
  weight: number;
  yieldPerKilo: null;
}

export interface PricegetOrderClientAdmin {
  id: string;
  firstAmountRange: number;
  lastAmountRange: number | null;
  price: number;
}

export interface SamplergetOrderClientAdmin {
  id: string;
  image: string;
  description: string;
  price: number;
}

export interface VariantgetOrderClientAdmin {
  id: string;
  color: CountryCodegetOrderClientAdmin;
  amount: number;
}

export interface OrderShippinggetOrderClientAdmin {
  id: string;
  company: string | null;
  streetName: string;
  numExt: string;
  numInt: string | null;
  zipCode: string;
  suburb: string;
  municipality: string;
  state: string;
  provider: string;
  serviceCode: string;
  serviceName: string;
  price: number;
  date: Date;
  shippingMethod?: SHIPPING_PROVIDERS;
  trackingNumber?: string;
  trackingUrlProvider?: string;
}

export interface StatusHistorygetOrderClientAdmin {
  id: string;
  status: string;
  date: Date;
}
