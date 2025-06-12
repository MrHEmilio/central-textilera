export interface ResponseCreateFiber {
  data: DataFiber;
  message: string;
}

export interface DataFiber {
  id: string;
  name: string;
  active: boolean;
}
