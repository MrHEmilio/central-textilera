import { NextPage } from 'next';
import { useRouter } from 'next/router';
import { RefObject, useEffect, useState } from 'react';
import ReCAPTCHA from 'react-google-recaptcha';
import { toast } from 'react-toastify';
import { useReady } from '../../app/Hooks/useReady';
import { useUserInfo } from '../../app/Hooks/useUserInfo';
import { CreateUser } from '../../app/interfaces/Request/Client/CreateUser';
import { FormRegister } from '../../app/modules/auths';
import { MainLayout } from '../../app/modules/shared';
import { updateClient } from '../../app/services/Client';
import { CtxDispatch } from '../../app/services/redux/store';
import { UpdateSessionInfo } from '../../app/services/redux/actions/SessionActions';

const FiscalInfoPage: NextPage = () => {
  const [loading, setLoading] = useState(false);
  const userinfo = useUserInfo();
  const router = useRouter();
  const ready = useReady();
  const dispatch = CtxDispatch();

  useEffect(() => {
    if (ready && !userinfo) {
      router.replace('/');
    }
  }, [ready, userinfo]);

  const onRegister = async (
    data: CreateUser,
    captcha: RefObject<ReCAPTCHA>
  ) => {
    if (!userinfo) {
      router.replace('/');
      return;
    }
    setLoading(true);
    const req = { ...data };
    const response = await updateClient(req);
    if (!response) {
      setLoading(false);
      return
    }
    captcha.current?.reset();
    setLoading(false);
    dispatch(UpdateSessionInfo(response.data));
    toast.success(response.message, { theme: 'colored' });
  };
  return (
    <MainLayout title={'Fiscal Info'} pageDescription={'Mi información fiscal'}>
      <div className="container mx-auto mt-12 max-w-lg">
        <FormRegister fill onRegister={onRegister} loading={loading} />
      </div>
    </MainLayout>
  );
};

export default FiscalInfoPage;
