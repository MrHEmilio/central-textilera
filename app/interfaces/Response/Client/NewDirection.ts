import { NewAddress } from '../../Request/Client/Address';

export interface NewDirection {
  data: NewAddress;
  message: string;
}

export interface Address {
  id: string;
  name: string;
  company: string;
  streetName: string;
  numExt: string;
  numInt: string;
  address?: AddressZipCode;
  predetermined: boolean;
  billingAddress: boolean;
  active: boolean;
}

export interface AddressZipCode {
  state: string;
  zipCode: string;
  name: string;
  municipality: string;
  id: string;
}
