import type { MenuProps } from 'antd';
import { itemCartFull } from '../interfaces/State/Cart';
import { NotRegisterUserState } from '../interfaces/State/NotRegisterUserInterface';
import { MonthsOfYear } from '../models/Enums';
import { authRedirect } from './auth/authService';
import CtxAxios from './interceptor';
import {
  setCartLocation,
  setItemsCart,
  setSamplersCart,
  setShippingInfo
} from './redux/actions/CartActions';
import { LogOut, SessionActions } from './redux/actions/SessionActions';
import { SessionReducerT } from './redux/reducers';
import { NewsLetterState } from './redux/reducers/NewsLetterReducer';
import { ReduxStore } from './redux/store';

type MenuItem = Required<MenuProps>['items'][number];

export const getItem = (
  label: React.ReactNode,
  key: React.Key,
  icon?: React.ReactNode,
  onClick?: any,
  children?: MenuItem[],
  type?: 'group'
): MenuItem => {
  return {
    key,
    icon,
    children,
    label,
    type,
    onClick
  } as MenuItem;
};

export const formateNameProduct = (name: string): string => {
  const lastPartUrl = encodeURI(name)
    .replace('(', '%28')
    .replace(')', '%29')
    .replaceAll('"', '%22')
    .replaceAll('"', '');

  return `products/${lastPartUrl}`;
};

export const staticPathsProduct = (name: string): string => {
  const lastPartUrl = encodeURI(name)
    .replace('(', '%28')
    .replace(')', '%29')
    .replaceAll('"', '%22')
    .replaceAll('"', '');

  return lastPartUrl;
};

export const inicialConfigurations = async () => {
  let session: string | null = null;
  let info: string | null = null;
  let shoopingCart: any | null = null;
  let res: any = undefined;

  if (typeof window !== 'undefined') {
    session = localStorage.getItem('session');
    info = localStorage.getItem('info');
    shoopingCart = getShoppingCart();
  }
  if (shoopingCart?.products) {
    const { products } = shoopingCart;
    ReduxStore.dispatch(setItemsCart(products));
  }
  if (shoopingCart?.samplers) {
    const { samplers } = shoopingCart;
    ReduxStore.dispatch(setSamplersCart(samplers || []));
  }
  if (shoopingCart?.shippingInfo) {
    const { shippingInfo } = shoopingCart;
    ReduxStore.dispatch(setShippingInfo(shippingInfo));
  }
  if (shoopingCart?.location) {
    const { location } = shoopingCart;
    ReduxStore.dispatch(setCartLocation(location));
  }
  if (session && info) {
    const user: any = JSON.parse(info);
    CtxAxios.defaults.headers.common['x-auth-token'] = session;
    if (user.root || user.role === 'ADMIN') {
      res = await authRedirect('/admin/info');
    } else {
      res = await authRedirect('/client/info');
    }
    if (!res) {
      ReduxStore.dispatch(LogOut());
      if (typeof window !== 'undefined') {
        localStorage.removeItem('info');
        localStorage.removeItem('session');
      }
      return;
    }
    ReduxStore.dispatch(SessionActions(res));
  }
};

export const setShoppingCart = () => {
  const { cart } = ReduxStore.getState();

  if (!cart) return;
  const cartSaveinLocal = {
    products: [
      ...cart.products.map(
        ({ img, variant, quantity, nameColor, nameTela }: itemCartFull) => {
          return {
            img,
            variant,
            quantity,
            nameColor,
            nameTela
          };
        }
      )
    ],
    samplers: [...cart.samplers]
  };
  localStorage.setItem('cart', JSON.stringify(cartSaveinLocal));
};

export const getShoppingCart = () => {
  const cart = localStorage.getItem('cart');
  return JSON.parse(cart || '{}');
};

export const getNotUserRegisterStorage = (): NotRegisterUserState => {
  if (typeof window === 'undefined')
    return {
      validate: false,
      isContactValid: false,
      isAddressValid: false,
      validatedUser: false
    };
  const info = localStorage.getItem('notRegisterUser');
  if (!info)
    return {
      validate: false,
      isContactValid: false,
      isAddressValid: false,
      validatedUser: false
    };
  return JSON.parse(info) as NotRegisterUserState;
};

export const getNewsLetterStorage = (): NewsLetterState | undefined => {
  if (typeof window === 'undefined') return undefined;
  const val = localStorage.getItem('newsLetterSave');
  if (!val) return undefined;
  return JSON.parse(val) as NewsLetterState;
};

export const saveNotRegisterStorage = (user: NotRegisterUserState): void => {
  localStorage.setItem('notRegisterUser', JSON.stringify(user));
};

export const deleteNotRegisterUserStorage = () => {
  localStorage.removeItem('notRegisterUser');
};

export const getUserInfo = (): SessionReducerT | undefined => {
  if (typeof window === 'undefined') return undefined;
  const info = localStorage.getItem('info');
  if (!info) return undefined;
  return JSON.parse(info);
};

export const formatNumber = (number = 0) => {
  const price = new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(number);

  return `${price} MXN`;
};

export const getDate = (date: Date | any) => {
  const array = date.toString();
  const arrayDate = array.split('T')[0].split('-');
  const meses = [
    'enero',
    'febrero',
    'marzo',
    'abril',
    'mayo',
    'junio',
    'julio',
    'agosto',
    'septiembre',
    'octubre',
    'noviembre',
    'diciembre'
  ];
  const currentMonth = meses[Number(arrayDate[1]) - 1];
  return `${arrayDate[2]} de ${currentMonth} de ${arrayDate[0]}`;
};

export const getDateUtc = (date: string) => {
  const arrayDate = date.split('/');
  const currentMonth = MonthsOfYear[Number(arrayDate[1]) - 1];
  return `${arrayDate[0]} de ${currentMonth} de ${arrayDate[2]}`;
};

export const formatNumberComma = (number: number) => {
  return new Intl.NumberFormat('en-US').format(number);
};

export const removeAccents = (inputStr: string) => {
  return inputStr.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
};

let timerRef: NodeJS.Timeout;
export const debounceInputChange = (time: number, callback: () => any) => {
  clearTimeout(timerRef);
  timerRef = setTimeout(() => {
    callback();
  }, time);
};

export const FormatPhoneMask = (phone: string): string => {
  const phoneArr = phone.split('');
  phoneArr.splice(2, 0, ' ');
  phoneArr.splice(7, 0, ' ');

  return phoneArr.toString().replaceAll(',', '');
};
