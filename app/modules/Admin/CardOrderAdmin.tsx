import { Button, Col, Modal, Row, Statistic } from 'antd';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { getOrderClientAdmin } from '../../interfaces/Response/Admin/OrderClient';
import { CtxSelector } from '../../services/redux/store';
import { formatNumber, getDate } from '../../services/utils';
import { CardInfo } from '../shared';
import mercadoPagoImg from '/public/img/Mercado_Pago-OGfnlreJZ_brandlogos.net.svg';
import noImg from '/public/img/noSampler1.jpg';
import paypalImg from '/public/img/paypal-logo-tcalm.svg';
import clickPick from '/public/img/ClickPick.png'
import { ModalGenBilling } from '../Orders/ModalGenBilling';
import { BillingService } from '../../services/Billing/billing-service';

interface Props {
  order: getOrderClientAdmin;
}

export const CardOrderAdmin: FC<Props> = ({ order }) => {
  const billingService = new BillingService();
  const router = useRouter();
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [orderS, setOrderS] = useState(false);
  const userInfo = CtxSelector((state: { session: any }) => state.session);

  useEffect(() => {
    if (order.orderBilling) {
      setOrderS(true);
    }
  }, [order]);

  const openBillingModal = async () => {
    if (orderS) {
      setLoading(true);
      await billingService.billingSendEmail(order.id);
      setLoading(false);
    } else {
      if (userInfo.info.rfc) {
        setShowModal(true);
      } else {
        toast.info('Registre su RFC para continuar', { theme: 'colored' });
        router.push('/fiscal-info');
      }
    }
  };

  const handleCancel = () => {
    setShowModal(false);
  };

  const onSubmit = () => {
    setOrderS(true);
    setShowModal(false);
  };

  return (
    <CardInfo
      key={order.id}
      title={
        <div className="flex justify-between">
          <div className="mt-2 flex">
            <p className="mr-2 ml-4">
              <span className="text-main underline">{`#${order.number}`}</span>
            </p>
            <p>{' - '}</p>
            <p className="ml-2">{order.statusHistory[order.statusHistory.length -1].status}</p>
          </div>
          <Button
            className="button-ctx ml-10"
            onClick={openBillingModal}
            loading={loading}
          >
            {orderS ? 'Reenviar factura' : 'Generar factura'}
          </Button>
        </div>
      }
    >
      <Row gutter={[12, 24]} className="border-b-[1px]   border-b-[#ccc]  pb-4">
        <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
          <Statistic
            title="Pedido Realizado"
            value={getDate(
              order.statusHistory[order.statusHistory.length - 1].date
            )}
            valueStyle={{ fontSize: '15px' }}
          />
        </Col>
        <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
          {order.deliveryMethod === 'Envío a domicilio' && (
            <Statistic
              title="Envío"
              value={`${order.orderShipping?.municipality}, ${order.orderShipping?.state}`}
              valueStyle={{ fontSize: '15px' }}
            />
          )}
          {order.deliveryMethod === 'Recoger en sucursal' && (

            <div className="text-start">
            <div className="ant-statistic-title">Recolección</div>
            <img
              width={75}
              src={clickPick.src}
              alt="click&pick"
            />
          </div>
          )}
        </Col>
        <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
          <Statistic
            title="Total"
            value={`$${formatNumber(order.total)}`}
            valueStyle={{ fontSize: '15px' }}
          />
        </Col>
        <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
          <div className="text-start">
            <div className="ant-statistic-title">Método de pago</div>
            <img
              width={75}
              src={
                order.paymentMethod === 'PAYPAL'
                  ? paypalImg.src
                  : mercadoPagoImg.src
              }
              alt="paypal"
            />
          </div>
        </Col>
        {order.deliveryMethod === 'Envío a domicilio' && (
          <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
            <Statistic
              title="Paquetería"
              value={order.orderShipping?.provider}
              valueStyle={{ fontSize: '15px' }}
            />
          </Col>
        )}
        {order.deliveryMethod === 'Envío a domicilio' && (
          <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
            <Statistic
              title="Costo del Envío"
              value={`$${formatNumber(order.orderShipping?.price)}`}
              valueStyle={{ fontSize: '15px' }}
            />
          </Col>
        )}
        <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
          <div className="text-start">
            {order.deliveryMethod === 'Envío a domicilio' && (
              <div className="ant-statistic-title">Número de guía</div>
            )}
            {order.orderShipping?.trackingUrlProvider ? (
              <a
                rel="noreferrer"
                href={order.orderShipping.trackingUrlProvider}
                className="font-bold text-main underline"
                target="_blank"
              >
                {order.orderShipping.trackingNumber}
              </a>
            ) : (
              <p>{order.orderShipping?.trackingNumber}</p>
            )}
          </div>
        </Col>
      </Row>
      <div className="mt-4 max-h-[14rem] overflow-auto">
        {order.products.map(order => (
          <Row key={order.id} className="mb-8 " gutter={[0, { xs: 16, lg: 0 }]}>
            <Col xs={{ span: 24 }} xxl={{ span: 6 }} span={6}>
              <div className="flex flex-col items-center xl:block xl:text-start">
                <div className="ant-statistic-title text-start">Detalle</div>
                <img
                  width={70}
                  src={order.image}
                  alt="paypal"
                  style={{ borderRadius: '50px' }}
                  onError={({ currentTarget }) => {
                    currentTarget.src = noImg.src;
                  }}
                />
              </div>
            </Col>
            <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
              <Statistic
                title="Nombre"
                value={`${order.name}`}
                valueStyle={{ fontSize: '15px' }}
              />
            </Col>
            {order.color && (
              <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
                <Statistic
                  title="Color"
                  value={`${order.color}`}
                  valueStyle={{ fontSize: '15px' }}
                />
              </Col>
            )}
            <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
              <Statistic
                title="Cantidad"
                value={`${order.amount}`}
                valueStyle={{ fontSize: '15px' }}
              />
            </Col>
            {order.sale && (
              <Col
                xs={{ span: 12, offset: 0 }}
                xxl={{ span: 6, offset: 6 }}
                span={6}
                offset={6}
              >
                <Statistic
                  title="Medida de venta"
                  value={`${order.sale}`}
                  valueStyle={{ fontSize: '15px' }}
                />
              </Col>
            )}
            <Col xs={{ span: 12 }} xxl={{ span: 6 }} span={6}>
              <Statistic
                title="Subtotal"
                value={`$${formatNumber(order.sellPrice)}`}
                valueStyle={{ fontSize: '15px' }}
              />
            </Col>
          </Row>
        ))}
      </div>
      {userInfo.info?.rfc && (
        <Modal
          title="Seleccione"
          visible={showModal}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose={true}
        >
          <ModalGenBilling
            rfc={userInfo.info.rfc}
            order={order.id}
            onSubmit={onSubmit}
          />
        </Modal>
      )}
    </CardInfo>
  );
};
