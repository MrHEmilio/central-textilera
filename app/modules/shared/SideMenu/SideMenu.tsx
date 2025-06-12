import { MenuOutlined } from '@ant-design/icons';
import { Drawer } from 'antd';
import { useState } from 'react';

import { LinkCtx } from '../Link';

export const SideMenu = () => {
  const [visible, setVisible] = useState<boolean>(false);

  const close = () => {
    setVisible(false);
  };

  const toogleMenu = () => {
    setVisible(!visible);
  };

  return (
    <>
      <button
        type="button"
        className="flex items-center text-lg"
        style={{
          border: 'none',
          // marginLeft: '.5rem',
          background: 'transparent'
        }}
        onClick={toogleMenu}
      >
        <MenuOutlined />
      </button>
      <Drawer placement="left" visible={visible} onClose={close}>
        <ul className="list-group list-group-flush ul-container">
          {
            // <li className="list-group-item ">
            //   <LinkCtx href={'/auths/login'}>INICIAR SESIÓN</LinkCtx>
            //   </li>
          }
          <li className="list-group-item ">
            <LinkCtx href={'/applications'}>USOS</LinkCtx>
          </li>
          <li className="list-group-item ">
            <LinkCtx href={'/fabrics'}>TELAS</LinkCtx>
          </li>
          <li className="list-group-item ">
            <LinkCtx href={'/contact'}>CONTACTO</LinkCtx>
          </li>
        </ul>
      </Drawer>
    </>
  );
};
