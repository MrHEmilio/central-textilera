import Image from 'next/image';
import { FC } from 'react';
import { GetOrders } from '../../../interfaces/Response/Orders/Orders';
import { CardInfo, PriceLabel } from '../../shared';
import { OrderClothInfo } from './OrderClothInfo';
import mercadoPagoImg from '/public/img/Mercado_Pago-OGfnlreJZ_brandlogos.net.svg';
import paypalImg from '/public/img/paypal-logo-tcalm.svg';
import noImgeSampler from '/public/img/noSampler1.jpg';

interface Props {
  order: GetOrders;
}

export const OrderCard: FC<Props> = ({ order }) => {
  const { total, orderShipping, products, status, paymentMethod } = order;

  // const RenderTitle = () => (
  //   <div className="text-lg font-bold text-slate-400">23 de Julio</div>
  // );
  return (
    <CardInfo>
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-4">
        <div className="clothOrderContainer">
          {products.map(cloth => (
            <OrderClothInfo
              key={cloth.id}
              img={cloth.image || noImgeSampler.src}
              nameColor={cloth.color}
              status={status}
              nameCloth={cloth.name}
            />
          ))}
        </div>
        <div className="flex flex-col">
          <div className="mb-3 border-b-2 border-gray-500 text-lg font-bold text-gray-900">
            Detalle
          </div>
          {/* <div className="grid grid-cols-2 justify-between">
            <p>Subtotal</p>
            <div className="text-end">
              <PriceLabel price={211} />
            </div>
          </div>
          <div className="grid grid-cols-2 justify-between border-b-2 border-gray-500">
            <p>Envíos</p>
            <div className="text-end">
              <PriceLabel price={orderShipping.shippingPrice} />
            </div>
          </div> */}
          <div className="mt-2 grid grid-cols-2 justify-between">
            <p>Total</p>
            <div className="flex w-full items-center">
              <span className="flex-1 pr-2 text-end">MXN</span>
              <PriceLabel className="text-lg font-bold" price={total} />
            </div>
          </div>
        </div>
        <div className="flex flex-col items-center">
          <div className="text-lg font-bold text-gray-900">Envío</div>
          <p className="mt-5">
            {orderShipping?.municipality ||
              'Recoger en sucursal'}
          </p>
        </div>
        <div className="flex flex-col ">
          <div className="mb-6 text-center text-lg font-bold text-gray-900">
            Pago
          </div>
          <div className="text-start">
            <Image
              width={200}
              layout="responsive"
              height={30}
              src={
                paymentMethod === 'PAYPAL' ? paypalImg.src : mercadoPagoImg.src
              }
              alt="paypal"
            />
          </div>
        </div>
      </div>
    </CardInfo>
  );
};
