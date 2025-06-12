import { NewAddress } from '../../../interfaces/Request/Client/Address';
import { Address } from '../../../interfaces/Response/Client/NewDirection';
import { Sampler } from '../../../interfaces/Response/Cloth/Cloth';
import { ShippmentProviderResponse } from '../../../interfaces/Response/Shippment';
import { itemCartFull, itemCartPrices } from '../../../interfaces/State/Cart';
import { CfdiCatalogResponse } from '../../Billing/billing-responses';
export interface CtxActions<> {
  type: string;
  payload?: any;
}
export const CartActions = (data: itemCartFull): CtxActions => ({
  type: 'addItem',
  payload: data
});

export const incrementQuantityCart = (data: itemCartFull): CtxActions => ({
  type: 'incremenQuantity',
  payload: data
});

export const deleteItemCart = (data: itemCartFull): CtxActions => ({
  type: 'deleteItemCart',
  payload: data
});

export const deleteSamplerCart = (data: itemCartFull): CtxActions => ({
  type: 'deleteSamplerCart',
  payload: data
});

export const changeSamplerQuantity = (data: {
  id: string;
  quantity: number;
}): CtxActions => ({
  type: 'changeQuantitySampler',
  payload: data
});

export const setItemsCart = (data: itemCartFull[]): CtxActions => ({
  type: 'setItemsCart',
  payload: data
});

export const setSamplersCart = (data: Sampler[]): CtxActions => ({
  type: 'setSamplersCart',
  payload: data
});

export const setQuantityCart = (data: itemCartFull): CtxActions => ({
  type: 'setQuantityCart',
  payload: data
});

export const setPricesCart = (data: itemCartPrices): CtxActions => ({
  type: 'setPricesCart',
  payload: data
});

export const loadingPrices = (data: boolean): CtxActions => ({
  type: 'loadingPrices',
  payload: data
});

export const setCartLocation = (data: NewAddress): CtxActions => ({
  type: 'selectedLocation',
  payload: data
});

export const setShippingInfo = (
  data: ShippmentProviderResponse
): CtxActions => ({
  payload: data,
  type: 'setShippingInfo'
});

export const saveCartOnStorage = (): CtxActions => ({
  type: 'saveOnStorage',
  payload: null
});

export const clearCart = (): CtxActions => ({
  type: 'clearCart',
  payload: null
});

export const setTaxLocationState = (taxLocation: NewAddress): CtxActions => ({
  type: 'setTaxLocation',
  payload: taxLocation
});

export const setDeliveryMethod = (
  method: 'SHIPPING' | 'PICK_UP'
): CtxActions => ({
  type: 'setDeliveryMethod',
  payload: method
});

export const addSamplerToCart = (
  sampler: Sampler,
  quantity: number,
  clothName: string
): CtxActions => ({
  type: 'addSampler',
  payload: { ...sampler, quantity, clothName }
});

export const clearShippingInfo = (): CtxActions => ({
  type: 'clearShippingInfo',
  payload: null
});

export const setRequiresTax = (requires: boolean): CtxActions => ({
  type: 'setRequiresTax',
  payload: requires
});

export const setCfdiCart = (cfdi: CfdiCatalogResponse): CtxActions => ({
  type: 'setCfdiUse',
  payload: cfdi
});

export const resetCartOnPayment = (): CtxActions => ({
  type: 'resetForPayment',
  payload: null
});
