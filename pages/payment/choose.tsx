import { LeftOutlined } from '@ant-design/icons';
import { Radio, Skeleton } from 'antd';
import { NextPage } from 'next';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import { CartState } from '../../app/interfaces/State/Cart';

import { MercadoPagoFC } from '../../app/modules/payment';
import PaypalPayment from '../../app/modules/payment/paypal';
import { PaymentLayout } from '../../app/modules/PaymentLayout';
import { PaymentMethods } from '../../app/services';
import { calculateInventory } from '../../app/services/Order';
import { LoaderActionsHide } from '../../app/services/redux/actions';
import { saveCartOnStorage } from '../../app/services/redux/actions/CartActions';
import { SetNotRegisterUserValidateStatus } from '../../app/services/redux/actions/NotRegisterUserActions';
import { CtxDispatch, ReduxStore } from '../../app/services/redux/store';
import icono from '/public/img/icono.png';
import mpImg from '/public/img/mercado-pago.svg';
import paypalImg from '/public/img/paypal-logo-tcalm.svg';

const ChoosePayment: NextPage = () => {
  const [payMethod, setPayMethod] = useState<string>(PaymentMethods.paypal);
  const [taxDir, setTaxDir] = useState<'same' | 'dif' | string>('same');
  const [loading, setloading] = useState(true);
  // const [isOkToPurchase, setisOkToPurchase] = useState(false);

  const dispatch = CtxDispatch();
  const router = useRouter();
  const { isReady } = router;

  const checkInventory = async () => {
    const s = ReduxStore.getState();

    if (!s.cart) return

    const cart: CartState = s.cart;
    const r = await calculateInventory({
      cloths: cart.products.map(x => ({
        amount: x.amount as number,
        variant: x.variant
      })),
      samplers: cart.samplers.map(x => ({
        amount: x.amount as number,
        sampler: x.id
      }))
    });
    setloading(false);
    if (!r || typeof r === 'object') {
      router.replace('/cart');
    }
  };

  useEffect(() => {
    if (!isReady) return;
    checkInventory();
    const state = ReduxStore.getState();
    dispatch(LoaderActionsHide());
    dispatch(SetNotRegisterUserValidateStatus(false));
    const cartInfo = state.cart;
    if (Object.getOwnPropertyNames(cartInfo).length == 0) {
      router.replace('/');
      return;
    }
    dispatch(SetNotRegisterUserValidateStatus(false));
    if (cartInfo?.deliveryMethod === 'PICK_UP') {
      setTaxDir('dif');
    }
  }, [isReady]);

  useEffect(() => {
    //Falta asignar el same
  }, [taxDir]);

  const saveCartMP = () => {
    
    dispatch(saveCartOnStorage());

    router.replace('/payment/page-ticketMP')
  }

  const Advertisement = (
    <div className="p-7">
      <div></div>
      <div className="text-center">
        <div className="flex justify-center">
          <img src={icono.src} alt="icono" />
        </div>
        Luego de hacer click en el botón de la plataforma, serás redirigido a
        {payMethod === PaymentMethods.paypal ? ' PayPal' : ' Mercado Pago'} para
        completar tu compra de forma segura.
      </div>
    </div>
  );

  return (
    <PaymentLayout>
      {loading ? (
        <div className="flex flex-col gap-5 p-8">
          <Skeleton.Input active className="w-full" />
          <Skeleton.Input active className="w-full" />
          <Skeleton.Button active />
        </div>
      ) : (
        <div>
          <div className="my-10 mb-28">
            <div className="mt-10">
              <h4 className="text-2xl font-bold">Pago</h4>
              <p className="mb-6 mt-3">
                Todas las transacciones son seguras y están encriptadas
              </p>
              <div className="rounded-2xl border-2 border-graySeparation p-3">
                <Radio.Group
                  className="grid grid-cols-1"
                  defaultValue={PaymentMethods.paypal}
                  onChange={({ target: { value } }) => setPayMethod(value)}
                >
                  <Radio
                    value={PaymentMethods.paypal}
                    className="flex items-start"
                  >
                    <div className="flex flex-col">
                      <div></div>
                      <div className="h-9">
                        <img src={paypalImg.src} />
                      </div>
                      {payMethod === PaymentMethods.paypal && (
                        <div>{Advertisement}</div>
                      )}
                    </div>
                  </Radio>
                  <hr className="my-3" />
                  <Radio value={PaymentMethods.mp} className="flex items-start">
                    <div className="flex flex-col">
                      {/* <div></div> */}
                      <div>
                        <img className="h-9" src={mpImg.src} />
                      </div>
                      {payMethod === PaymentMethods.mp && (
                        <div>{Advertisement}</div>
                      )}
                    </div>
                  </Radio>
                </Radio.Group>
              </div>
            </div>
          </div>
          {/*end directions*/}
          <div
            className="my-10"
            onClick={() => {
              if (payMethod === PaymentMethods.mp) {
                saveCartMP();
              } else {
                dispatch(saveCartOnStorage());
              }
            }}
          >
            <>
              {payMethod === PaymentMethods.paypal && (
                <div>
                  <PaypalPayment />
                </div>
              )}
            </>
            <div>
              {payMethod === PaymentMethods.mp && (
                <div
                  className={
                    payMethod !== PaymentMethods.mp
                      ? 'absolute left-[-100rem]'
                      : ''
                  }
                >
                  <MercadoPagoFC />
                </div>
              )}
            </div>
            <Link href="/payment/billing" className="hover:text-enphasis">
              <a className="flex items-center gap-2">
                <LeftOutlined /> Volver a Facturación
              </a>
            </Link>
          </div>
        </div>
      )}
    </PaymentLayout>
  );
};

export default ChoosePayment;
