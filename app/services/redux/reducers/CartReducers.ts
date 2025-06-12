import { Reducer } from 'react';
import { AnyAction } from 'redux';
import { Sampler } from '../../../interfaces/Response/Cloth/Cloth';
import {
  CartState,
  itemCartFull,
  itemCartPrices
} from '../../../interfaces/State/Cart';

const initial = () => {
  const r = typeof window == 'undefined' ? null : localStorage.getItem('cart');
  if (!r)
    return {
      products: [],
      deliveryMethod: 'SHIPPING',
      loading: false,
      location: undefined,
      samplers: [],
      taxLocation: undefined,
      shippingInfo: undefined,
      requiresTax: false,
      cfdiUse: undefined,
      cfdi: undefined
    };

  return JSON.parse(r);
};

const saveStorageObj = (obj: unknown) => {
  if (typeof window !== undefined) {
    const str = JSON.stringify(obj);
    localStorage.setItem('cart', str);
  }
};
export const CartReducer: Reducer<CartState | undefined, AnyAction> = (
  state = initial(),
  { payload, type }: AnyAction
) => {
  switch (type) {
    case 'addItem':
      saveStorageObj({ ...state, products: [...state.products, payload] });
      return { ...state, products: [...state.products, payload] };
    case 'incremenQuantity':
      saveStorageObj({
        ...state,
        products: [
          ...state.products.map((item: itemCartFull) => {
            if (item.variant === payload.variant) {
              return { ...item, quantity: item.quantity + payload.quantity };
            } else {
              return item;
            }
          })
        ]
      });
      return {
        ...state,
        products: [
          ...state.products.map((item: itemCartFull) => {
            if (item.variant === payload.variant) {
              return { ...item, quantity: item.quantity + payload.quantity };
            } else {
              return item;
            }
          })
        ]
      };

    case 'deleteItemCart':
      saveStorageObj({
        ...state,
        products: [
          ...state.products.filter(
            (product: itemCartFull) => product.variant !== payload.variant
          )
        ]
      });
      return {
        ...state,
        products: [
          ...state.products.filter(
            (product: itemCartFull) => product.variant !== payload.variant
          )
        ]
      };

    case 'deleteSamplerCart':
      saveStorageObj({
        ...state,
        samplers: [
          ...state.samplers.filter((sam: Sampler) => sam.id !== payload.variant)
        ]
      });
      return {
        ...state,
        samplers: [
          ...state.samplers.filter((sam: Sampler) => sam.id !== payload.variant)
        ]
      };
    case 'setItemsCart':
      saveStorageObj({ ...state, products: [...payload] });
      return { ...state, products: [...payload] };

    case 'setSamplersCart':
      saveStorageObj({ ...state, samplers: [...payload] });
      return { ...state, samplers: [...payload] };

    case 'changeQuantitySampler':
      saveStorageObj({
        ...state,
        samplers: [
          ...state.samplers.map((sam: Sampler) => {
            if (sam.id === payload.id) {
              return { ...sam, quantity: payload.quantity };
            } else {
              return sam;
            }
          })
        ]
      });
      return {
        ...state,
        samplers: [
          ...state.samplers.map((sam: Sampler) => {
            if (sam.id === payload.id) {
              return { ...sam, quantity: payload.quantity };
            } else {
              return sam;
            }
          })
        ]
      };

    case 'setQuantityCart':
      saveStorageObj({
        ...state,
        products: [
          ...state.products.map((item: itemCartFull) => {
            if (item.variant === payload.variant) {
              return { ...item, quantity: payload.quantity };
            } else {
              return item;
            }
          })
        ]
      });
      return {
        ...state,
        products: [
          ...state.products.map((item: itemCartFull) => {
            if (item.variant === payload.variant) {
              return { ...item, quantity: payload.quantity };
            } else {
              return item;
            }
          })
        ]
      };

    case 'setPricesCart':
      saveStorageObj({
        ...state,
        products: [
          ...state.products.map((item: itemCartFull) => {
            const prices: itemCartPrices = payload.find(
              (itm: itemCartPrices) => item.variant === itm.product
            );

            // delete prices?.amount;

            return { ...item, ...prices };
          })
        ],
        samplers: [
          ...state.samplers.map(item => {
            const prices: itemCartPrices = payload.find(
              (itm: itemCartPrices) => item.id === itm.product
            );

            // delete prices?.amount;

            return { ...item, ...prices, price: prices.sellPrice };
          })
        ]
      });
      return {
        ...state,
        products: [
          ...state.products.map((item: itemCartFull) => {
            const prices: itemCartPrices = payload.find(
              (itm: itemCartPrices) => item.variant === itm.product
            );

            // delete prices?.amount;

            return { ...item, ...prices };
          })
        ],
        samplers: [
          ...state.samplers.map(item => {
            const prices: itemCartPrices = payload.find(
              (itm: itemCartPrices) => item.id === itm.product
            );

            // delete prices?.amount;

            return { ...item, ...prices, price: prices.sellPrice };
          })
        ]
      };

    case 'loadingPrices':
      return { ...state, loading: payload };
    case 'selectedLocation':
      saveStorageObj({ ...state, location: payload });
      return { ...state, location: payload };
    case 'saveOnStorage':
      saveStorageObj(state);
      return state;
    case 'clearCart':
      saveStorageObj({ ...state, products: [], samplers: [] });
      return { ...state, products: [], samplers: [] };
    case 'setTaxLocation':
      saveStorageObj({ ...state, taxLocation: payload });
      return { ...state, taxLocation: payload };
    case 'setShippingInfo':
      saveStorageObj({ ...state, shippingInfo: payload });
      return { ...state, shippingInfo: payload };
    case 'clearShippingInfo':
      saveStorageObj({ ...state, shippingInfo: {} });
      return { ...state, shippingInfo: {} };
    case 'setDeliveryMethod':
      saveStorageObj({ ...state, deliveryMethod: payload });
      return { ...state, deliveryMethod: payload };
    case 'addSampler':
      // eslint-disable-next-line no-case-declarations
      const exists: number = state.samplers.findIndex(
        (i: Sampler) => i.id === payload.id
      );
      if (exists === -1) {
        saveStorageObj({ ...state, samplers: [...state.samplers, payload] });
        return { ...state, samplers: [...state.samplers, payload] };
      } else {
        saveStorageObj({
          ...state,
          samplers: [
            state.samplers.map((i: Sampler) => {
              if (i.id === payload.id) {
                return { ...i, quantity: i.quantity + payload.quantity };
              } else {
                return i;
              }
            })[0]
          ]
        });
        return {
          ...state,
          samplers: [
            state.samplers.map((i: Sampler) => {
              if (i.id === payload.id) {
                return { ...i, quantity: i.quantity + payload.quantity };
              } else {
                return i;
              }
            })[0]
          ]
        };
      }
    case 'setRequiresTax':
      saveStorageObj({ ...state, requiresTax: payload });
      return { ...state, requiresTax: payload };
    case 'setCfdiUse':
      saveStorageObj({ ...state, cfdi: payload });
      return { ...state, cfdi: payload };
    case 'resetForPayment':
      saveStorageObj({
        ...state,
        deliveryMethod: 'SHIPPING',
        location: undefined,
        taxLocation: undefined,
        shippingInfo: undefined,
        requiresTax: false,
        cfdiUse: undefined,
        cfdi: undefined
      });
      return {
        ...state,
        deliveryMethod: 'SHIPPING',
        location: undefined,
        taxLocation: undefined,
        shippingInfo: undefined,
        requiresTax: false,
        cfdiUse: undefined,
        cfdi: undefined
      };
    default:
      return state;
  }
};
