import { UpOutlined, WechatOutlined, WhatsAppOutlined } from '@ant-design/icons';
import * as Chatra from '@chatra/chatra';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { FC, ReactNode, useEffect, useState } from 'react';
import { getUserInfo } from '../../../services/utils';

export const Totop = () => {
  const router = useRouter();
  const { isReady } = router;
  const [top, setTop] = useState(true);

  useEffect(() => {
    if (!isReady) return;
    initChatra();
  }, [isReady]);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY < 200) {
        setTop(true);
      } else {
        setTop(false);
      }
    };

    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  const initChatra = async () => {
    const userInfo = getUserInfo()?.info;
    let config: any = {
      ID: process.env.NEXT_PUBLIC_CHATRA_ID,
      setup: {
        buttonSize: 50,
        colors: {
          buttonBg: '#192440'
        }
      }
    };

    if (userInfo) {
      config = {
        ...config,
        integration: {
          name: `${userInfo.name} ${userInfo.firstLastname}`,
          email: userInfo.email
        }
      };
    }

    Chatra('init', config);
  };
  const gotoTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };
  interface btnProps {
    children: ReactNode;
    classColor: 'bg-chatbtn' | 'bg-whatsapp';
    link?: string;
  }
  const ButtonContact: FC<btnProps> = ({ children, classColor, link }) => {
    return (
      <Link href={link || ''}>
        <a
          href={link}
          target="_blank"
          rel="noopener noreferrer"
          type="button"
          className={`${classColor} grid
          h-[2.3rem]
            w-[2.3rem] place-content-center
            rounded-r-lg
            text-lg
            text-white
            transition-all
            duration-150
            hover:w-[4rem]
            md:h-[3rem]
            md:w-[3rem]
            md:text-2xl
            md:hover:w-[4.3rem]`}
        >
          {children}
        </a>
      </Link>
    );
  };
  return (
    <>
      <div className="fixed bottom-[9%] left-0 z-[999]">
        <ButtonContact
          link="https://web.whatsapp.com/send?phone=525525534949&text=Hola!%20quisiera%20informacion%20"
          classColor="bg-whatsapp"
        >
          <WhatsAppOutlined />
        </ButtonContact>
      </div>
      {/*<div className="fixed bottom-[calc(15%+4rem)] right-0 z-[999]">
        <ButtonContact classColor="bg-chatbtn">
          <WechatOutlined />
        </ButtonContact>
      </div>*/}
      <button
        onClick={gotoTop}
        style={{
          zIndex: 999
        }}
        className={`
      ${top ? 'hidden' : 'fixed'}
      bottom-4
      right-4 flex
      h-[3rem]
      w-[3rem]
      items-center
      justify-center
      rounded-full
      border-none 
      bg-white
      shadow-sm
      shadow-slate-400 focus:outline-none`}
      >
        *<UpOutlined />
      </button>
    </>
  );
};
