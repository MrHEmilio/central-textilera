import { Checkbox, Form, Row } from 'antd';
import FormItem from 'antd/lib/form/FormItem';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { UserInfo } from '../../../interfaces/Response/User/UserInfo';
import { verifyNewsLetter } from '../../../services/newLetters';
import { SetNewsLetterState } from '../../../services/redux/reducers/NewsLetterReducer';
import { CtxDispatch, CtxSelector } from '../../../services/redux/store';
import { FormatPhoneMask, getUserInfo } from '../../../services/utils';
import { CardInfo } from '../../shared';
import { PaymentFormUserNotRegistered } from './PaymentFormUserNotRegistered';

export const PaymentFormContact: FC = () => {
  const dispatch = CtxDispatch();
  const { email } = CtxSelector(s => s.newsLetterRegistering);
  const [userInfo, setUserInfo] = useState<UserInfo>();
  const { userContact } = CtxSelector(s => s.notRegisterUser);
  const router = useRouter();
  const { pathname } = router;
  const { isReady } = router;

  useEffect(() => {
    if (!isReady || !userInfo) return;
    verifyNewsLetter(userInfo.email).then(rs => {
      const r = rs as { isSubscribe: boolean };
      // setIsSubscribed(r?.isSubscribe || false); // pending check these value
      dispatch(SetNewsLetterState(r?.isSubscribe || false));
    });
  }, [isReady, userInfo]);

  useEffect(() => {
    if (!isReady) return;
    const info = getUserInfo();
    if (!info) return;
    setUserInfo(info.info);
  }, [isReady]);

  return (
    <Row justify="space-between">
      <CardInfo title="Información de contacto">
        {(userInfo || userContact) && (pathname !== '/payment' || userInfo) ? (
          <div className="pl-4">
            <p className="mb-2 text-xs sm:text-sm">
              <span className='mr-4 font-["Mon-Bold"]'>Nombre:</span>{' '}
              {(userInfo &&
                `${userInfo?.name} ${userInfo?.firstLastname} ${
                  userInfo?.secondLastname || ''
                }`) ||
                `${userContact?.name} ${userContact?.lastName} ${
                  userContact?.secondLastName || ''
                }`}
            </p>
            <p className='mb-2 text-xs sm:text-sm'>
              <span className='mr-4 font-["Mon-Bold"]'>Correo:</span>{' '}
              {userInfo?.email || userContact?.email}{' '}
            </p>
            <p className="mb-2 text-xs sm:text-sm">
              <span className='mr-4 font-["Mon-Bold"]'>Teléfono:</span>{' '}
              {FormatPhoneMask(userInfo?.phone || userContact?.phone || '')}{' '}
            </p>
          </div>
        ) : (
          <PaymentFormUserNotRegistered setEmail={v => v} />
        )}
        <Form className="mt-3 w-full">
          {isReady && (
            <FormItem className="mb-0">
              <Checkbox
                checked={email}
                onChange={({ target: { checked } }) => {
                  dispatch(SetNewsLetterState(checked));
                }}
                className='text-xs'
              >
                Enviarme novedades y ofertas por correo electrónico
              </Checkbox>
            </FormItem>
          )}
        </Form>
      </CardInfo>
    </Row>
  );
};
