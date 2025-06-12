import { NextPage } from 'next';
import { useEffect } from 'react';
import { PaymentForm } from '../../app/modules/payment/form';
import { PaymentLayout } from '../../app/modules/PaymentLayout';
import { resetCartOnPayment } from '../../app/services/redux/actions/CartActions';
import { CtxDispatch } from '../../app/services/redux/store';

const PaymentPage: NextPage = () => {
  const ctxDispatch = CtxDispatch();
  useEffect(() => {
    ctxDispatch(resetCartOnPayment());
  }, []);
  return (
    <PaymentLayout>
      <PaymentForm />
    </PaymentLayout>
  );
};
export default PaymentPage;
