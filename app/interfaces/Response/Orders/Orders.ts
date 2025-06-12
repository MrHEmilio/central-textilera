import { OrderShippinggetOrderClientAdmin } from "../Admin/OrderClient";

export interface GetOrders {
  id: string;
  client: Client;
  total: number;
  products: OrderCloth[];
  orderShipping: OrderShippinggetOrderClientAdmin;
  orderBilling: OrderBilling;
  paymentMethod: string;
  deliveryMethod: string
  status: string;
}

export interface Client {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: string;
  phone: string;
  email: string;
  emailValidated: boolean;
  active: boolean;
}

export interface OrderBilling {
  id: string;
  clientAddress: ClientAddress;
}

export interface ClientAddress {
  id: string;
  name: string;
  company: string;
  streetName: string;
  numExt: string;
  numInt: string;
  address: Address;
  predetermined: boolean;
  active: boolean;
}

export interface Address {
  state: string;
  zipCode: string;
  name: string;
  municipality: string;
}

export interface OrderCloth {
  amount: number;
  color: string;
  id: string;
  image: string;
  name: string;
  sale: string;
  sellPrice: number;
}

export interface Cloth {
  id: string;
  image: string;
  name: string;
  mainDescription: string;
  descriptions: Description[];
  fiber: Fiber;
  sale: Fiber;
  variants: Variant[];
  collections: Fiber[];
  uses: Fiber[];
  prices: Price[];
  active: boolean;
}

export interface Fiber {
  id: string;
  name: string;
  image?: string;
  active: boolean;
  code?: string;
}

export interface Description {
  id: string;
  name: string;
}

export interface Price {
  id: string;
  firstAmountRange: number;
  lastAmountRange: number;
  price: number;
}

export interface Variant {
  id: string;
  color: Fiber;
  amount: number;
}

export interface OrderShipping {
  id?: string;
  clientAddress?: ClientAddress;
  shippingPrice?: number;
  type?: string;
}

export interface CreateOrderResponse {
  data: OrderDetail;
  message: string;
}

export interface OrderDetail {
  id: string;
  client: Client;
  total: number;
  products: OrderCloth[];
  orderShipping: OrderShippinggetOrderClientAdmin;
  orderBilling: OrderBilling;
  paymentMethod: string;
  statusHistory: StatusHistory[];
  status: string;
}

export interface StatusHistory {
  id: string;
  status: string;
  date: Date;
}
