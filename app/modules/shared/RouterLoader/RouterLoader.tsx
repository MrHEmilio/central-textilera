import { Spin } from 'antd';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import { CtxSelector } from '../../../services/redux/store';

export const RouterLoader = () => {
  const router = useRouter();
  const [loading, setloading] = useState(false);
  const [tamaño] = useState('100vh');
  const fromStore = CtxSelector(state => state.loader.visible);

  useEffect(() => {
    const saludar = () => {
      if (typeof window !== 'undefined') {
        // window.scrollTo({ top: 0, behavior: 'smooth' });
      }
      setloading(true);
    };

    const despedirse = () => {
      setloading(false);
    };
    router.events.on('routeChangeStart', saludar);
    router.events.on('routeChangeComplete', despedirse);
    router.events.on('routeChangeError', despedirse);

    return () => {
      router.events.off('routeChangeStart', saludar);
      router.events.off('routeChangeComplete', despedirse);
      router.events.off('routeChangeError', despedirse);
    };
  }, [router]);

  useEffect(() => {
    setloading(fromStore);
  }, [fromStore]);

  return (
    <div
      className={`
      fixed
      z-[9999]
      h-full
      min-h-fit
      w-full
    min-w-fit
   ${loading ? 'grid' : 'hidden'}
    place-content-center
    backdrop-blur-sm
  `}
      style={{ height: tamaño }}
    >
      <Spin size={'large'} />
    </div>
  );
};
