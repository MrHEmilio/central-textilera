import Icon, {
  CustomIconComponentProps
} from '@ant-design/icons/lib/components/Icon';
import { Breadcrumb, Button, Modal, Spin, Steps } from 'antd';
import { NextPage } from 'next';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import { useGetRequest } from '../../app/Hooks/useGetRequest';
import { useReady } from '../../app/Hooks/useReady';
import { ModalGenBilling } from '../../app/modules/Orders/ModalGenBilling';
import { CardInfo, MainLayout } from '../../app/modules/shared';
import { BillingService } from '../../app/services/Billing/billing-service';
import { formatNumber } from '../../app/services/utils';
import noImage from '/public/img/noSampler1.jpg';
import { CtxDispatch, CtxSelector } from '../../app/services/redux/store';
import { closeSesionService } from '../../app/services/auth/authService';
import { LogOut } from '../../app/services/redux/actions/SessionActions';
import { GetOrderResponse } from '../../app/services';

const RevSvg = () => (
  <svg
    id="Capa_1"
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 100 100"
    width="1em"
    height="1em"
    fill="white"
  >
    <ellipse
      width="1em"
      height="1em"
      fill="currentColor"
      id="Elipse_1352"
      className="cls-2"
      cx="50"
      cy="50"
      rx="48.56"
      ry="46"
    />
    <g id="Grupo_10865">
      <path
        id="receipt-solid"
        className="cls-1"
        d="M25.71,18.95c1.01-.46,2.19-.3,3.03,.43l4.79,4.1,4.79-4.1c1.06-.91,2.64-.91,3.7,0l4.79,4.1,4.79-4.1c1.06-.91,2.64-.91,3.7,0l4.78,4.1,4.8-4.1c1.19-1.02,2.99-.89,4.01,.31,.44,.52,.69,1.17,.69,1.85v55.01c0,1.57-1.27,2.84-2.84,2.84-.68,0-1.34-.24-1.85-.69l-4.8-4.1-4.79,4.1c-1.06,.91-2.64,.91-3.7,0l-4.79-4.1-4.79,4.1c-1.06,.91-2.64,.91-3.7,0l-4.78-4.1-4.79,4.1c-1.19,1.02-2.99,.89-4.01-.31-.44-.52-.69-1.17-.69-1.85V21.54c0-1.11,.65-2.12,1.66-2.58Zm9.72,16.81c-1.05,0-1.9,.85-1.9,1.9s.85,1.9,1.9,1.9h22.76c1.05,0,1.9-.85,1.9-1.9s-.85-1.9-1.9-1.9h-22.76Zm-1.9,24.66c0,1.05,.85,1.89,1.9,1.9h22.76c1.05,0,1.9-.85,1.9-1.9s-.85-1.9-1.9-1.9h-22.76c-1.05,0-1.89,.85-1.9,1.9h0Zm1.9-13.28c-1.05,0-1.9,.85-1.9,1.9s.85,1.9,1.9,1.9h22.76c1.05,0,1.9-.85,1.9-1.9s-.85-1.9-1.9-1.9h-22.76Z"
      />
      <g id="Grupo_10864">
        <ellipse
          width="1em"
          height="1em"
          fill="currentColor"
          id="Elipse_1358"
          className="cls-2"
          cx="69.33"
          cy="67.89"
          rx="21.72"
          ry="20.44"
        />
        <path
          id="magnifying-glass-solid"
          className="cls-1"
          d="M81.74,66.7c0,2.98-.94,5.89-2.7,8.3l3.61,2.84c.83,.75,.16,1.11-.83,2.04-.54,.51-1.11,.99-1.71,1.43l-4.13-3.24c-6.27,4.58-15.07,3.21-19.66-3.06s-3.21-15.07,3.06-19.66c6.27-4.58,15.07-3.21,19.66,3.06,1.76,2.41,2.71,5.31,2.71,8.3h0Zm-14.06,9.73c5.38,0,9.74-4.36,9.74-9.74s-4.36-9.74-9.74-9.74-9.74,4.36-9.74,9.74h0c0,5.38,4.36,9.74,9.74,9.74Z"
        />
      </g>
    </g>
  </svg>
);
const PrepSvg = () => (
  <svg
    id="Capa_1"
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 100 100"
    width="1em"
    height="1em"
    fill="white"
  >
    <g id="Grupo_10862">
      <ellipse
        id="Elipse_1356"
        cx="50"
        cy="50"
        rx="48.56"
        ry="46"
        fill="currentColor"
      />
      <path
        id="box-open-solid"
        d="M24.82,28.88c.29-.6,.94-.94,1.6-.85l23.99,3,23.99-3c.66-.08,1.3,.26,1.6,.85l4.09,8.18c.77,1.55,.15,3.44-1.41,4.21-.17,.09-.35,.16-.53,.21l-16.02,4.57c-1.36,.39-2.82-.19-3.55-1.41l-8.18-13.62-8.18,13.62c-.73,1.22-2.18,1.8-3.55,1.41l-16-4.58c-1.66-.47-2.63-2.21-2.15-3.87,.05-.19,.13-.37,.21-.55l4.08-8.17Zm25.7,8.42l5.38,8.96c1.46,2.44,4.38,3.59,7.11,2.8l12.5-3.57v16.37c0,2.16-1.47,4.04-3.57,4.57l-20,5c-1,.25-2.04,.25-3.04,0l-20-5c-2.09-.53-3.56-2.42-3.57-4.58v-16.37l12.51,3.58c2.73,.78,5.64-.37,7.11-2.8l5.37-8.96h.22Z"
      />
    </g>
  </svg>
);
const SendSvg = () => (
  <svg
    id="Capa_1"
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 100 100"
    width="1em"
    height="1em"
    fill="white"
  >
    <g id="Grupo_10863">
      <ellipse
        width="1em"
        height="1em"
        fill="currentColor"
        id="Elipse_1357"
        cx="50"
        cy="50"
        rx="48.56"
        ry="46"
      />
      <path
        id="truck-fast-solid"
        d="M31.37,27.94c-2.34,0-4.24,1.9-4.24,4.24v4.24h-4.24c-.78,0-1.41,.63-1.41,1.42,0,.78,.63,1.41,1.41,1.41h22.63c.78,0,1.41,.63,1.41,1.42,0,.78-.63,1.41-1.41,1.41H25.71c-.78,0-1.41,.63-1.42,1.41,0,.78,.63,1.41,1.41,1.42h16.98c.78,0,1.41,.63,1.42,1.41,0,.78-.63,1.41-1.41,1.42H22.88c-.78,0-1.41,.63-1.41,1.41s.63,1.41,1.41,1.41h16.98c.78,0,1.41,.63,1.41,1.41s-.63,1.41-1.41,1.41h-12.73v11.32c0,4.69,3.8,8.49,8.49,8.49s8.49-3.8,8.49-8.49h11.32c0,4.69,3.8,8.49,8.49,8.49s8.49-3.8,8.49-8.49h2.83c1.56,0,2.83-1.27,2.83-2.83,0-1.56-1.27-2.83-2.83-2.83v-10.14c0-1.5-.59-2.94-1.65-4l-6.83-6.83c-1.06-1.06-2.5-1.66-4-1.65h-4.48v-4.24c0-2.34-1.9-4.24-4.24-4.24H31.37Zm38.19,20.98v1.65h-11.32v-8.49h4.48l6.83,6.83Zm-33.95,11.55c2.34,0,4.24,1.9,4.24,4.24s-1.9,4.24-4.24,4.24-4.24-1.9-4.24-4.24,1.9-4.24,4.24-4.24h0Zm24.05,4.24c0-2.34,1.89-4.25,4.24-4.25,2.34,0,4.25,1.89,4.25,4.24,0,2.34-1.89,4.25-4.24,4.25,0,0,0,0,0,0-2.34,0-4.24-1.9-4.24-4.24Z"
      />
    </g>
  </svg>
);
const PickSvg = () => (
  <svg
    id="Capa_1"
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 100 100"
    width="1em"
    height="1em"
    fill="white"
  >
    <g id="Grupo_10866">
      <ellipse
        width="1em"
        height="1em"
        fill="currentColor"
        id="Elipse_1355"
        cx="50"
        cy="50"
        rx="48.56"
        ry="46"
      />
      <path
        id="dolly-solid"
        d="M20.25,31.3c0-1.84,1.49-3.34,3.33-3.34,0,0,0,0,0,0h7.62c2.88,0,5.43,1.84,6.34,4.58l9.62,28.85c3.03,.05,5.87,1.47,7.73,3.87l21.11-7.04c1.75-.59,3.64,.36,4.23,2.11,.59,1.75-.36,3.64-2.11,4.23h0l-21.11,7.04c-.1,5.54-4.67,9.94-10.21,9.84-5.54-.1-9.94-4.67-9.84-10.21,.06-3.03,1.48-5.87,3.86-7.73l-9.62-28.85h-7.62c-1.84,0-3.34-1.49-3.34-3.33,0,0,0,0,0,0Zm25.57,10.71c-.58-1.75,.37-3.63,2.12-4.21,0,0,.01,0,.02,0l4.77-1.55,2.07,6.36,6.36-2.07-2.07-6.36,4.77-1.55c1.75-.57,3.63,.38,4.21,2.13,0,0,0,0,0,.01l5.16,15.9c.57,1.75-.38,3.63-2.13,4.21,0,0,0,0-.01,0l-15.91,5.16c-1.75,.57-3.63-.38-4.21-2.13,0,0,0,0,0-.01l-5.16-15.89Z"
      />
    </g>
  </svg>
);
const DeliSvg = () => (
  <svg
    id="Capa_1"
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 100 100"
    width="1em"
    height="1em"
    fill="white"
  >
    <g id="Grupo_10857">
      <ellipse
        width="1em"
        height="1em"
        fill="currentColor"
        id="Elipse_1354"
        cx="50"
        cy="50"
        rx="48.56"
        ry="46"
      />
    </g>
    <path
      id="box-solid"
      d="M34.18,31.97l-4.63,9.26h18.98v-11.68h-10.43c-1.66,0-3.18,.94-3.93,2.42Zm17.28,9.26h18.99l-4.63-9.26c-.75-1.48-2.27-2.42-3.93-2.42h-10.43v11.68Zm18.98,2.92H29.56v20.44c0,3.22,2.62,5.84,5.84,5.84h29.21c3.22,0,5.84-2.62,5.84-5.84v-20.44Z"
    />
    <g id="Grupo_10859">
      <circle
        width="1em"
        height="1em"
        fill="grey"
        id="Elipse_1354-2"
        cx="67.25"
        cy="67.25"
        r="13.42"
      />
    </g>
    <path
      id="circle-check-solid"
      d="M66.98,77.88c6.02,0,10.9-4.88,10.9-10.9s-4.88-10.9-10.9-10.9-10.9,4.88-10.9,10.9c0,0,0,0,0,0,0,6.02,4.88,10.9,10.9,10.9Zm4.82-12.9l-5.45,5.45c-.4,.4-1.04,.4-1.44,0l-2.73-2.72c-.4-.4-.4-1.05,0-1.45s1.05-.4,1.45,0l2,2,4.73-4.73c.42-.38,1.06-.35,1.44,.07,.35,.39,.35,.98,0,1.37h0Z"
    />
  </svg>
);
const RevIcon = (props: Partial<CustomIconComponentProps>) => (<Icon component={RevSvg} {...props} />);
const PrepIcon = (props: Partial<CustomIconComponentProps>) => (<Icon component={PrepSvg} {...props} />);
const SendIcon = (props: Partial<CustomIconComponentProps>) => (<Icon component={SendSvg} {...props} />);
const PickIcon = (props: Partial<CustomIconComponentProps>) => (<Icon component={PickSvg} {...props} />);
const DeliIcon = (props: Partial<CustomIconComponentProps>) => (<Icon component={DeliSvg} {...props} />);

const OrderTracking: NextPage = () => {
  const router = useRouter();
  const dispatch = CtxDispatch();
  const billingService = new BillingService();
  const [orderId, setOrderId] = useState(router.query.tracking);
  const [orderS, setOrderS] = useState(false);

   /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const [orderDate, setorderDate] = useState<any>();
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [url, setUrl] = useState('');
  const ready = useReady();
  
  const [order, loadingOrder] = useGetRequest<GetOrderResponse>(
    url,
    ready && !!orderId
  );

  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);

  useEffect(() => {
    if (userInfo.auth) {
      closeSession();
    }
    if (router.query.tracking) {
      if (router.query.tracking.length > 0 && ready) {
        setOrderId(router.query.tracking);
      }
    }
  }, [router, ready]);

  useEffect(() => {
    if (!ready || !orderId) return;
    setUrl(`/order/${orderId}`);
  }, [ready, orderId]);

  useEffect(() => {
    if (order) {
      if (order.orderBilling) {
        setOrderS(true);
      }
      const date = order.statusHistory[0].date.toString().substring(0, 10);
      const orderDate = new Date(date);
      const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      } as const;
      setorderDate(orderDate.toLocaleDateString('es-ES', options));
    }
  }, [order]);

  const closeSession = async () => {
    dispatch(LogOut());
    await closeSesionService();
    localStorage.removeItem('info');
    localStorage.removeItem('session');
  };

  const handleCancel = () => {
    setShowModal(false);
  };

  const onSubmit = () => {
    setOrderS(true);
    setShowModal(false);
  };

  const openBillingModal = async () => {
    if (order.orderBilling || orderS) {
      setLoading(true);
      await billingService.billingSendEmail(order.id);
      setLoading(false);
    } else {
      setShowModal(true);
    }
  };

  return (
    <MainLayout title="Pedidos" pageDescription="Historial de pedidos">
      <div className={`${!loadingOrder ? 'block' : 'hidden'} m-8 pb-5  pt-4`}>
        <div className="mt-8 mb-8">
          <Breadcrumb>
            <Breadcrumb.Item>
              <Link href="/">Inicio</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>Seguimiento del pedido</Breadcrumb.Item>
          </Breadcrumb>
        </div>
        {order && (
          <h1 className="color-main mb-8 flex self-center text-2xl lg:text-4xl">
            Rastreo de pedido: #{order.number}
          </h1>
        )}
        <div className="flex justify-center">
          <div className=" w-9/12 ">
            {order && (
              <CardInfo
                title={
                  <div className="grid-cols-3 sm:grid">
                    <div className="header-col flex justify-center">
                      <div>
                        <h2>Fecha del pedido</h2>
                        <p>{orderDate}</p>
                      </div>
                    </div>
                    <div className="header-col flex justify-center">
                      <div>
                        <h2>Total</h2>
                        <p>${formatNumber(order.total)}</p>
                      </div>
                    </div>
                    <div>
                      <Button
                        className="button-ctx ml-10"
                        onClick={openBillingModal}
                        loading={loading}
                      >
                        {orderS ? 'Reenviar factura' : 'Generar factura'}
                      </Button>
                    </div>
                  </div>
                }
              >
                <div className="max-h-[18rem] overflow-auto md:ml-10">
                  {order.products.map(product => (
                    <div key={product.id} className="mb-7 grid-cols-4 sm:grid">
                      <div className="header-col flex justify-center gap-y-10 md:justify-start">
                        <div>
                          <h2 className="mb-4">Detalle</h2>
                          <img
                            src={product.image}
                            alt=""
                            onError={e => {
                              e.currentTarget.onerror = null;
                              e.currentTarget.src = noImage.src;
                            }}
                          />
                        </div>
                      </div>
                      <div className="header-col flex justify-center md:justify-start">
                        <div>
                          <h2>Nombre:</h2>
                          <p>{product.name}</p>
                        </div>
                      </div>
                      <div className="header-col flex justify-center md:justify-start">
                        <div>
                          <h2>Color:</h2>
                          <p>{product.color}</p>
                        </div>
                      </div>
                      <div className="header-col flex justify-center md:justify-start">
                        <div>
                          <h2>Unidad(es):</h2>
                          <p>
                            {product.amount} {product.sale}(s)
                          </p>
                        </div>
                      </div>
                      <div className="header-col flex justify-center md:mt-4 md:justify-start">
                        <div>
                          <h2>Paquetería:</h2>
                          <p>{order.orderShipping?.provider}</p>
                        </div>
                      </div>
                      <div className="header-col flex justify-center md:mt-4 md:justify-start">
                        <div>
                          <h2>Número de guía:</h2>
                          {order.orderShipping?.trackingUrlProvider ? (
                            <a
                              rel="noreferrer"
                              href={order.orderShipping.trackingUrlProvider}
                              target="_blank"
                            >
                              {order.orderShipping.trackingNumber}
                            </a>
                          ) : (
                            <p>{order.orderShipping?.trackingNumber}</p>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="container-h-col-steps mt-10 md:ml-10 md:mr-10">
                  {order.deliveryMethod === 'Envío a domicilio' && (
                    <Steps
                      direction="horizontal"
                      current={order.statusHistory.length - 1}
                      labelPlacement="horizontal"
                      responsive
                    >
                      <Steps.Step
                        title={'En revisión'}
                        icon={
                          <RevIcon className="align-baseline text-[35px] md:align-baseline md:text-[50px]" />
                        }
                      />
                      <Steps.Step
                        title={
                          <p>
                            Preparando
                            <br />
                            los productos
                          </p>
                        }
                        icon={
                          <PrepIcon className="align-baseline text-[35px] md:align-baseline md:text-[50px]" />
                        }
                      />
                      <Steps.Step
                        title={'Enviado'}
                        icon={
                          <SendIcon className="align-baseline text-[35px] md:align-baseline md:text-[50px]" />
                        }
                      />
                      <Steps.Step
                        title={'Entregado'}
                        icon={
                          <DeliIcon className="align-baseline text-[35px] md:align-baseline md:text-[50px]" />
                        }
                      />
                    </Steps>
                  )}
                  {order.deliveryMethod === 'Recoger en sucursal' && (
                    <Steps
                      direction="horizontal"
                      current={order.statusHistory.length - 1}
                      labelPlacement="horizontal"
                    >
                      <Steps.Step
                        title={'En revisión'}
                        icon={
                          <RevIcon className=" text-[35px] md:text-[50px]" />
                        }
                      />
                      <Steps.Step
                        className="step-container"
                        title={
                          <p>
                            Preparando
                            <br />
                            los productos
                          </p>
                        }
                        icon={
                          <PrepIcon className=" text-[35px]  md:text-[50px]" />
                        }
                      />
                      <Steps.Step
                        className="step-container"
                        title={'Listo para recoger en sucursal'}
                        icon={
                          <PickIcon className=" text-[35px]  md:text-[50px]" />
                        }
                      />
                      <Steps.Step
                        title={'Entregado'}
                        icon={
                          <DeliIcon className=" text-[35px] md:text-[50px]" />
                        }
                      />
                    </Steps>
                  )}
                </div>
              </CardInfo>
            )}
          </div>
        </div>
      </div>
      <div
        className={`flex h-96 content-center justify-center py-24 ${
          loadingOrder ? 'block' : 'hidden'
        }`}
      >
        <div className="flex content-center justify-center py-24 ">
          <Spin size="large" className="" />
        </div>
      </div>
      {order && (
        <Modal
          title="Seleccione"
          visible={showModal}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose={true}
        >
          <ModalGenBilling
            order={order.id}
            onSubmit={onSubmit}
             /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
            client={order.client as any}
          />
        </Modal>
      )}
    </MainLayout>
  );
};

export default OrderTracking;
