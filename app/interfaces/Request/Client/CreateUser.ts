export interface CreateUser {
  name: string;
  firstLastname: string;
  secondLastname: string;
  phone?: string;
  email: string;
  password: string;
  rfc?: string;
  fiscalRegimen?: string;
  companyName?: string;
}

export interface UpdateClient {
  id?: string;
  name: string;
  firstLastname: string;
  secondLastname: string;
  countryCode: string;
  phone: string;
  rfc: string;
  companyName: string;
  fiscalRegimen: string;
}
