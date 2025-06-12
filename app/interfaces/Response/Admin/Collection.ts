export interface ResponseCollectionAdmin {
  data: DataCollection;
  message: string;
}

export interface DataCollection {
  id: string;
  name: string;
  image: string;
  active: boolean;
}
