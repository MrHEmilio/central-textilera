import { Col, Row } from 'antd';
import { MainLayout } from '../../app/modules/shared/MainLayout/MainLayout';
import { FormRegister } from '../../app/modules/auths/FormRegister/FormRegister';
import { RefObject, useState } from 'react';
import { CreateUser } from '../../app/interfaces/Request/Client/CreateUser';
import { createClient } from '../../app/services/Client/CreateClient';
import { authRedirect, authService } from '../../app/services/auth/authService';
import { SessionActions } from '../../app/services/redux/actions/SessionActions';
import { CtxDispatch, CtxSelector } from '../../app/services/redux/store';
import { ProtectedRoutesAuth } from '../../app/modules/shared/ProtectedRoutesAuth';
import ReCAPTCHA from 'react-google-recaptcha';

const register = () => {

  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);

  const dispatch = CtxDispatch();

  const [loading, setLoading] = useState<boolean>(false);

  const onRegister = async (
    data: CreateUser,
    captcha: RefObject<ReCAPTCHA>
  ) => {
    setLoading(true);
    const response = await createClient(data);
    setLoading(false);
    if (!response) {
      captcha.current?.reset();
      return;
    }
    const responseAut = await authService({
      username: data.email,
      password: data.password
    });
    if (responseAut) {
      const res = await authRedirect(responseAut.redirect);
      if (res) {
        dispatch(SessionActions(res));
        const infoObj = { ...res.info, role: res.role };
        localStorage.setItem('info', JSON.stringify(infoObj));

        setLoading(false);
      }
    }
    setLoading(false);
  };
  return (
    <MainLayout title={'Crear cuenta'} pageDescription={'Registrate'}>
      <div className="color-main container mx-auto ">
        <h1 className="color-main pb-5 pt-4 text-4xl">Crear cuenta</h1>
        <div>
          <Row className="continer-login">
            <Col xs={{ span: 24 }} lg={{ span: 6, offset: 1 }}></Col>
            <Col
              xs={{ span: 24 }}
              lg={{ span: 9, offset: 1 }}
              className="container-col"
            >
              <ProtectedRoutesAuth auth={userInfo}>
                <FormRegister onRegister={onRegister} loading={loading} />
              </ProtectedRoutesAuth>
            </Col>
            <Col xs={{ span: 24 }} lg={{ span: 6, offset: 1 }}></Col>
          </Row>
        </div>
      </div>
    </MainLayout>
  );
};

export default register;
