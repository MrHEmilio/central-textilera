import { toast } from 'react-toastify';
import { NewAddress } from '../../interfaces/Request/Client/Address';
import { OrderShippinggetOrderClientAdmin } from '../../interfaces/Response/Admin/OrderClient';
import { CountryCode } from '../../interfaces/Response/Client/CreateClient';
import { Cloth } from '../../interfaces/Response/Cloth/Cloth';
import {
  CreateOrderResponse,
  OrderCloth
} from '../../interfaces/Response/Orders/Orders';
import { BaseService } from '../base-service';
import { addNewLetters } from '../newLetters';
import { itemSampler } from '../Order';
import { getNewsLetterStorage, getNotUserRegisterStorage } from '../utils';

export enum PaymentMethods {
  // eslint-disable-next-line no-unused-vars
  paypal = 'PAYPAL',
  // eslint-disable-next-line no-unused-vars
  mp = 'MERCADO_PAGO'
}

export interface ShippingInterface {
  clientAddress: string;
  provider: string;
  serviceCode: string;
  serviceName: string;
  price: number;
  date: Date;
  rateId: string;
  shippingMethod: string;
}
export interface CreateOrderRequest {
  cloths: ClothSimple[];
  samplers: itemSampler[];
  paymentMethod: 'PAYPAL' | 'MERCADO_PAGO';
  deliveryMethod: 'SHIPPING' | 'PICK_UP';
  shippingAddress?: string;
  billingAddress?: string;
  shipping?: ShippingInterface | undefined; // termporal
  paymentId: string;
}

export interface ClothSimple {
  variant: string;
  amount: number;
}

export interface SamplerSimple {
  sampler: string;
  amount: number;
}

export interface CreateOrderNotRegisterUserRequest {
  client: CreateOrderNotRegisterUserInfo;
  cloths: ClothSimple[];
  samplers: SamplerSimple[];
  shipping?: ShippingNotResiterUser | null;
  deliveryMethod: string;
  paymentMethod: string;
  paymentId: string;
  billingAddress?: NewAddress;
  addressSame: boolean;
  orderBilling: boolean;
}

export interface CreateOrderNotRegisterUserInfo {
  name: string;
  firstLastname: string;
  secondLastname: string;
  countryCode: string;
  phone: string;
  email: string;
  rfc?: string;
  companyName?: string;
  fiscalRegimen?: string;
}

export interface ShippingNotResiterUser {
  clientAddress: NewAddress;
  provider: string;
  serviceCode: string;
  serviceName: string;
  shippingMethod: string;
  rateId: string;
  price: number | '';
  date: Date;
}

export interface GetOrderResponse {
  id: string;
  client: GetOrderResponseClient;
  number: number;
  total: number;
  products: OrderCloth[];
  deliveryMethod: string;
  paymentMethod: string;
  statusHistory: StatusHistory[];
  orderShipping?: OrderShippinggetOrderClientAdmin;
  orderBilling?: any;
}

export interface GetOrderResponseClient {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: string;
  countryCode: CountryCode;
  phone: string;
  email: string;
  emailValidated: boolean;
  date: Date;
  rfc: string;
  companyName: string;
  fiscalRegimen: string;
  active: boolean;
}

export interface StatusHistory {
  id: string;
  status: string;
  date: Date;
}

export const createOrderService = (
  req: CreateOrderRequest
): Promise<CreateOrderResponse | null> => {
  const baseService = new BaseService();
  return baseService.PostRequest<CreateOrderRequest, CreateOrderResponse>(
    req,
    '/order'
  );
};

export const createOrderNotRegisteredUser = async (
  req: CreateOrderNotRegisterUserRequest
) => {
  const userContact = getNotUserRegisterStorage();
  const newsRegister = getNewsLetterStorage();
  if (userContact && userContact.userContact && newsRegister?.email) {
    addNewLetters(userContact.userContact.email);
  }
  return new BaseService()
    .PostRequest<CreateOrderNotRegisterUserRequest, CreateOrderResponse>(
      req,
      '/order/without/account'
    )
    .catch(r => {
      toast.error(r.message || 'Hubo un error, intente de nuevo más tarde');
      return null;
    });
};
