export interface EditClientAdminResponse {
  data: DataEditClientAdmin;
  message: string;
}

export interface DataEditClientAdmin {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: string;
  countryCode: CountryCodeEditClientAdmin;
  phone: string;
  email: string;
  emailValidated: boolean;
  active: boolean;
}

export interface CountryCodeEditClientAdmin {
  id: string;
  name: string;
  code: string;
}

export interface VerifyClientExistsResponse {
  ok: boolean;
  dataWrong: string[];
}
