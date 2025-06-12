import { useRouter } from 'next/router';
import React, { useEffect, useState } from 'react';
import { ProtectAdmin } from '../../../app/modules/Admin';
import { AdminLayout } from '../../../app/modules/shared/AdminLayout';
import {
  getIdCliente,
  deleteClientAdmin,
  reactiveClient
} from '../../../app/services/Client/ClientServices';
import { InfoClientAdmin } from '../../../app/interfaces/Response/Admin/Client';
import { FormClientAdmin } from '../../../app/modules/Admin/FormClientAdmin';
import { CardInfo } from '../../../app/modules/shared';
import { Button, Col, Modal, Row, Spin } from 'antd';
import { verifyNewsLetter } from '../../../app/services/newLetters/newLetters';
import { getAllClientAddressId } from '../../../app/services/Client/address/addressService';
import { DirectionCard } from '../../../app/modules/Directions';
import { getAllOrderId } from '../../../app/services/Order/CalculatePrice';
import { getOrderClientAdmin } from '../../../app/interfaces/Response/Admin/OrderClient';
import { CardOrderAdmin } from '../../../app/modules/Admin/CardOrderAdmin';
import {
  changePassword,
  verifyEmailAdmin
} from '../../../app/services/email/email';
import { toast } from 'react-toastify';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { NewAddress } from '../../../app/interfaces/Request/Client/Address';

const { confirm } = Modal;

const addClient = () => {
  const [infoUser, setInfoUser] = useState<InfoClientAdmin | null>();
  const [newsLetter, setNewsLetter] = useState(false);
  const [loadingGetInfo, setLoadingGetInfo] = useState(false);
  const [loadingMail, setLoadingMail] = useState(false);
  const [loadingVerify, setLoadingVerify] = useState(false);
  const [loadingActive, setLoadingActive] = useState(false);
  const [direction, setDirection] = useState<NewAddress[]>([]);
  const [orders, setOrders] = useState<getOrderClientAdmin[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const { query, isReady } = router;

  const getUsuario = async (idClient: string) => {
    setLoadingGetInfo(true);
    const response = await getIdCliente(idClient);
    if (response) {
      setInfoUser(response.info);
    }
    setLoadingGetInfo(false);
  };

  const veifyNewsLetterAdmin = async () => {
    const response = await verifyNewsLetter(infoUser?.email || '');
    if (response) {
      setNewsLetter(true);
    }
  };

  const getDirections = async (id: string) => {
    const response = await getAllClientAddressId(id);
    if (response) {
      setDirection(response.content);
    }
  };

  const getOrders = async (id: string) => {
    const response = await getAllOrderId(id);
    if (response) {
      setOrders(response.content);
    }
    setLoading(false);
  };

  const changePasswordClient = async () => {
    setLoadingMail(true);
    if (infoUser) {
      const response = await changePassword(infoUser.email);
      if (response) {
        toast.success(response.message, { theme: 'colored' });
      }
    }
    setLoadingMail(false);
  };

  const emailVerify = async () => {
    setLoadingVerify(true);
    if (infoUser) {
      const response = await verifyEmailAdmin(infoUser.id);
      if (response) {
        toast.success(
          'Se ha enviado un correo electrónico al cliente para que valide su cuenta',
          { theme: 'colored' }
        );
      }
    }
    setLoadingVerify(false);
  };

  const modifyClient = async () => {
    setLoadingActive(true);
    if (infoUser?.active) {
      confirm({
        icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
        content: <h1>¿Desea desactivar este Cliente?</h1>,
        async onOk() {
          const response = await deleteClientAdmin(infoUser?.id);
          if (response) {
            toast.success(response.message, { theme: 'colored' });
            getUsuario(infoUser?.id);
          }
          setLoadingActive(false);
        },
        onCancel() {
          setLoadingActive(false);
        },
        cancelText: 'Cancelar'
      });
    } else if (!infoUser?.active) {
      confirm({
        icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
        content: <h1>¿Desea reactivar este cliente?</h1>,
        async onOk() {
          const response = await reactiveClient(infoUser?.id || '');
          if (response) {
            toast.success(response.message, { theme: 'colored' });
            getUsuario(infoUser?.id || '');
          }
          setLoadingActive(false);
        },
        onCancel() {
          setLoadingActive(false);
        }
      });
    }
    setLoadingActive(false);
  };

  useEffect(() => {
    if (!query || !isReady) return;
    const { idClient } = query as { idClient: string };
    getUsuario(idClient);
  }, [query, isReady]);

  useEffect(() => {
    if (infoUser) {
      setLoading(true);
      veifyNewsLetterAdmin();
      getDirections(infoUser.id);
      getOrders(infoUser.id);
    }
  }, [infoUser]);

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div
          className={`
      fixed
      z-[9999]
      h-full
      min-h-fit
      w-full
    min-w-fit
   ${loadingGetInfo ? 'grid' : 'hidden'}
    place-content-center
    backdrop-blur-sm
  `}
        >
          <Spin size={'large'} />
        </div>
        <div
          className="color-main container relative mx-auto"
          style={{ border: '1px solid #F6F6F7' }}
        >
          <div className="fixed right-[2%] z-50 ">
            <CardInfo>
              <div className="flex">
                <Button
                  type="primary"
                  onClick={() => router.back()}
                  className="button-ctx  w-[25%]"
                  disabled={loadingVerify || loadingMail || loadingActive}
                >
                  Regresar
                </Button>
                <Button
                  type="primary"
                  className="button-ctx w-full"
                  disabled={infoUser?.emailValidated || !infoUser?.active}
                  onClick={emailVerify}
                  loading={loadingVerify}
                >
                  Verificar Cuenta
                </Button>
                <Button
                  type="primary"
                  className="button-ctx w-full"
                  onClick={changePasswordClient}
                  loading={loadingMail}
                  disabled={!infoUser?.active}
                >
                  Cambiar Contraseña
                </Button>

                <Button
                  type="primary"
                  className="button-ctx-delete mt-0"
                  onClick={modifyClient}
                  loading={loadingActive}
                >
                  {infoUser?.active
                    ? 'Desactivar Cliente'
                    : 'Reactivar Cliente'}
                </Button>
              </div>
            </CardInfo>
          </div>
          <Row gutter={[24, 16]} className="mt-[5.5rem]">
            <Col span={10}>
              <div className="flex items-center justify-between px-4">
                <h1 className="color-main pb-5 pt-4 text-3xl">Cliente</h1>
              </div>

              <FormClientAdmin client={infoUser} newsLetter={newsLetter} />
            </Col>
            <Col span={14}>
              <div className="flex items-center justify-between px-4">
                <h1 className="color-main pb-5 pt-4 text-3xl">Direcciones</h1>
              </div>
              <div>
                <CardInfo title="">
                  <Row>
                    <Col
                      span={24}
                      className=" max-h-[12.8rem] min-h-[12.8rem] overflow-auto"
                    >
                      {direction.length > 0 ? (
                        direction.map(d => (
                          <DirectionCard
                            key={d.id}
                            direction={d}
                            controls={false}
                          />
                        ))
                      ) : (
                        <div className="p-20 text-center text-xl font-bold text-[#ccc]">
                          <p>Este usuario no tiene direcciones aun</p>
                        </div>
                      )}
                    </Col>
                  </Row>
                </CardInfo>
              </div>
            </Col>
            <Col span={24}>
              <div className="flex items-center justify-between px-4">
                <h1 className="color-main pb-5 pt-4 text-3xl">
                  Historial de pedidos
                </h1>
              </div>
              <div className={`${!loading ? 'hidden' : 'block'} grid p-20`}>
                <Spin size={'large'} />
              </div>
              <div className={`px-12 ${loading ? 'hidden' : 'block'}`}>
                {orders.length > 0 ? (
                  orders.map(order => (
                    <CardOrderAdmin order={order} key={order.id} />
                  ))
                ) : (
                  <CardInfo>
                    <div className="p-20 text-center text-xl font-bold text-[#ccc]">
                      <p>Este usuario no tiene pedidos aun</p>
                    </div>
                  </CardInfo>
                )}
              </div>
            </Col>
          </Row>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default addClient;
