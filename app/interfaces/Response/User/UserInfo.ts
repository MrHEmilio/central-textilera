export interface UserInfo {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: null;
  phone: string;
  email: string;
  emailValidated: boolean;
  active: boolean;
  rfc?: string;
  companyName?: string;
  fiscalRegimen?: string;
  countryCode: {
    code: string;
    id: string;
    name: string;
  };
}
