import { LeftOutlined } from '@ant-design/icons';
import { Alert, Button, Col, Radio, Skeleton, Typography } from 'antd';
import Link from 'next/link';
import { useRouter } from 'next/router';
import React, { useEffect, useState } from 'react';
import useWindowDimensions from '../../app/Hooks/useWindowDimmensions';
import { PaymentCalculatePriceShippingFullRequest } from '../../app/interfaces/Request/payment/PaymentRequests';
import { ShippmentProviderResponse } from '../../app/interfaces/Response/Shippment';
import { CartState } from '../../app/interfaces/State/Cart';
import { mockArr } from '../../app/models';
import { PaymentLayout } from '../../app/modules/PaymentLayout';
import { CardInfo, ContactForm, PriceLabel } from '../../app/modules/shared';
import { getUserAddressForShipment } from '../../app/modules/Shipment/ShipmentUtils';
import { ClothSimple } from '../../app/services';
import { itemSampler } from '../../app/services/Order';
import { calculatePriceShippingFull } from '../../app/services/payment/PaymentServices';
import {
  saveCartOnStorage,
  setShippingInfo
} from '../../app/services/redux/actions/CartActions';
import { SetNotRegisterUserValidateStatus } from '../../app/services/redux/actions/NotRegisterUserActions';
import {
  CtxDispatch,
  CtxSelector,
  ReduxStore
} from '../../app/services/redux/store';
import { getUserInfo } from '../../app/services/utils';
import noShip from '/public/img/shippProviders/noShip.svg';
import style from '../../styles/Contact.module.css';


const { Text } = Typography;
const ShippmentPage = () => {
  const router = useRouter();
  const { isReady } = router;
  const dispatch = CtxDispatch();

  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
  const { shippingInfo }: CartState = CtxSelector(s => s.cart!);
  const [res, setRes] = useState<PaymentCalculatePriceShippingFullRequest>();
  const [response, setResp] = useState<ShippmentProviderResponse[]>();
  const [loading, setLoading] = useState<boolean>(true);
  const [providerSelected, setProviderSelected] = useState('');
  const { width } = useWindowDimensions();

  useEffect(() => {
    dispatch(SetNotRegisterUserValidateStatus(false));
    if (!isReady) return;
    const cart = localStorage.getItem('cart');
    const { notRegisterUser } = ReduxStore.getState();
    const userInfo = getUserInfo();
    if (!notRegisterUser.userContact && !userInfo) {
      router.replace('/payment');
      return;
    }
    if (cart) {
      const parseCart: CartState = JSON.parse(cart);
      const cloths: ClothSimple[] = [];
      const samplers: itemSampler[] = [];
      parseCart.products.map(cloth => {
        cloths.push({
          variant: cloth.variant,
          amount: cloth.quantity
        });
      });
      parseCart.samplers.map(sample => {
        if (sample.quantity)
          samplers.push({
            sampler: sample.id,
            amount: sample.quantity
          });
      });
      if (shippingInfo?.provider) {
        setProviderSelected(shippingInfo.serviceCode.toLowerCase());
      }
      const userInfoForPurchase = getUserAddressForShipment({
        cartState: parseCart,
        notRegisterUserState: notRegisterUser
      });
      setRes({
        latitude: userInfoForPurchase.lat,
        longitude: userInfoForPurchase.long,
        email: userInfoForPurchase.email,
        phone: userInfoForPurchase.phone,
        cloths: cloths,
        samplers: samplers,
        clientName: userInfoForPurchase.clientName,
        address: {
          longitude: userInfoForPurchase.long,
          latitude: userInfoForPurchase.lat,
          city: userInfoForPurchase.city,
          state: userInfoForPurchase.state,
          numExt: userInfoForPurchase.numExt,
          numInt: userInfoForPurchase.numInt,
          suburb: userInfoForPurchase.suburb,
          country: userInfoForPurchase.country,
          zipCode: userInfoForPurchase.zipcode,
          references: userInfoForPurchase.refs,
          streetName: userInfoForPurchase.streetname,
          municipality: userInfoForPurchase.municipality
        }
      });
    }
  }, [isReady]);

  useEffect(() => {
    calcuilatePrice();
  }, [res]);

  const calcuilatePrice = async () => {
    if (res) {
      const resp = await calculatePriceShippingFull({ req: res });
      if (resp) setResp(resp);
      setLoading(false);
    }
  };

  const urlTerms = () => {
    router.push('/terms-conditions')
  }
  return (
    <PaymentLayout>
      <div className="payment-shippment">
        <h4 className="mb-4 text-2xl font-bold">Entrega a domicilio</h4>
        <div className="radioGrup-payment">
          <Alert
            message={
              <Text strong >¡Información importante!</Text>
            }
            description=
            {
              <>
                Una vez generada la guía con la paquetería que elijas, el seguimiento del envío es directamente con ellos, ya que es un servicio externo a nuestro Ecommerce, de acuerdo a  nuestros{' '}
                <a onClick={urlTerms} style={{ cursor: 'pointer', textDecoration: 'underline' }}> Términos y Condiciones.</a>
              </>
            }
            type='info'
            closable
            showIcon
          />

          <Alert
            message={
              <div className="flex items-center space-between text-center gap-6">
                <div>
                  <b>¿Estás pensando en elegir la opción más barata?</b> Existe una opción <b>Standard</b> sin marca de paquetería, que puede estar utilizando paqueterías menos confiables.
                  Si tu pedido es urgente o valioso, te recomendamos usar otro servicio de envío.
                </div>
              </div>
            }
            type='warning'
            closable
            showIcon
          />


          <Alert
            message={
              <div className="flex items-center space-between text-center gap-6">
                <Text strong>¿Necesitas ayuda con tu envío? <Button className="bg-main text-white text-sm md:text-base lg:text-md px-4 rounded-md transition">
                  <a href="https://wa.me/525525534949?text=Hola!%20necesito%20ayuda%20con%20mi%20envío%20" target='_blank' rel="noreferrer">Contactanos!!</a>
                </Button></Text>
              </div>
            }
            type='warning'
            closable
            showIcon
          />


          <Radio.Group
            className="w-full mt-5"
            value={providerSelected}
            onChange={e => {
              setProviderSelected(e.target.value);

              const shipp = response?.find(
                x => x.provider.toLowerCase() + x.serviceCode.toLocaleLowerCase() === e.target.value
              );

              if (!shipp) return;
              setProviderSelected(
                shipp.provider.toLowerCase() + shipp.serviceCode.toLowerCase()
              );
              dispatch(setShippingInfo(shipp));
              dispatch(saveCartOnStorage());
            }}
          >
            {loading ? (
              mockArr(4).map((_, index) => {
                return (
                  <Skeleton.Input
                    active
                    size={'large'}
                    block={true}
                    key={'skeleton-' + index.toString()}
                    className={'p-4'}
                  />
                );
              })) : (
              response && response.length > 0 ? (
                response.map(pr => (

                  <Radio key={pr.provider.toLowerCase() + pr.serviceCode.toLowerCase()}
                    value={pr.provider.toLowerCase() + pr.serviceCode.toLowerCase()}
                    className="radio-shippment">
                    <div className="flex w-full items-center">
                      <img
                        src={pr.image || `/img/shippProviders/${pr.provider && width <= 640
                          ? pr.provider.toLocaleLowerCase() + '_app'
                          : pr.provider.toLocaleLowerCase()
                          }_logo.svg`}
                        className="h-8 w-[50px] md:w-[100px]"
                        onError={({ currentTarget }) => {
                          currentTarget.src = noShip.src;
                        }}
                      />
                      <h1 className='ml-5 font-["Mon-Bold"] text-xs sm:text-base'>
                        {pr.serviceName}
                      </h1>
                      <span className="flex-1 text-right">
                        <PriceLabel
                          coin
                          price={pr.price}
                          className="font-famBold text-xs sm:text-base"
                        />
                      </span>
                    </div>
                  </Radio>

                ))
              ) : (
                <div>
                  <h4 className='ml-5 font-["Mon-Bold"] text-xs sm:text-base text-center '>
                    <span></span>
                  </h4>
                  <Col
                    span={12}
                    xs={{ span: 24 }}
                    md={{ span: 24 }}
                    className={style.ColFlex}
                  >
                    <div className={style.CardCointainer}>
                      <CardInfo title="Sin cobertura de envio a domicilio">
                        <ContactForm />
                      </CardInfo>
                    </div>
                  </Col>
                </div>
              )

            )

            }

          </Radio.Group>
        </div>
        <div className="mt-[4rem] flex place-content-between">
          <Link href="/payment" className="hover:text-enphasis">
            <a className="flex items-center gap-2">
              <LeftOutlined /> Volver a Cuenta
            </a>
          </Link>
          {response && response.length > 0 && (
            <Button
              disabled={!providerSelected}
              onClick={() => router.push('billing')}
              className="button-ctx"
            >
              Continuar
            </Button>
          )}
        </div>
      </div>
    </PaymentLayout>
  );
};
export default ShippmentPage;
