export interface RequestCreateFreighter {
    name: string;
    image: string;
    costPerDistance: number;
    costPerWeight: number;
  }
  
export interface RequestUpdateFreighter {
    id: string;
    name: string;
    costPerDistance: number;
    costPerWeight: number;
}