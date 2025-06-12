export interface ResponseAdminClient {
  role: string;
  info: InfoClientAdmin;
  permission: string[];
  menus: Menu[];
}

export interface InfoClientAdmin {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: string;
  countryCode: CountryCode;
  phone: string;
  email: string;
  emailValidated: boolean;
  rfc?: string;
  fiscalRegimen?: string;
  companyName?: string;
  active: boolean;
}

export interface CountryCode {
  id: string;
  name: string;
  code: string;
}

export interface Menu {
  name: string;
  icon: string;
  path: string;
}

export interface CountryCode {
  id: string;
  name: string;
  code: string;
}
