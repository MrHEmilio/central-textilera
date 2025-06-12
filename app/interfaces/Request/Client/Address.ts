export interface NewAddress {
  id?: string;
  name?: string;
  lastName?: string;
  streetName: string;
  numExt: string;
  numInt: string;
  company?: string;
  zipCode?: string;
  city?: string;
  suburb: string;
  cellPhone?: string;
  predetermined?: boolean;
  billingAddress?: boolean;
  state?: string;
  municipality?: string;
  country?: string;
  latitude?: number;
  longitude?: number;
  references?: string;
  active?: boolean;
}
