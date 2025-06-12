import { Row } from 'antd';

import React from 'react';
import { CardInfo } from '../shared';

export const EmptyTable = () => {
  return (
    <Row style={{ width: '100%' }}>
      <div className="container-emptycart">
        <CardInfo title="">
          <div
            style={{
              padding: '2rem 0rem',
              width: '100%',
              textAlign: 'center'
            }}
          >
            <p>No se obtuvieron resultados</p>
          </div>
        </CardInfo>
      </div>
    </Row>
  );
};
