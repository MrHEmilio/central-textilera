import Head from 'next/head';
import { useRouter } from 'next/router';
import { FC, useEffect } from 'react';
import { useReady } from '../../../Hooks/useReady';
import { KEYWORDS_SEO } from '../../../models/constants';
import { ClearNotRegisterUserState } from '../../../services/redux/actions/NotRegisterUserActions';
import { CtxDispatch } from '../../../services/redux/store';
import { Footer } from '../Footer/Footer';
import { Navbar } from '../Navbar';
import React from 'react';
import Script from 'next/script';
import { Totop } from '../ToTop';
interface Props {
  children?: React.ReactNode | undefined;
  title: string;
  pageDescription: string;
  imageFullUrl?: string;
}

export const MainLayout: FC<Props> = ({
  children,
  title,
  pageDescription,
  imageFullUrl
}) => {
  const dispatch = CtxDispatch();
  const router = useRouter();
  const isready = useReady();
  useEffect(() => {
    if (!isready) return;

    if (
      router.pathname.includes('mercadopago/success') ||
      router.pathname.includes('ticket')
    ) {
      return;
    }

    dispatch(ClearNotRegisterUserState());
  }, [isready]);
  return (
    <>
      <Head>
        {/* Google Tag Manager */}
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

        {/*< Google Analytics>*/}
        <script async src="https://www.googletagmanager.com/gtag/js?id=G-BXTZZR1RZJ" />
        <script
          dangerouslySetInnerHTML={{
            __html: `
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
            gtag('js', new Date());
            gtag('config', 'G-BXTZZR1RZJ');
            `,
          }}
        />
        <title>{`${title} - Central Textilera`}</title>
        <meta name="description" content={pageDescription} />
        <meta name="og:description" content={pageDescription} />
        <meta name="og:title" content={`${title} - Central Textilera`} />
        <meta name="title" content={`${title} - Central Textilera`} />
        <meta name="keywords" content={`${KEYWORDS_SEO.join(',')},${title}`} />
        <meta property="image" content="/img/logo-ctx.jpg" />
        <meta
          property="og:image"
          content={imageFullUrl || '/img/logo-ctx.jpg'}
        />
      </Head>

      <main className="relative min-h-screen flex-col">
        {/*<Totop />*/}
        <Navbar />
        <div className="container-layout-children flex flex-1 flex-col overflow-x-hidden">
          {children}
        </div>
        <Footer />
      </main>
    </>
  );
};
