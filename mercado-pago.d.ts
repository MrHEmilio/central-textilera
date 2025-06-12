declare global {
  class MercadoPago {
    // eslint-disable-next-line no-unused-vars
    constructor(pk: string, options?: any);
    // eslint-disable-next-line no-unused-vars
    checkout(obj: any);
  }
}
export {};
