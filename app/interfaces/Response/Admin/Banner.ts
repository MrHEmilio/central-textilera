export interface ResponseCreateBanner {
  data: DataBanner;
  message: string;
}

export interface DataBanner {
  id: string;
  image: string;
  description: string;
  active: boolean;
  waitTime: number;
}
