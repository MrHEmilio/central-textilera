import { useRouter } from 'next/router';
import React, { FC, useEffect } from 'react';
import { CtxDispatch } from '../../../services/redux/store';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../../services/redux/actions';
// import Link from 'next/link';

interface Props {
  children?: React.ReactNode | undefined;
  auth: boolean;
}

export const ProtectedRoutesDirections: FC<Props> = ({ children, auth }) => {
  // const [isAuth, setIsAuth] = useState(false);
  const navigate = useRouter();
  const dispatch = CtxDispatch();

  useEffect(() => {
    dispatch(LoaderActionsShow());
    setTimeout(() => {
      if (!auth) {
        navigate.replace('/');
      } else {
        setTimeout(() => {
          // setIsAuth(true);
          dispatch(LoaderActionsHide());
        }, 800);
      }
    }, 800);
  }, [auth]);

  return (
    <div>
      <div>{children}</div>
      {/* <div className={`${!isAuth ? 'block' : 'hidden'} h-96`}>
        <div className="flex content-center justify-center py-24 ">
          <Spin size="large" className="" />
        </div>
      </div> */}
    </div>
  );
};
