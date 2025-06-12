export interface CtxActions<> {
  type: string;
  payload?: any;
}
export const EditDirectionsActions = (data: any): CtxActions => ({
  type: 'editDirection',
  payload: data
});
