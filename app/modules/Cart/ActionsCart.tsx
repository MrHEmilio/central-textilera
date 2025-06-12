import { Button, Checkbox } from 'antd';
import { CheckboxChangeEvent } from 'antd/lib/checkbox';
import { useRouter } from 'next/router';
import { useState } from 'react';
import { LinkCtx } from '../shared/Link';
import paypal from '/public/img/paypal-logo-tcalm.svg';
import mercadoLibre from '/public/img/mercado-pago.svg';
import { CtxDispatch } from '../../services/redux/store';
import { LoaderActionsShow } from '../../services/redux/actions';

export const ActionsCart = () => {
  const [checked, setChecked] = useState(true);
  const router = useRouter();
  const dispatch = CtxDispatch();

  const onChange = (e: CheckboxChangeEvent) => {
    setChecked(!e.target.checked);
  };
  return (
    <>
      <div className="actions-container">
        <div className="flex flex-col justify-between gap-4 sm:flex-row">
          <LinkCtx href={'/terms-conditions'}>
            <a className="text-center text-main underline">
              Lea los términos y condiciones
            </a>
          </LinkCtx>
          <Checkbox
            style={{
              color: '#ff0000',
              fontWeight: 'bold'
            }}
            onChange={onChange}
          >
            <span className="text-l font-bold text-red-600">** He leído y acepto los términos y condiciones.</span>
          </Checkbox>
        </div>

        <div className="actions-buttons">
          {/*<LinkCtx href={`/fabrics`}>
            <a className="button-link-ctx button-continueshop link-forgotpassword w-60">Seguir comprando</a>
          </LinkCtx>*/}

          <Button className="button-link-ctx button-continueshop link-forgotpassword w-60" onClick={() => router.back()}>
          Seguir comprando . . .

          </Button>

          <Button
            type="primary"
            onClick={() => {
              dispatch(LoaderActionsShow());
              router.push('/payment');
            }}
            htmlType="submit"
            className="button-link-ctx button-continueshop w-60"
            disabled={checked}
          >
            Realizar pago . . .
          </Button>
        </div>

        <div className="flex justify-end">
          <div className="paypal-logo mr-4">
            <img className="h-6" src={paypal.src} />
          </div>
          <div className="paypal-logo">
            <img className="h-6" src={mercadoLibre.src} />
          </div>
        </div>
      </div>
    </>
  );
};
