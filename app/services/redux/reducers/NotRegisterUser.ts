import { AnyAction, Reducer } from 'redux';
import { NotRegisterUserState } from '../../../interfaces/State/NotRegisterUserInterface';
import { getNotUserRegisterStorage } from '../../utils';

export enum NotRegisterUserOptions {
  // eslint-disable-next-line no-unused-vars
  setContact = 'setContact',
  // eslint-disable-next-line no-unused-vars
  setAddress = 'setAddress',
  // eslint-disable-next-line no-unused-vars
  setValidateStatus = 'setValidateStatus',
  // eslint-disable-next-line no-unused-vars
  setContactValid = 'setContactValid',
  // eslint-disable-next-line no-unused-vars
  setUserAddressValid = 'setUserAddressValid',
  // eslint-disable-next-line no-unused-vars
  setTaxAddress = 'setTaxAddress',
  // eslint-disable-next-line no-unused-vars
  setClearNotRegisterUser = 'clearNotRegisterUser',
  // eslint-disable-next-line no-unused-vars
  clearTaxAddress = 'clearTaxAddress',
  // eslint-disable-next-line no-unused-vars
  setBillingInfo = 'notRegisterUserBillingInfo',
  // eslint-disable-next-line no-unused-vars
  setFiscalRegimen = 'notRegisterFiscalRegimen',
  // eslint-disable-next-line no-unused-vars
  clearUserAddress = 'clearUserAddress',
  // eslint-disable-next-line no-unused-vars
  setValidatedUser = 'setValidatedUser'
}

const initialState: NotRegisterUserState = getNotUserRegisterStorage();

const saveStorageObj = (obj: unknown) => {
  if (typeof window !== undefined) {
    const str = JSON.stringify(obj);
    localStorage.setItem('notRegisterUser', str);
  }
};

export const NotRegisterUserReducer: Reducer<
  NotRegisterUserState,
  AnyAction
> = (state: NotRegisterUserState = initialState, action: AnyAction) => {
  switch (action.type) {
    case NotRegisterUserOptions.setContact:
      saveStorageObj({ ...state, userContact: action.payload });
      return { ...state, userContact: action.payload };

    case NotRegisterUserOptions.clearUserAddress:
      saveStorageObj({ ...state, userAddress: undefined });
      return { ...state, userAddress: undefined };
    case NotRegisterUserOptions.setAddress:
      if (typeof window !== undefined) {
        localStorage.setItem(
          'notRegisterUser',
          JSON.stringify({ ...state, userAddress: action.payload })
        );
      }
      return { ...state, userAddress: action.payload };
    case NotRegisterUserOptions.setUserAddressValid:
      return { ...state, isAddressValid: action.payload };
    case NotRegisterUserOptions.setValidateStatus:
      return { ...state, validate: action.payload };
    case NotRegisterUserOptions.setContactValid:
      return { ...state, isContactValid: action.payload };
    case NotRegisterUserOptions.setTaxAddress:
      if (typeof window !== undefined) {
        localStorage.setItem(
          'notRegisterUser',
          JSON.stringify({ ...state, taxAddress: action.payload })
        );
      }
      return { ...state, taxAddress: action.payload };
    case NotRegisterUserOptions.setClearNotRegisterUser:
      saveStorageObj({
        validate: false,
        isContactValid: false,
        isAddressValid: false
      });
      return {
        validate: false,
        isContactValid: false,
        isAddressValid: false
      };
    case NotRegisterUserOptions.clearTaxAddress:
      if (typeof window !== undefined) {
        localStorage.setItem(
          'notRegisterUser',
          JSON.stringify({ ...state, taxAddress: undefined })
        );
      }
      return { ...state, taxAddress: undefined };
    case NotRegisterUserOptions.setBillingInfo:
      saveStorageObj({ ...state, billingInfo: action.payload });
      return { ...state, billingInfo: action.payload };
    case NotRegisterUserOptions.setFiscalRegimen:
      saveStorageObj({ ...state, fiscalRegimen: action.payload });
      return { ...state, fiscalRegimen: action.payload };
    case NotRegisterUserOptions.setValidatedUser:
      saveStorageObj({ ...state, validatedUser: action.payload });
      return { ...state, validatedUser: action.payload };
    default:
      return state;
  }
};
