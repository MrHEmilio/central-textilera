import { Button, Tag } from 'antd';
import React, { FC, useEffect, useState } from 'react';

interface Props {
  currentStatus: string;
  deliveryMethod: string;
  // eslint-disable-next-line no-unused-vars
  onClick: (status: string) => void;
}

const ModalStatus: FC<Props> = ({ currentStatus, deliveryMethod, onClick }) => {
  const [statusEnable, setstatusEnable] = useState('En revisión');
  const [rootAdmin, setRootAdmin] = useState(false);

  useEffect(() => {
    setstatusEnable(currentStatus);
  }, [currentStatus]);

  useEffect(() => {
    
    const info = localStorage.getItem('info');
    if (info) {
      const user: any = JSON.parse(info);
      setRootAdmin(user.root);
    }
  }, []);

  const disabledDelivered = () => {
    if (
      statusEnable === 'Enviado' ||
      statusEnable === 'Listo para recoger en sucursal' ||
      rootAdmin === true
    ) {
      return false;
    }

    return true;
  };

  return (
    <div className="flex flex-col items-center">
      <div className="mb-2">
        <Button
          disabled={rootAdmin === true ? false : true}
          type="text"
          onClick={() => onClick('REVISION')}
        >
          <Tag
            color={rootAdmin === true ? 'orange' : 'default'}
            style={{ padding: '0 2rem' }}
            className="w-[10rem] cursor-pointer text-center "
          >
            En revisión
          </Tag>
        </Button>
      </div>
      <div className="mb-2">
        <Button
          disabled={
            statusEnable === 'En revisión' || rootAdmin === false ? false : true
          }
          type="text"
          onClick={() => onClick('PREPARATION')}
        >
          <Tag
            color={
              statusEnable === 'En revisión' || rootAdmin === true
                ? 'orange'
                : 'default'
            }
            style={{ padding: '0 2rem' }}
            className="w-[10rem] text-center"
          >
            En preparación
          </Tag>
        </Button>
      </div>
      {deliveryMethod === 'Envío a domicilio' && (
        <div className="mb-2">
          <Button
            disabled={
              statusEnable === 'Preparando los productos' || rootAdmin === true
                ? false
                : true
            }
            type="text"
            onClick={() => onClick('SHIPPING')}
          >
            <Tag
              color={
                statusEnable === 'Preparando los productos' ||
                rootAdmin === true
                  ? 'cyan'
                  : 'default'
              }
              style={{ padding: '0 2rem' }}
              className="w-[10rem] text-center"
            >
              Enviado
            </Tag>
          </Button>
        </div>
      )}
      {deliveryMethod === 'Recoger en sucursal' && (
        <div className="mb-2">
          <Button
            disabled={
              statusEnable === 'Preparando los productos' || rootAdmin === true
                ? false
                : true
            }
            type="text"
            onClick={() => onClick('PICK_UP')}
          >
            <Tag
              color={
                statusEnable === 'Preparando los productos' ||
                rootAdmin === true
                  ? 'blue'
                  : 'default'
              }
              style={{ padding: '0 2rem' }}
              className="w-[10rem] text-center"
            >
              En sucursal
            </Tag>
          </Button>
        </div>
      )}
      <div>
        <Button
          type="text"
          disabled={disabledDelivered()}
          onClick={() => onClick('DELIVERED')}
        >
          <Tag
            color={disabledDelivered() ? 'default' : 'blue'}
            style={{ padding: '0 2rem' }}
            className="w-[10rem] text-center"
          >
            Entregado
          </Tag>
        </Button>
      </div>
    </div>
  );
};

export default ModalStatus;
