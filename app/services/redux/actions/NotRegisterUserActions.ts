import {
  NotRegisterUserAddress,
  NotRegisterUserInfo
} from '../../../interfaces/State/NotRegisterUserInterface';
import { NotRegisterUserOptions } from '../reducers/NotRegisterUser';
import { CtxActions } from './SessionActions';

export const SetNotRegisterUserContact = (
  payload: NotRegisterUserInfo
): CtxActions => ({
  type: NotRegisterUserOptions.setContact,
  payload
});

export const SetNotRegisterUserAddress = (
  payload: NotRegisterUserAddress
): CtxActions => ({
  type: NotRegisterUserOptions.setAddress,
  payload
});

export const SetNotRegisterUserContactValid = (
  payload: boolean
): CtxActions => ({
  type: NotRegisterUserOptions.setContactValid,
  payload
});

export const SetNotRegisterUserAddressValid = (
  payload: boolean
): CtxActions => ({
  type: NotRegisterUserOptions.setUserAddressValid,
  payload
});

export const SetNotRegisterUserValidateStatus = (
  payload: boolean
): CtxActions => ({
  type: NotRegisterUserOptions.setValidateStatus,
  payload
});

export const SetNotRegisterUserTaxAddress = (
  address: NotRegisterUserAddress
): CtxActions => ({
  type: NotRegisterUserOptions.setTaxAddress,
  payload: address
});

export const ClearNotRegisterUserState = (): CtxActions => ({
  type: NotRegisterUserOptions.setClearNotRegisterUser,
  payload: null
});

export const ClearNotRegisterUserTaxAddress = (): CtxActions => ({
  type: NotRegisterUserOptions.clearTaxAddress,
  payload: null
});

export const SetNotRegisterUserBillingInof = (
  rfc: string,
  companyName?: string
): CtxActions => ({
  type: NotRegisterUserOptions.setBillingInfo,
  payload: {
    rfc,
    companyName
  }
});

export const SetNotRegisterUserFiscalRegimen = (
  fiscalRegimenValue: string
): CtxActions => ({
  type: NotRegisterUserOptions.setFiscalRegimen,
  payload: fiscalRegimenValue
});

export const notRegisterUserClearUserAddress = (): CtxActions => ({
  type: NotRegisterUserOptions.clearUserAddress,
  payload: null
});

export const setValidatedUser = (valid: boolean): CtxActions => ({
  type: NotRegisterUserOptions.setValidatedUser,
  payload: valid
});
