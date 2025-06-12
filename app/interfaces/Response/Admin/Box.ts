export interface ResponseCreateBox {
  data: DataBox;
  message: string;
}

export interface DataBox {
  id: string;
  name: string;
  width: number;
  height: number;
  depht: number;
  colorCode: string;
  active: true;
}

export interface BoxCaculateResponse {
  clothName: string;
  colorName: string;
  boxes: Box[];
}

export interface Box {
  amount: number;
  boxes:  Boxes;
}

export interface Boxes {
  id:        string;
  name:      string;
  width:     number;
  height:    number;
  depth:     number;
  colorCode: string;
  active:    boolean;
}

