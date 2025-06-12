import { Row } from 'antd';
import React from 'react';
import { CardInfo } from '../shared';
import { LinkCtx } from '../shared/Link/Link';

export const EmptyCart = () => {
  return (
    <Row style={{ width: '100%' }}>
      <div className="container-emptycart">
        <CardInfo title="">
          <div
            style={{ padding: '2rem 0rem', width: '100%', textAlign: 'center' }}
          >
            <p>Su carrito esta vacío</p>
            <div className="container-emptyButton">
              <LinkCtx href={`/fabrics`}>
                <a className="button-link-ctx mt-4 w-64 ">
                  Continuar Comprando
                </a>
              </LinkCtx>
            </div>
          </div>
        </CardInfo>
      </div>
    </Row>
  );
};
