import { Spin } from 'antd';
import { useRouter } from 'next/router';
import React, { useState, useEffect, FC } from 'react';
import { Role } from '../../models/Enums';

interface Props {
  children?: React.ReactNode | undefined;
}

export const ProtectAdmin: FC<Props> = ({ children }) => {
  const [isAuth, setIsAuth] = useState(false);
  const navigate = useRouter();

  useEffect(() => {
    const info = localStorage.getItem('info');
    if (info) {
      const user: any = JSON.parse(info);
      const { role } = user;
      const roles = [Role.adminRoot, Role.admin];
      setTimeout(() => {
        if (!roles.includes(role)) {
          navigate.replace('/');
        } else {
          setTimeout(() => {
            setIsAuth(true);
          }, 800);
        }
      }, 800);
    } else {
      navigate.replace('/');
    }
  }, []);

  return (
    <div>
      <div className={isAuth ? 'block' : 'hidden'}>{children}</div>
      <div className={`${!isAuth ? 'block' : 'hidden'} h-96`}>
        <div
          className={`
      absolute
    bottom-0
    right-0
    top-0 
    left-0
    z-[9999]
   grid 
    place-content-center
    backdrop-blur-sm
  `}
          // style={{ height: tamaño }}
        >
          <Spin size={'large'} />
        </div>
      </div>
    </div>
  );
};
