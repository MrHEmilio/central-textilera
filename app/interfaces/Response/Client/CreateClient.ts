export interface CreateClientResponse {
  data: SingleUserInfo;
  message: string;
}

export interface SingleUserInfo {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: string;
  phone: string;
  email: string;
  emailValidated: boolean;
  active: boolean;
  rfc?: string;
  companyName?: string;
  fiscalRegimen?: string;
}

export interface UpdateClientResponse {
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

export interface CountryCode {
  id: string;
  name: string;
  code: string;
}
