import { NextPage } from 'next';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import { OrderDetail } from '../../app/interfaces/Response/Orders/Orders';
import { OrderCard } from '../../app/modules/Orders/OrderCard';
import { MainLayout, SuccesPay } from '../../app/modules/shared';
import { ClearNotRegisterUserState } from '../../app/services/redux/actions/NotRegisterUserActions';
import { TicketActionsClear } from '../../app/services/redux/actions/TicketActions';
import { CtxDispatch, CtxSelector } from '../../app/services/redux/store';
import { deleteNotRegisterUserStorage } from '../../app/services/utils';

const TicketPage: NextPage = () => {
  const dispatch = CtxDispatch();
  const ticketInfo: OrderDetail = CtxSelector(
    state => state.ticket as OrderDetail
  );

  const router = useRouter();
  const { isReady } = router;

  const [showComponent, setShowComponent] = useState(false);
  const notRegisterUserS = CtxSelector(s => s.notRegisterUser);

  useEffect(() => {
    if (!isReady || !ticketInfo) return;
    if (!ticketInfo.id) router.replace('/fabrics');
    setShowComponent(true);
  }, [isReady, ticketInfo]);

  return (
    <MainLayout title={'Compra exitosa'} pageDescription={'Compra exitosa'}>
      {showComponent && ticketInfo && (
        <div className=" flex flex-col items-center justify-center">
          <div className="w-full max-w-5xl">
            <SuccesPay />
          </div>
          {ticketInfo.id && (
            <div
              className="cursor-pointer"
              onClick={() => {
                dispatch(TicketActionsClear());
                if (notRegisterUserS) {
                  deleteNotRegisterUserStorage();
                  dispatch(ClearNotRegisterUserState());
                  router.replace('/');
                } else {
                  router.replace('/orders');
                }
              }}
            >
              {/*eslint-disable-next-line @typescript-eslint/no-explicit-any*/}
              <OrderCard order={ticketInfo as any} />
            </div>
          )}
        </div>
      )}
    </MainLayout>
  );
};

export default TicketPage;
