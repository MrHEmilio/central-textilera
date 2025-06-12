import { NewAddress } from "../../interfaces/Request/Client/Address";

export interface GenerateBillingRequest {
  order: string;
  cfdiUse: string;
}

export interface BillinWithoutAccountReq {
  order: string;
  cfdiUse: string;
  rfc: string;
  companyName: string;
  fiscalRegimen: string;
  billingAddress: NewAddress;
}

export interface BillingAddress {
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

export interface BillingWithOutAccountRequest {
  order: string;
  cfdiUse: string;
  rfc: string;
  companyName: string;
  fiscalRegimen: string;
  billingAddress: NewAddress;
}
