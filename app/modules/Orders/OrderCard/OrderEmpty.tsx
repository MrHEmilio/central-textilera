import { Row } from 'antd';
import Link from 'next/link';
import React from 'react';
import { CardInfo } from '../../shared';

export const OrderEmpty = () => {
  return (
    <Row style={{ width: '100%' }}>
      <div className="container-emptycart">
        <CardInfo title="">
          <div
            style={{ padding: '2rem 0rem', width: '100%', textAlign: 'center' }}
          >
            <p>No cuentas con pedidos realizados</p>
            <div className="container-emptyButton">
              <Link href="/fabrics">
                <a className="button-link-ctx buttonaddnewdirection w-60">
                  Seguir comprando
                </a>
              </Link>
            </div>
          </div>
        </CardInfo>
      </div>
    </Row>
  );
};
