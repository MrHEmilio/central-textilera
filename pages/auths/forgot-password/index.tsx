import { Col, Row } from 'antd';
import { FormForgotPassword } from '../../../app/modules/auths';
import { MainLayout } from '../../../app/modules/shared';
import { ProtectedRoutesAuth } from '../../../app/modules/shared/ProtectedRoutesAuth';
import { CtxSelector } from '../../../app/services/redux/store';

const ForgotPassword = () => {
  
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);

  return (
    <MainLayout title={'Cuenta'} pageDescription={'Restablece tu contraseña'}>
      <div className="color-main container mx-auto ">
        <h1 className="color-main pb-5 pt-4 text-4xl">
          Restrablecer contraseña
        </h1>
        <div>
          <Row className="continer-login">
            <Col xs={{ span: 24, offset: 1 }} lg={{ span: 6, offset: 1 }}></Col>
            <Col
              xs={{ span: 24 }}
              lg={{ span: 9, offset: 1 }}
              className="container-col"
            >
              <ProtectedRoutesAuth auth={userInfo}>
                <FormForgotPassword />
              </ProtectedRoutesAuth>
            </Col>
            <Col xs={{ span: 5, offset: 1 }} lg={{ span: 6, offset: 1 }}></Col>
          </Row>
        </div>
      </div>
    </MainLayout>
  );
};

export default ForgotPassword;
