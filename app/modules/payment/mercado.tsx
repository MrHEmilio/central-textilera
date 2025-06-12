import { Skeleton } from 'antd';
import { FC, useEffect, useState } from 'react';
import { useReady } from '../../Hooks/useReady';
import { PaymentCheckoutRequest } from '../../interfaces/Request/payment/PaymentRequests';
import { CartState } from '../../interfaces/State/Cart';
import { ClothSimple, SamplerSimple } from '../../services';
import { getPreferenceIdMP } from '../../services/payment/PaymentServices';
import { CtxSelector, ReduxStore } from '../../services/redux/store';
import { getUserInfo } from '../../services/utils';
import { MercadoBtn } from './mercado-btn';

export const MercadoPagoFC: FC = () => {
  const [preferenceId, setPreferenceId] = useState<string>();
  const [visible, setVisible] = useState(false);
  const notRegisterUserS = CtxSelector(s => s.notRegisterUser);
  const isReady = useReady();

  useEffect(() => {
    if (!isReady) return;
    const cart: CartState = JSON.parse(localStorage.getItem('cart') || '');
    const { session } = ReduxStore.getState();
    if (!cart.products && !cart.samplers) return;
    const cloths: ClothSimple[] = cart.products.map(i => ({
      amount: i.quantity,
      variant: i.variant
    }));

    const samplers: SamplerSimple[] =
      cart.samplers.map(i => ({
        amount: i.quantity || 0,
        sampler: i.id
      })) || [];

    const { location, taxLocation } = cart;
    const { userAddress, userContact, taxAddress } = notRegisterUserS;
    const userRegistered = getUserInfo()?.info;

    const req = {
      samplers,
      cloths,
      client: {
        countryCode:
          userContact?.countryCode || userRegistered?.countryCode?.id || '',
        email: userContact?.email || userRegistered?.email || '',
        firstLastname:
          userContact?.lastName || userRegistered?.firstLastname || '',
        secondLastName:
          userContact?.secondLastName || userRegistered?.secondLastname || '',
        name: userContact?.name || userRegistered?.name || '',
        phone: userContact?.phone || userRegistered?.phone || ''
      },
      ...((userAddress || taxAddress || location?.id) && {
        clientAddress: !session?.auth
          ? taxAddress || userAddress
          : taxLocation || location
      }),
      shippingPrice: cart.shippingInfo?.price || 0
    };

    getPreferenceIdMP(req as PaymentCheckoutRequest).then(r => {
      if (!r) return;
      setVisible(true);
      setPreferenceId(r.id);
    });
  }, [isReady]);

  return (
    <div className="grid h-[10rem] w-full place-content-center">
      {visible ? (
        <div>{preferenceId && <MercadoBtn preferenceId={preferenceId} />}</div>
      ) : (
        <Skeleton.Button
          block
          className="h-[4rem] w-[14rem]"
          size={'large'}
          active
        />
      )}
    </div>
  );
};
