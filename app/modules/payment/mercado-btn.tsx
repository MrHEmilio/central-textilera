import { useRouter } from 'next/router';
import { FC, useEffect } from 'react';

const createCheckoutButton = (preferenceId: string, pk: string) => {
  const father = document.querySelector('.father-ch');
  const mp = new MercadoPago(pk, {
    locale: 'es-MX'
  });
  mp.checkout({
    preference: {
      id: preferenceId
    },
    render: {
      container: '.checkout',
      label: '  '
    },
    redirect: 'self'
  });

  if (father) {
    const divCheck = document.createElement('div');
    divCheck.className = 'checkout';
    father.appendChild(divCheck);
  }
};

interface Props {
  preferenceId: string;
}
export const MercadoBtn: FC<Props> = ({ preferenceId }) => {
  const pk = process.env.NEXT_PUBLIC_MP_PUBLIC_KEY;
  const router = useRouter();
  const { isReady } = router;

  useEffect(() => {
    if (!isReady || !pk) return;

    sessionStorage.setItem("preference", preferenceId);

    createCheckoutButton(preferenceId, pk);

    return () => {
      const ch = document.querySelector('.father-ch');
      if (ch) {
        ch.firstChild && ch.removeChild(ch.firstChild);
      }
    };
  }, [isReady, pk]);

  return <div className="father-ch"></div>;
};
