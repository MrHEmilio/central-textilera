import { LeftOutlined, ShopOutlined } from '@ant-design/icons';
import { Breadcrumb, Collapse, Modal } from 'antd';
import Link from 'next/link';
import Image from 'next/image';
import React, { FC, PropsWithChildren, useEffect, useState } from 'react';
import { PaymentFormContact } from '../payment/form/PaymentFormContact';
import { Resume } from '../payment/resume';

import imgLogo from '/public/img/iLogoCT_color.webp';
import { useRouter } from 'next/router';
import Head from 'next/head';
import { getShoppingCart } from '../../services/utils';
import { CtxSelector } from '../../services/redux/store';
import { CartState } from '../../interfaces/State/Cart';
import { useReady } from '../../Hooks/useReady';

const { Panel } = Collapse;

const HeaderPanel: FC = () => {
  return (
    <div className="mt-[4px] flex items-center gap-3">
      <ShopOutlined />
      <span className="text-main">Mostrar el resumen</span>
    </div>
  );
};

export const PaymentLayout: FC<PropsWithChildren<unknown>> = ({ children }) => {
  const { deliveryMethod }: CartState = CtxSelector(s => s.cart!);
  const [politicaEnvio, setPoliticaEnvio] = useState(false);
  const [serviceTerm, setServiceTerm] = useState(false);
  const router = useRouter();
  const isReady = useReady();
  const [route, setRoute] = useState('/payment');
  const [visible, setVisible] = useState(false);
  // if (typeof window !== 'undefined') {
  //   window.addEventListener('beforeunload', e => {
  //     e.preventDefault();
  //     return 'Si recargas esta pagina, tendrás  que volver a iniciar el proceso de pago';
  //   });
  // }
  useEffect(() => {
    if (!isReady) return;
    const cartInfo = getShoppingCart();
    if (Object.getOwnPropertyNames(cartInfo).length === 0) {
      router.replace('/');
      return;
    }
    // setTimeout(() => {
    setRoute(router.asPath);
    setVisible(true);
    // }, 100)
  }, [isReady]);
  useEffect(() => {
    // window.onbeforeunload = e => {
    //   e.preventDefault();
    //   return 'Si recargas esta página, tendrás que volver a inciar el proceso de pago';
    // };
    // window.addEventListener('unload', e => {
    //   console.log('unload');
    // });
  }, []);

  return (
    <>
      <Head> {/* Google Tag Manager */}
        <script
          dangerouslySetInnerHTML={{
            __html: `
            (function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
            new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
            j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
            'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
            })(window,document,'script','dataLayer','GTM-WWRXK5LJ');
            `,
          }}
        />
        {/* End Google Tag Manager */}
        <title>{`Pagos - Central Textilera`}</title>
        <meta name="description" content={'pagos'} />
        <meta name="og:description" content={'pagos'} />
        <meta name="og:title" content={`pagos - Central Textilera`} />
        <meta name="google" content="notranslate" />
        <script defer src="https://sdk.mercadopago.com/js/v2"></script>
      </Head>
      <main>
        <div>
          {visible && (
            <div className="container mx-auto">
              <div className=" grid h-screen grid-cols-1 md:grid-cols-6">
                <div className="container relative pt-16 sm:col-span-3 2xl:col-span-4">
                  <div className="mb-10">
                    <div className="mx-auto max-w-[15rem] cursor-pointer">
                      <Link href="/" className="aspect-video h-[3rem] ">
                        <Image
                          src={imgLogo.src}
                          layout="responsive"
                          height={100}
                          width={300}
                        />
                      </Link>
                    </div>
                    <div>
                      <Breadcrumb
                        className="hidden justify-center sm:flex"
                        separator=">"
                      >
                        <Breadcrumb.Item>
                          <Link href={'/cart'}>Carrito de compra</Link>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item>
                          <Link href={'/payment'}>Cuenta</Link>
                        </Breadcrumb.Item>
                        {(route === '/payment/choose' ||
                          route === '/payment/shippment' ||
                          route === '/payment/billing') &&
                          deliveryMethod === 'SHIPPING' && (
                            <Breadcrumb.Item>
                              <Link href={'/payment/shippment'}>
                                Dirección de envío
                              </Link>
                            </Breadcrumb.Item>
                          )}
                        {(route === '/payment/choose' ||
                          route === '/payment/billing') && (
                          <Breadcrumb.Item>
                            <Link href={'/payment/billing'}>Facturación</Link>
                          </Breadcrumb.Item>
                        )}
                        {route === '/payment/choose' && (
                          <Breadcrumb.Item>
                            <Link href={'/payment/choose'}>Pago</Link>
                          </Breadcrumb.Item>
                        )}
                      </Breadcrumb>
                      {route === '/payment' && (
                        <Link href="/cart" className="hover:text-enphasis ">
                          <a className="flex items-center gap-2 sm:hidden">
                            <LeftOutlined /> Volver al carrito
                          </a>
                        </Link>
                      )}
                      {route === '/payment/shippment' && (
                        <Link href="/payment" className="hover:text-enphasis ">
                          <a className="flex items-center gap-2 sm:hidden">
                            <LeftOutlined /> Volver a Cuenta
                          </a>
                        </Link>
                      )}
                      {route === '/payment/choose' && (
                        <Link
                          href="/payment/shippment"
                          className="hover:text-enphasis "
                        >
                          <a className="flex items-center gap-2 sm:hidden">
                            <LeftOutlined /> Volver a Dirección de envio
                          </a>
                        </Link>
                      )}
                    </div>
                    <div className="mt-8">
                      <PaymentFormContact />
                    </div>
                  </div>

                  <div className="mb-[6rem]">{children}</div>

                  <div
                    className="relative bottom-0 left-0 right-0 mt-12 mr-5 ml-5 flex py-4 md:absolute md:p-4"
                    style={{ borderTop: '1px solid #cccccc' }}
                  >
                    <p
                      className="cursor-pointer text-main"
                      onClick={() => setPoliticaEnvio(true)}
                    >
                      Politica de envio
                    </p>
                    <p
                      onClick={() => setServiceTerm(true)}
                      className="ml-8 cursor-pointer text-main"
                    >
                      Términos del servicio
                    </p>
                  </div>
                </div>
                <div className="order-first md:hidden">
                  <Collapse>
                    <Panel header={<HeaderPanel />} key="resumen">
                      <Resume />
                    </Panel>
                  </Collapse>
                </div>
                <div className="container order-first hidden bg-grayPayment pt-16 sm:col-span-3 md:order-last md:block 2xl:col-span-2">
                  <Resume />
                </div>
              </div>
              <Modal
                title={
                  <h2 className="text-2xl font-bold">Política de envío</h2>
                }
                visible={politicaEnvio}
                // onOk={handleOk}
                footer={null}
                onCancel={() => setPoliticaEnvio(false)}
                width={'50rem'}
              >
                <div>
                  <p className="py-4 text-lg">
                    Los envíos son de 3 a 5 días hábiles.
                  </p>
                </div>
              </Modal>
              <Modal
                title={
                  <h2 className="text-2xl font-bold">Términos del servicio</h2>
                }
                visible={serviceTerm}
                // onOk={handleOk}
                footer={null}
                onCancel={() => setServiceTerm(false)}
                width={'50rem'}
              >
                <div>
                  <p className="py-4 text-lg">
                    No hay reembolso antes de los 30 días hábiles.
                  </p>
                </div>
              </Modal>
            </div>
          )}
        </div>
      </main>
    </>
  );
};
