import { NewAddress } from "../Request/Client/Address";

export interface NotRegisterUserInfo {
  name: string;
  lastName: string;
  secondLastName?: string;
  email: string;
  phone: string;
  countryCode: string;
}

export interface NotRegisterUserInfoMercado {
  name: string;
  firstLastname: string;
  secondLastName?: string;
  email: string;
  phone: string;
  countryCode: string;
}

export interface NotRegisterUserAddress {
  streetName: string;
  numExt: string;
  numInt: string;
  zipCode: string;
  suburb: string;
  company?: string;
  same: boolean;
  latitude: string;
  longitude: string;
}

export interface NotRegisterUserState {
  validate: boolean;
  isContactValid: boolean;
  isAddressValid: boolean;
  validatedUser?: boolean;
  userContact?: NotRegisterUserInfo;
  userAddress?: NewAddress;
  taxAddress?: NewAddress;
  billingInfo?: NotRegisterUserBillingInfo;
  fiscalRegimen?: string;
}

export interface NotRegisterUserBillingInfo {
  rfc: string;
  companyName: string;
}
