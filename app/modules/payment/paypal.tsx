import { PayPalButtons, PayPalScriptProvider } from '@paypal/react-paypal-js';
import { Skeleton } from 'antd';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { NewAddress } from '../../interfaces/Request/Client/Address';
import { ShippmentProviderResponse } from '../../interfaces/Response/Shippment';
import { CartState, itemCartFull } from '../../interfaces/State/Cart';
import {
  ClothSimple,
  PaymentMethods,
  SamplerSimple,
  ShippingInterface,
  createOrderNotRegisteredUser,
  createOrderService
} from '../../services';
import { BillingService } from '../../services/Billing/billing-service';
import { getPricesCart } from '../../services/Order';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../services/redux/actions';
import { clearCart } from '../../services/redux/actions/CartActions';
import { TicketActionsSet } from '../../services/redux/actions/TicketActions';
import { CtxDispatch, CtxSelector } from '../../services/redux/store';
import { getShoppingCart } from '../../services/utils';

interface PaypalItems {
  unit_amount: UnitAmount;
  quantity: string;
  name: string;
}

interface UnitAmount {
  value: string;
  currency_code: string;
}

const PaypalPayment: FC = () => {
  const dispatch = CtxDispatch();
  const billingService = new BillingService();
  const notRegisterUserStateV = CtxSelector(x => x.notRegisterUser);

  const [itemsToPay, setItemsToPay] = useState<PaypalItems[]>([]);
  const [totalSellPrice, setTotalSellPrice] = useState<number>();
  // const { location, taxLocation } = CtxSelector(state => state.cart as CartState)
  const [cloths, setCloths] = useState<ClothSimple[]>([]);
  const [samplersSim, setSamplersSim] = useState<SamplerSimple[]>([]);
  const [cartInfo, setCartInfo] = useState<CartState | undefined>();
  const [shipment, setShipment] = useState<
    ShippmentProviderResponse | undefined
  >();
  const [visible, setvisible] = useState(false);

  const router = useRouter();
  const { isReady } = router;

  const getPrices = async (
    cloths: ClothSimple[],
    samplers: SamplerSimple[]
  ) => {
    const res: itemCartFull[] = (await getPricesCart({
      cloths,
      samplers
    })) as any;

    // calculate total
    let total = 0;
    res.forEach(i => {
      if (!i.totalSellPrice) return;
      total += i.totalSellPrice;
    });
    if (total <= 0) return;
    const cart: CartState = getShoppingCart();
    if (!cart) return;
    const map = res
      .map(price => {
        return cart.products
          .map(product => {
            if (price?.product === product.variant) {
              return {
                quantity: product.quantity.toString(),
                name: `${product.nameTela} - color: ${product.nameColor}`,
                unit_amount: {
                  value: price.sellPrice?.toFixed(2).toString(),
                  currency_code: 'MXN'
                }
              } as PaypalItems;
            }
          })
          .filter(el => el !== undefined);
      })
      .filter(el => el !== undefined)
      .flatMap(n => n);

    const map2 = res.map(price => {
      return cart.samplers.map(sam => {
        if (price.product === sam.id) {
          return {
            quantity: sam.quantity?.toString(),
            name: `Muestrario ${sam.clothName}`,
            unit_amount: {
              value: price.sellPrice?.toFixed(2).toString(),
              currency_code: 'MXN'
            }
          };
        }
      });
    });

    const map2Clean = map2.filter(i => i[0] !== undefined)[0];
    if (map2Clean?.length > 0) {
      map2Clean.map(i => {
        map.push(i as PaypalItems);
      });
    }

    if (cart.shippingInfo?.price) {
      map.push({
        quantity: '1',
        name: 'Envío',
        unit_amount: {
          value: cart.shippingInfo.price.toString(),
          currency_code: 'MXN'
        }
      });
      total += cart.shippingInfo.price;
    }

    setItemsToPay(map as PaypalItems[]);
    setTotalSellPrice(total);
    setvisible(true);
  };

  /* useEffect(() => {
    setRenderP(false);
    setTimeout(() => {
      setRenderP(true);
    }, 100);
  }, [notRegisterUserStateV]); */

  useEffect(() => {
    if (!isReady) return;
    // if (!location.id) router.back();
    const cart: CartState = getShoppingCart();
    setCartInfo(cart);
    if (!cart) return;
    // map cloths
    const clothsSimple = cart.products.map(i => ({
      variant: i.variant,
      amount: i.quantity || 0
    }));
    const samplerSimple = cart.samplers.map(i => ({
      sampler: i.id,
      amount: i.quantity || 0
    }));
    setShipment(cart.shippingInfo);
    setCloths(clothsSimple);
    setSamplersSim(samplerSimple);
    getPrices(clothsSimple, samplerSimple);
  }, [isReady]);

  const clientId = process.env.NEXT_PUBLIC_PP_CLIENT_ID;

  return (
    <div className="grid min-h-[10rem] w-full place-content-center">
      {visible ? (
        <div className="w-[18rem] md:w-[23rem] lg:w-[30rem]">
          {clientId && totalSellPrice && itemsToPay.length > 0 && (
            <PayPalScriptProvider
              options={{
                'client-id': clientId,
                currency: 'MXN'
              }}
            >
              <PayPalButtons
                style={{ layout: 'horizontal' }}
                createOrder={(_, actions) => {
                  dispatch(LoaderActionsShow());
                  return actions.order.create({
                    purchase_units: [
                      {
                        description: 'Compra en Central Textilera',
                        items: itemsToPay as any,
                        amount: {
                          value: totalSellPrice.toString(),
                          breakdown: {
                            item_total: {
                              value: totalSellPrice.toString(),
                              currency_code: 'MXN'
                            }
                          }
                        }
                      }
                    ]
                  });
                }}
                onError={() => {
                  dispatch(LoaderActionsHide());
                  toast.error(
                    'Lo sentimos, parece que hubo un error al procesar tu pago, intenta nuevamente',
                    { theme: 'colored' }
                  );
                }}
                onCancel={() => dispatch(LoaderActionsHide())}
                onApprove={async (data, actions) => {
                  dispatch(LoaderActionsShow());
                  if (!cartInfo) return;
                  dispatch(LoaderActionsShow());
                  actions.order?.capture().then(() => {
                    dispatch(LoaderActionsShow());
                    if (notRegisterUserStateV.userContact) {
                      const { userContact, userAddress, taxAddress } =
                        notRegisterUserStateV;

                      createOrderNotRegisteredUser({
                        paymentMethod: PaymentMethods.paypal,
                        cloths: cloths,
                        samplers: samplersSim,
                        deliveryMethod: cartInfo.deliveryMethod,
                        paymentId: data.orderID,
                        client: {
                          name: userContact.name,
                          countryCode: userContact.countryCode,
                          email: userContact.email,
                          firstLastname: userContact.lastName,
                          secondLastname: userContact.secondLastName || '',
                          phone: userContact.phone,
                          rfc: notRegisterUserStateV.billingInfo?.rfc || '',
                          companyName:
                            notRegisterUserStateV.billingInfo?.companyName ||
                            '',
                          fiscalRegimen:
                            notRegisterUserStateV.fiscalRegimen || ''
                        },
                        billingAddress: (taxAddress
                          ? taxAddress
                          : userAddress) as NewAddress,
                        shipping:
                          cartInfo.deliveryMethod == 'PICK_UP'
                            ? null
                            : {
                              shippingMethod: cartInfo.shippingInfo?.shippingMethod || '',
                              rateId: cartInfo.shippingInfo?.rateId || '',
                              price: cartInfo.shippingInfo?.price || '',
                              date: cartInfo.shippingInfo?.date || new Date(),
                              provider: cartInfo.shippingInfo?.provider || '',
                              serviceCode:
                                cartInfo.shippingInfo?.serviceCode || '',
                              serviceName:
                                cartInfo.shippingInfo?.serviceName || '',
                              clientAddress: userAddress as NewAddress
                            },
                        addressSame: !taxAddress,
                        orderBilling: cartInfo.requiresTax || false
                      })
                        .then(r => {
                          if (r) {
                            dispatch(TicketActionsSet(r.data));
                            billingService.sendTaxRequirement(r.data.id);
                          }
                          const res = r as { message: string };
                          dispatch(clearCart());
                          toast.success(res.message);
                          router.replace('/payment/ticket');
                        })
                        .catch((e: { message: string }) => {
                          dispatch(LoaderActionsHide());
                          toast.error(e.message, { theme: 'colored' });
                        });
                      return;
                    }
                    // purchase user
                    createOrderService({
                      paymentMethod: PaymentMethods.paypal,
                      billingAddress: cartInfo.taxLocation?.id,
                      cloths: cloths,
                      samplers: samplersSim,
                      deliveryMethod: cartInfo.deliveryMethod,
                      paymentId: data.orderID,
                      ...(cartInfo.deliveryMethod === 'SHIPPING' &&
                        cartInfo.location && {
                        shipping: {
                          ...shipment,
                          clientAddress: cartInfo.location.id
                        } as ShippingInterface
                      })
                    })
                      .then(r => {
                        if (r) {
                          dispatch(TicketActionsSet(r.data));
                          billingService.sendTaxRequirement(r.data.id);
                        }
                        const res = r as { message: string };
                        dispatch(clearCart());
                        toast.success(res.message);
                        router.replace('/payment/ticket');
                      })
                      .catch((e: { message: string }) => {
                        dispatch(LoaderActionsHide());
                        toast.error(e.message, { theme: 'colored' });
                      });
                  });
                }}
              ></PayPalButtons>
            </PayPalScriptProvider>
          )}
        </div>
      ) : (
        <Skeleton.Button />
      )}
    </div>
  );
};
export default PaypalPayment;
