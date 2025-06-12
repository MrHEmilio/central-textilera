import { Col, Row, Statistic } from 'antd';
import React, { FC, useEffect, useState } from 'react';

import { CardInfo } from '../shared';
import { InfoClientAdmin } from '../../interfaces/Response/Admin/Client';

interface Props {
  client: InfoClientAdmin | null | undefined;
  newsLetter: boolean;
  // eslint-disable-next-line no-unused-vars
}

export const FormClientAdmin: FC<Props> = ({ client, newsLetter }) => {
  const [clientCurrent, setClientCurrent] = useState<
    InfoClientAdmin | undefined
  >(undefined);

  useEffect(() => {
    if (client) {
      setClientCurrent(client);
    }
  }, [client]);

  const cellPhone = () => {
    const parte1 = clientCurrent?.phone.slice(0, 5);
    const parte2 = clientCurrent?.phone.slice(5, 10);

    return `${parte1} ${parte2}`;
  };

  return (
    <div className="">
      <CardInfo title="">
        <Row>
          <Col span={12}>
            <Statistic
              title="Nombre"
              value={`${clientCurrent?.name} ${clientCurrent?.firstLastname} ${
                clientCurrent?.secondLastname || ''
              }`}
              valueStyle={{ fontSize: '20px' }}
            />
          </Col>

          <Col span={12}>
            <Statistic
              title="NewsLetter"
              value={newsLetter ? 'Usuario Subscrito' : 'Usuario no Subscrito'}
              valueStyle={{ fontSize: '20px' }}
            />
          </Col>

          <Col span={24} className={'mt-4'}>
            <Statistic
              title="Correo"
              value={clientCurrent?.email}
              valueStyle={{ fontSize: '20px' }}
            />
          </Col>
          <Col span={24} className={'mt-4'}>
            <Statistic
              title="Teléfono"
              value={`${clientCurrent?.countryCode.code} ${cellPhone()}`}
              valueStyle={{ fontSize: '20px' }}
            />
          </Col>
          <Col span={24} className={'mt-4'}>
            <Statistic
              title="RFC"
              value={`${clientCurrent?.rfc || 'Sin registrar'}`}
              valueStyle={{ fontSize: '20px' }}
            />
          </Col>
          <Col span={24} className={'mt-4'}>
            <Statistic
              title="Razón social"
              value={`${clientCurrent?.companyName || 'Sin registrar'}`}
              valueStyle={{ fontSize: '20px' }}
            />
          </Col>
          <Col span={24} className={'mt-4'}>
            <Statistic
              title="Régimen fiscal"
              value={`${clientCurrent?.fiscalRegimen || 'Sin registrar'}`}
              valueStyle={{ fontSize: '20px' }}
            />
          </Col>
        </Row>

        {/* <Button
          type="primary"
          className="button-ctx-delete "
          onClick={deleteClient}
          loading={deleteloading}
          // disabled={disableDelete}
        >
          Eliminar
        </Button> */}
      </CardInfo>
    </div>
  );
};
