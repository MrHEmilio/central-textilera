export interface CreateBanner {
  description: string;
  image?: any;
  waitTime: number;
}

export interface EditBannerInterface {
  id: string;
  waitTime: number;
  description: string;
}
