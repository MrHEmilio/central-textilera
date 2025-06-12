/* eslint-disable @typescript-eslint/no-non-null-assertion */
import { CartState } from '../../interfaces/State/Cart';
import { NotRegisterUserState } from '../../interfaces/State/NotRegisterUserInterface';
import { ReduxStore } from '../../services/redux/store';

/**
 * @description use it only after select a location, with or without user account, if not it will not work!
 */
export const getUserAddressForShipment = ({
  notRegisterUserState,
  cartState
}: {
  notRegisterUserState: NotRegisterUserState;
  cartState: CartState;
}) => {
  const notRegisterUserAddress = notRegisterUserState.userAddress;
  const userAddress = cartState.location;
  const { session } = ReduxStore.getState();
  return {
    lat:
      notRegisterUserAddress?.latitude?.toString() ||
      userAddress?.latitude?.toString() ||
      '',
    long:
      notRegisterUserAddress?.longitude?.toString() ||
      userAddress?.longitude?.toString() ||
      '',
    city: (notRegisterUserAddress?.city || userAddress?.city)!,
    state: (notRegisterUserAddress?.state || userAddress?.state)!,
    numExt: (notRegisterUserAddress?.numExt || userAddress?.numExt)!,
    numInt: (notRegisterUserAddress?.numInt || userAddress?.numInt)!,
    suburb: (notRegisterUserAddress?.suburb || userAddress?.suburb)!,
    country: (notRegisterUserAddress?.country || userAddress?.country)!,
    zipcode: (notRegisterUserAddress?.zipCode || userAddress?.zipCode)!,
    refs: (notRegisterUserAddress?.references || userAddress?.references)!,
    streetname: (notRegisterUserAddress?.streetName ||
      userAddress?.streetName)!,
    municipality: (notRegisterUserAddress?.municipality ||
      userAddress?.municipality)!,
    email: (notRegisterUserState.userContact?.email || session?.info?.email)!,
    phone: (notRegisterUserState.userContact?.phone || session?.info?.phone)!,
    clientName: notRegisterUserState.userContact
      ? `${notRegisterUserState.userContact?.name} ${notRegisterUserState.userContact?.lastName}`
      : `${session?.info?.name} ${session?.info?.firstLastname}`!
  };
};
