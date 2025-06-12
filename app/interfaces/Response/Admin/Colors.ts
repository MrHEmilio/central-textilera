export interface ResponseColors {
  data: DataColors;
  message: string;
}

export interface DataColors {
  id: string;
  name: string;
  code: string;
  active: boolean;
}
