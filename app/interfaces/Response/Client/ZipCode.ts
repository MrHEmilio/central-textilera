export interface ZipCode {
  state: string;
  municipality: string;
  suburbs: Suburb[];
  status?: number;
  data?: any;
}

export interface Suburb {
  id: string;
  name: string;
}
