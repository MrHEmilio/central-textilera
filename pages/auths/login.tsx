// import { useEffect, ReactNode } from 'react';
import { Col, Row } from 'antd';
import { MainLayout } from '../../app/modules/shared';
import { FormLogin } from '../../app/modules/auths';
import { NextPage } from 'next';
import { ProtectedRoutesAuth } from '../../app/modules/shared/ProtectedRoutesAuth';
import { CtxSelector } from '../../app/services/redux/store';

const login: NextPage = () => {
  
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);

  return (
    <MainLayout title={'Cuenta'} pageDescription={'Inicia sesion ya!!'}>
      <div className="color-main container mx-auto ">
        <h1 className="color-main pb-5 pt-4 text-4xl">Iniciar sesión</h1>
        <div>
          <Row className="continer-login">
            <Col xs={{ span: 24, offset: 1 }} lg={{ span: 6, offset: 1 }}></Col>
            <Col
              xs={{ span: 24 }}
              lg={{ span: 9, offset: 1 }}
              className="container-col "
            >
              <ProtectedRoutesAuth auth={userInfo}>
                <FormLogin />
              </ProtectedRoutesAuth>
            </Col>
            <Col xs={{ span: 5, offset: 1 }} lg={{ span: 6, offset: 1 }}></Col>
          </Row>
        </div>
      </div>
    </MainLayout>
  );
};

// You should use getServerSideProps when:
// - Only if you need to pre-render a page whose data must be fetched at request time

export default login;
