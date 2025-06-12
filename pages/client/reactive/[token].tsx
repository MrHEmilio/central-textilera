import { NextPage } from 'next';
import { useRouter } from 'next/router';
import { useEffect } from 'react';
import { toast } from 'react-toastify';
import { MainLayout } from '../../../app/modules/shared';
import { putClientReactive } from '../../../app/services/Client';
import { LoaderActionsShow } from '../../../app/services/redux/actions';
import { CtxDispatch } from '../../../app/services/redux/store';

const ReactivatAccountPage: NextPage = () => {
  const router = useRouter();
  const { query, isReady } = router;
  const dispatch = CtxDispatch();

  useEffect(() => {
    dispatch(LoaderActionsShow());
    if (!query || !isReady) return;
    const { token } = query as { token: string };
    if (!token) return;
    putClientReactive(token)
      .then(r => {
        if (!r) return;
        toast.success(r.message, { theme: 'colored' });
        router.replace('/');
      })
      .catch((err: { message: string }) => {
        toast.error(err.message, { theme: 'colored' });
        router.replace('/');
      });
  }, [query, isReady]);

  return (
    <MainLayout pageDescription={'Verify account link'} title={'VerifyAccount'}>
      <div className="min-h-[70vh]"></div>
    </MainLayout>
  );
};

export default ReactivatAccountPage;
