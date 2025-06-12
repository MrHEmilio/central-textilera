import { Row } from 'antd';
import Link from 'next/link';
import React from 'react';
import { CardInfo } from '../shared';

export const EmptyDirections = () => {
  return (
    <Row style={{ width: '100%' }}>
      <div className="container-emptycart">
        <CardInfo title="">
          <div
            style={{ padding: '2rem 0rem', width: '100%', textAlign: 'center' }}
          >
            <p>No cuenta con direcciones agregadas</p>
            <div className="container-emptyButton">
              <Link href="/directions/addDirection">
                <a className="button-link-ctx buttonaddnewdirection w-40">
                  Agregar
                </a>
              </Link>
            </div>
          </div>
        </CardInfo>
      </div>
    </Row>
  );
};
