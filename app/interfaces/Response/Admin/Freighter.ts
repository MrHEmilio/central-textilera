export interface ResponseNewFreighter {
    data: dataFreighter;
    message: string;
}

export interface dataFreighter {
    id: string;
    name: string;
    image: string;
    costPerDistance: number;
    costPerWeight: number;
    active: boolean;
}