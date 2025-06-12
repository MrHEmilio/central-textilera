import { NextPage } from 'next';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import img from '/public/img/pagoefectivo.png';
import { MainLayout } from '../../../app/modules/shared';

import { clearCart } from '../../../app/services/redux/actions/CartActions';
import { CtxDispatch, ReduxStore } from '../../../app/services/redux/store';
import { Button, Descriptions, Typography } from 'antd';
import { deleteNotRegisterUserStorage } from '../../../app/services/utils';
import { ClearNotRegisterUserState } from '../../../app/services/redux/actions/NotRegisterUserActions';
const { Text } = Typography;

const MPSuccess: NextPage = () => {
  const router = useRouter();
  const { isReady, query } = router;
  const [item, setItem] = useState<{ label: string, children: string }[]>([]);
  const dispatch = CtxDispatch();

  useEffect(() => {
    if (!isReady) return;
    setTimeout(() => {
      const { cart, session } = ReduxStore.getState();

      if (!isReady || !query || !cart) return;
      /*eslint-disable-next-line @typescript-eslint/no-non-null-assertion*/
      if (session === null || session === undefined) return
      setItem([
        {
          label: 'PAGO MP: ',
          children: `${query.payment_id}`
        },
        {
          label: 'ORDEN MP: ',
          children: `${query.merchant_order_id}`
        },
        {
          label: 'ESTATUS: ',
          children: `${query.status}`
        },

      ]);

      deleteNotRegisterUserStorage();
      dispatch(ClearNotRegisterUserState());
      dispatch(clearCart());
    }, 500);
  }, [isReady, query]);


  return (
    <MainLayout
      title={'Pago exitoso'}
      pageDescription={'Pago exitoso'}
    >
      <div
        className="mb-12 w-full p-16 text-center "
        style={{ borderBottom: '1px solid #CCCCCC ' }}
      >
        <div>
          <p className="text-3xl ps-16 font-bold color-main">
            ¡Gracias por comprar con nosotros!
          </p>
          <div className="flex justify-center">
            <img src={img.src} alt="" className="h-40 w-[500px] md:w-[250px]" />
          </div>
        </div>
        <div className="flex justify-center">
          <p className="mt-5 w-2/3 text-xl color-main">
            Tu pago está en proceso. En breve recibirás un e-mail con información de tu compra.
          </p>
        </div>
        {item && (
          <div className='mt-[2rem] flex justify-center min-w-xs'>
            <Descriptions
              column={1}
              bordered

            >
              {item.map((data, index) => (
                <Descriptions.Item key={index} label={

                  <Text strong>{data.label}</Text>
                }>
                  {data.children}
                </Descriptions.Item>
              ))}
            </ Descriptions>

          </div>
        )}
        <div className="mt-[3rem] flex justify-center min-w-xs" >
          <Button
            onClick={() => router.push('/')}
            className="button-ctx"
          >
            Ir a inicio
          </Button>
        </div>
      </div>

    </MainLayout >
  );
};
export default MPSuccess;
