import { CtxSelector, CtxDispatch } from '../../../services/redux/store';
import { LogOut } from '../../../services/redux/actions/SessionActions';
import { FC } from 'react';
import { closeSesionService } from '../../../services/auth/authService';
import { useRouter } from 'next/router';
import { ArrayRoutesBack } from '../../../models/Enums';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../../services/redux/actions';

interface Props {
  onCloseSesion: () => void;
}

export const TitleModalLogin: FC<Props> = ({ onCloseSesion }) => {
  const navigate = useRouter();
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const dispatch = CtxDispatch();

  const { auth } = userInfo;

  const closeSession = async () => {
    dispatch(LoaderActionsShow());
    onCloseSesion();
    dispatch(LogOut());
    await closeSesionService();
    localStorage.removeItem('info');
    localStorage.removeItem('session');
    const path = navigate.asPath;
    if (
      ArrayRoutesBack.includes(path) ||
      path.includes('admin') ||
      path.includes('fiscal')
    ) {
      navigate.replace('/');
    } else {
      setTimeout(() => {
        dispatch(LoaderActionsHide());
      }, 800);
    }
  };

  return (
    <div
      style={{ display: 'flex', justifyContent: 'space-between' }}
      id="prueba"
    >
      <p
        style={{
          fontSize: '20px',
          fontWeight: '700',
          paddingTop: '.5rem',
          paddingBottom: '.5rem',
          paddingRight: auth ? ' 4rem ' : '10rem'
        }}
      >
        Mi Cuenta
      </p>
      {auth && (
        <a
          id="prueba"
          onClick={closeSession}
          style={{
            fontSize: '15px',
            fontWeight: '700',
            paddingTop: '.8rem ',
            color: '#006eb2'
          }}
          className="closeSession"
        >
          Cerrar sesión
        </a>
      )}
    </div>
  );
};
