import { ClothSimple } from '../../../services';
import { itemSampler } from '../../../services/Order';
import {
  NotRegisterUserInfoMercado
} from '../../State/NotRegisterUserInterface';
import { NewAddress } from '../Client/Address';

export interface PaymentCheckoutRequest {
  cloths: ClothSimple[];
  samplers: itemSampler[];
  client: NotRegisterUserInfoMercado;
  shippingPrice: number;
  clientAddress: NewAddress;
}

export interface PaymentCalculatePriceRequest {
  cloths: ClothSimple[];
  samplers: itemSampler[];
  zipCode: string;
  latitude: string;
  longitude: string;
}

export interface PaymentCalculateMethod {
  cloths: ClothSimple[];
}

export interface PaymentCalculatePriceShippingFullRequest {
  latitude: string;
  longitude: string;
  cloths: ClothSimple[];
  samplers: itemSampler[];
  address: AddressFullShipping;
  clientName: string;
  phone: string;
  email: string;
}

export interface AddressFullShipping {
  streetName: string;
  numExt: string;
  numInt: string;
  suburb: string;
  zipCode: string;
  municipality: string;
  city: string;
  state: string;
  country: string;
  references: string;
  latitude: string;
  longitude: string;
}

