export interface ResponseCreateUse {
  data: DataUse;
  message: string;
}

export interface DataUse {
  id: string;
  name: string;
  active: boolean;
}
