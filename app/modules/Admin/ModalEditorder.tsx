import { Button, Col, Modal, Row, Skeleton, Statistic, Tag } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { getOrderClientAdmin } from '../../interfaces/Response/Admin/OrderClient';
import { editOrderAdmin } from '../../services/Order/CalculatePrice';
import { toast } from 'react-toastify';
import { formatNumber, getDate } from '../../services/utils';
import ModalStatus from './ModalStatus';
import { CtxDispatch } from '../../services/redux/store';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../services/redux/actions';
import { boxCalculate } from '../../services/admin/box';
import Icon, {
  CustomIconComponentProps
} from '@ant-design/icons/lib/components/Icon';
import StatisticManual from '../shared/StatisticManual/StatisticManual';

interface Props {
  order: getOrderClientAdmin;
  onSubmit: () => void;
}

export const ModalEditorder: FC<Props> = ({ order, onSubmit }) => {
  const [modal, setModal] = useState(false);
  const [box, setBox] = useState<any>([]);
  const dispatch = CtxDispatch();
  const [loading, setLoading] = useState(true);

  const boxSvg = () => (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="15"
      height="15"
      viewBox="0 0 35.992 36"
    >
      <path
        id="cajas"
        d="M3.045,34.443V12.3a1.942,1.942,0,0,1-1.212-.9A2.964,2.964,0,0,1,1.25,9.645V3.934a2.643,2.643,0,0,1,.81-1.871A2.526,2.526,0,0,1,3.946,1.23H34.538a2.609,2.609,0,0,1,1.871.833,2.554,2.554,0,0,1,.833,1.871V9.645a2.981,2.981,0,0,1-.583,1.757,1.915,1.915,0,0,1-1.212.9V34.443a2.731,2.731,0,0,1-.833,1.909,2.524,2.524,0,0,1-1.871.879h-27a2.5,2.5,0,0,1-1.886-.879,2.768,2.768,0,0,1-.81-1.909ZM34.538,9.652V3.934H3.939V9.645h30.6ZM13.838,21.8h10.8l-10.8,7.832V21.8Z"
        transform="translate(-1.25 -1.23)"
        fill="currentColor"
      />
    </svg>
  );
  const BoxIcon = (props: Partial<CustomIconComponentProps>) => (
    <Icon component={boxSvg} {...props} />
  );
  const urlPdf = 'https://cc.paquetexpress.com.mx:8082/wsReportPaquetexpress/GenCartaPorte?trackingNoGen='
  const urlSize = '&measure=4x6'
  const onNextStatus = async (status: string) => {
    dispatch(LoaderActionsShow());
    const response = await editOrderAdmin(order.id, status);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }
    dispatch(LoaderActionsHide());
    setModal(false);
  };

  const calculateBox = async () => {
    setLoading(true);
    const response = await boxCalculate(order.id);
    if (response) {
      setBox(response);
    }
    setLoading(false);
  };

  useEffect(() => {
    calculateBox();
  }, []);

  const getPhoneNumber = () => {
    const firstPart = order.client.phone.slice(0, 5);
    const secondPart = order.client.phone.slice(5, 10);

    return `${order.client.countryCode.code} ${firstPart} ${secondPart}`;
  };

  const getStatus = () => {
    
    const currentStatus = order.statusHistory[order.statusHistory.length-1].status;

    switch (currentStatus) {
      case 'En revisión':
        return (
          <Tag
            color="volcano"
            className="cursor-pointer text-center sm:w-24 lg:w-[10rem]"
          >
            {currentStatus}
          </Tag>
        );
      case 'Preparando los productos':
        return (
          <Tag
            color="orange"
            className="cursor-pointer text-center sm:w-24 lg:w-[10rem]"
          >
            En Preparación
          </Tag>
        );

      case 'Enviado':
        return (
          <Tag
            color="cyan"
            className=" cursor-pointer text-center sm:w-24 lg:w-[10rem]"
          >
            {currentStatus}
          </Tag>
        );

      case 'Listo para recoger en sucursal':
        return (
          <Tag
            color="blue"
            className="cursor-pointer text-center sm:w-24 lg:w-[10rem]"
          >
            En Sucursal
          </Tag>
        );

      case 'Entregado':
        return (
          <Tag
            color="blue"
            className=" cursor-pointer text-center sm:w-24 lg:w-[10rem]"
          >
            {currentStatus}
          </Tag>
        );
      default:
        break;
    }
  };

  return (
    <div>
      <div className="pb-4" key={order.id}>
        <Row
          gutter={[12, 24]}
          className="p-4"
          style={{
            backgroundColor: '#F6F6F7',
            borderBottom: '1px solid #D8D8D8'
          }}
        >
          <Col span={6}>
            <Statistic
              title="Cliente"
              value={`${order.client.name} ${order.client.firstLastname} ${order.client.secondLastname ? order.client.secondLastname : ''
                }`}
              valueStyle={{ fontSize: '15px' }}
            />
          </Col>
          <Col span={6}>
            {order.deliveryMethod === 'Envío a domicilio' ? (
              <Statistic
                title="Dirección"
                value={`${order.orderShipping?.streetName} #${order.orderShipping?.numExt} ${order.orderShipping?.municipality} ${order.orderShipping?.zipCode} ${order.orderShipping?.suburb}, ${order.orderShipping?.state}`}
                valueStyle={{ fontSize: '15px' }}
              />
            ) : (
              <Statistic
                title="Envío a domicilio"
                value={'Recoger en tienda'}
                valueStyle={{ fontSize: '15px' }}
              />
            )}
          </Col>
          <Col span={4}>
            <Statistic
              title="Pedido Realizado"
              value={getDate(
                order.statusHistory[0].date
              )}
              valueStyle={{ fontSize: '15px' }}
            />
          </Col>
          <Col span={4}>
            <Statistic
              title="Total"
              value={`$${formatNumber(order.total)}`}
              valueStyle={{ fontSize: '15px' }}
            />
          </Col>
          <Col span={4}>
            <div className="text-start">
              <div className="ant-statistic-title">Estado</div>
              <div className="mt-1 flex justify-center">
                <Button
                  style={{ border: 'none' }}
                  type="text"
                  onClick={() => setModal(true)}
                >
                  {getStatus()}
                </Button>
              </div>
            </div>
          </Col>
          <Col span={6}>
            <Statistic
              title="Teléfono"
              value={`${getPhoneNumber()}`}
              valueStyle={{ fontSize: '15px' }}
            />
            <Statistic
              title="Email"
              value={order.client.email}
              valueStyle={{ fontSize: '15px' }}
            />
          </Col>
    
          {order.deliveryMethod === 'Envío a domicilio' && (
            <Col span={4}>
              <Statistic
                title="Paquetería"
                value={order.orderShipping?.provider}
                valueStyle={{ fontSize: '15px' }}
              />
            </Col>
          )}
          <Col span={4}>
            <Statistic
              title="Pago"
              value={
                order.paymentMethod === 'MERCADO_PAGO'
                  ? 'Mercado Pago'
                  : 'Paypal'
              }
              valueStyle={{ fontSize: '15px' }}
            />
          </Col>
          {order.deliveryMethod === 'Envío a domicilio' && (
            <>
              <Col span={4}>
                <Statistic
                  title="Costo del Envío"
                  value={`$${formatNumber(order.orderShipping?.price)}`}
                  valueStyle={{ fontSize: '15px' }}
                />
              </Col>
              <Col span={6} />

              <Col span={6}>
                <StatisticManual
                  title="Número de guía"
                  child={
                    <p
                      className="font-bold text-main underline"
                      style={{ color: '#252525' }}
                    >
                      {order.orderShipping?.provider === "PAQUETEXPRESS" ? (
                        <a href={`${urlPdf}${order.orderShipping?.trackingNumber}${urlSize}`}>
                          Guia-PDF {order.orderShipping?.trackingNumber}
                        </a>
                      ):(order.orderShipping?.trackingNumber)}
                    </p>
                  }
                />
              </Col>

              <Col span={6}>
                <StatisticManual
                  title="Url del proveedor"
                  child={
                    order.orderShipping?.trackingUrlProvider ? (
                      <a
                        rel="noreferrer"
                        className="font-bold text-main underline"
                        href={order.orderShipping?.trackingUrlProvider}
                        target="_blank"
                      >
                        Rastrear Envío
                      </a>
                    ) : (
                      <p className=" font-bold">-</p>
                    )
                  }
                />
              </Col>
            </>
          )}
        </Row>
        <div className="mt-4 max-h-[30rem] overflow-auto overflow-x-hidden ">
          {order.products.map(product => (
            <div key={product.id} className="mb-8 flex">
              <div>
                {' '}
                <div className="px-[5rem] text-start">
                  <div className="ant-statistic-title ">Detalle</div>
                  <img
                    width={180}
                    src={product.image}
                    alt="paypal"
                    style={{ borderRadius: '8px' }}
                  />
                </div>
              </div>
              <Row className="mb-4  h-full w-full" gutter={[12, 24]}>
                <Col span={6}>
                  <Statistic
                    title="Nombre"
                    value={`${product.name}`}
                    valueStyle={{ fontSize: '15px' }}
                  />
                </Col>
                <Col span={4}>
                  <Statistic
                    title="Color"
                    value={`${product.color}`}
                    valueStyle={{ fontSize: '15px' }}
                  />
                </Col>
                <Col span={4}>
                  <Statistic
                    title="Cantidad"
                    value={`${product.amount}`}
                    valueStyle={{ fontSize: '15px' }}
                  />
                </Col>
                <Col span={4}>
                  <Statistic
                    title="Venta"
                    value={`${product.sale}`}
                    valueStyle={{ fontSize: '15px' }}
                  />
                </Col>
                <Col span={4}>
                  <Statistic
                    title="Subtotal"
                    value={`$${formatNumber(product.sellPrice)}`}
                    valueStyle={{ fontSize: '15px' }}
                  />
                </Col>
                                
                <Col span={7}>
                  <p>Embalaje</p>
                  {loading ? (
                    <Skeleton.Input active={true} size="small" />
                  ) : (
                    <div>
                      {box.map((border: any) => (
                        <div key={border.variant} className="mt-1">
                          {border.clothName === product.name &&
                            border.colorName === product.color &&
                            border.boxes.map((boxm: any) =>
                              boxm.boxes.active ? (
                                <div
                                  key={boxm.boxes.id}
                                  className="flex flex-row align-middle"
                                >
                                  <div
                                    key={boxm.boxes.id}
                                    className="flex flex-row align-middle"
                                  >
                                    <BoxIcon
                                    style={{ color: boxm.boxes.colorCode }}
                                  />
                                    <p >[ {boxm.amount} ] </p>
                                  </div>
                                  <p className="mr-4 ml-1">{boxm.boxes.name}</p>
                                </div>
                              ) : (
                                <div className="flex flex-row align-middle">
                                  <BoxIcon />
                                  <p className="mr-4 ml-1">
                                    No hay cajas activas
                                  </p>
                                </div>
                              )
                            )}
                        </div>
                      ))}
                    </div>
                  )}
                </Col>

                <Col span={7}>
                  <p>Medidas</p>
                  {loading ? (
                    <Skeleton.Input active={true} size="small" />
                  ) : (
                    <div>
                      {box.map((border: any) => (
                        <div key={border.variant} className="mt-1">
                          {border.clothName === product.name &&
                            border.colorName === product.color &&
                            border.boxes.map((boxm: any) =>
                              boxm.boxes.active ? (
                                <div
                                  key={boxm.boxes.id}
                                  className="flex flex-row align-middle"
                                >
                                  <p className="mr-4 ml-1">{boxm.boxes.height} X {boxm.boxes.depth} X {boxm.boxes.width}</p>

                                </div>
                              ) : (
                                <div className="flex flex-row align-middle">
                                  <BoxIcon />
                                  <p className="mr-4 ml-1">
                                    No hay cajas activas
                                  </p>
                                </div>
                              )
                            )}
                        </div>
                      ))}
                    </div>
                  )}
                </Col>

              </Row>
            </div>
          ))}
        </div>
      </div>
      {modal && (
        <Modal
          visible={modal}
          closable={false}
          mask={false}
          onCancel={() => setModal(false)}
          style={{ top: '230px', left: '525px' }}
          footer={null}
          width={'14rem'}
        >
          <ModalStatus
            currentStatus={order.statusHistory[order.statusHistory.length-1].status}
            deliveryMethod={order.deliveryMethod}
            onClick={onNextStatus}
          />
        </Modal>
      )}
    </div>
  );
};
