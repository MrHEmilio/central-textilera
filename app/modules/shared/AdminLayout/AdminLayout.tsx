import Head from 'next/head';
import React, { FC } from 'react';
// import { AdminNavbar } from '../../AdminLayout/AdminNavbar';
import { AdminSideMenu } from '../../AdminLayout/AdminSideMenu';
import { Navbar } from '../Navbar';

interface Props {
  children?: React.ReactNode | undefined;
  title: string;
  pageDescription: string;
  imageFullUrl?: string;
}

export const AdminLayout: FC<Props> = ({
  children,
  title,
  pageDescription,
  imageFullUrl,
}) => {
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

        <title>{`${title} - Central Textilera`}</title>
        <meta name="description" content={pageDescription} />
        <meta name="og:description" content={pageDescription} />
        <meta name="og:title" content={`${title} - Central Textilera`} />

        {imageFullUrl && <meta name="og:img" content={imageFullUrl} />}
      </Head>
      <div style={{ backgroundColor: '#F6F6F7' }}>
        {/* <AdminNavbar /> */}
        <Navbar />
        <main className="main-adminLayout flex">
          <AdminSideMenu />
          <div className="children-adminLayout mt-6 min-h-screen w-full">
            {children}
          </div>
        </main>
      </div>
    </>
  );
};
