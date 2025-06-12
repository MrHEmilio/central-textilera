import { useRouter } from 'next/router';
import React, { FC, useEffect } from 'react';
import { SessionStore } from '../../../interfaces/State/session';

// import Link from 'next/link';
import { Role } from '../../../models/Enums';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../../services/redux/actions';
import { CtxDispatch } from '../../../services/redux/store';

interface Props {
  children?: React.ReactNode | undefined;
  auth: SessionStore;
}

export const ProtectedRoutesAuth: FC<Props> = ({ children, auth }) => {
  // const [isAuth, setIsAuth] = useState(false);
  const navigate = useRouter();
  const dispatch = CtxDispatch();

  useEffect(() => {
    setTimeout(() => {
      dispatch(LoaderActionsShow());
      if (auth.auth) {
        if (auth.role === Role.adminRoot || auth.role === Role.admin) {
          navigate.push('/admin/home');
        } else if (auth.role === 'CLIENT') {
          navigate.push('/');
        } else {
          navigate.back();
        }
      } else {
        // setTimeout(() => {
        //   // setIsAuth(true);
        // }, 800);
        dispatch(LoaderActionsHide());
      }
    }, 800);
  }, [auth]);

  return (
    <div>
      <div>{children}</div>
      {/* <div className={`${!isAuth ? 'block' : 'hidden'} h-96`}>
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
      </div> */}
    </div>
  );
};
