import { Button } from 'antd';
import React, { FC } from 'react';
import { LinkCtx } from '../shared/Link';
import { CtxSelector } from '../../services/redux/store';

interface Props {
  name: string;
  // eslint-disable-next-line no-unused-vars
  onSubmitCart: (buy?: boolean) => void;
}

export const ActionModalDetail: FC<Props> = ({ name, onSubmitCart }) => {
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  return (
    <div className="container-actionsModal">
      <div className="moreInfo-modal">
        <LinkCtx href={`products/${name}`}>
          <a>Más información</a>
        </LinkCtx>
      </div>
      <div className="container-buttons-dialog">
        {userInfo?.role !== 'ADMIN' && userInfo?.role !== 'ADMIN_ROOT' && (
          <>
            <Button
              type="primary"
              className="button-ctx w-2/3"
              onClick={() => onSubmitCart()}
            >
              Agregar al carrito
            </Button>
            <Button
              type="primary"
              htmlType="submit"
              className="button-ctx mt-3 w-2/3 bg-main text-white"
              onClick={() => onSubmitCart(true)}
            >
              Comprar
            </Button>
          </>
        )}
      </div>
    </div>
  );
};
