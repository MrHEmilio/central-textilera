import { Col, Row } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { CardInfo, MainLayout } from '../../app/modules/shared';
import { LinkCtx } from '../../app/modules/shared/Link';
import { newAddressClient } from '../../app/services/Client/address/addressService';
import { useRouter } from 'next/router';
import { toast } from 'react-toastify';
import { NewAddress } from '../../app/interfaces/Request/Client/Address';
import { ProtectedRoutesDirections } from '../../app/modules/shared/ProtectedRoutesDirections';
import { CtxSelector } from '../../app/services/redux/store';
import PaymentFormAddressGoogle from '../../app/modules/shared/PaymentFormAddress/PaymentFormAddressGoogle';
// import PaymentFormAdress from '../../app/modules/shared/PaymentFormAddress/PaymentFormAddress';

const addDirection = () => {
  
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const { auth } = userInfo;
  const navigate = useRouter();

  const addDirections = async (value: NewAddress) => {
    const response = await newAddressClient(value);
    if (response) {
      navigate.back();
      toast.success(`${response.message}`, { theme: 'colored' });
    }
  };

  return (
    <MainLayout
      title={'Mis direcciones'}
      pageDescription={'Direcciones guardadas'}
    >
      <div className="color-main container mx-auto">
        <div>
          <Row>
            <Col className="container mx-auto sm:w-full lg:max-w-4xl">
              <div className="flex pt-20 pl-3">
                <LinkCtx href={'/directions'}>
                  <ArrowLeftOutlined className="mr-4 cursor-pointer text-lg" />
                </LinkCtx>
                <h1 className="mb-4  pb-2 text-2xl">Agregar dirección</h1>
              </div>
              <ProtectedRoutesDirections auth={auth}>
                <CardInfo>
                  <div className="container-formAdress">
                    {/* <PaymentFormAdress
                        enterprise
                        addDirection
                        onSubmit={addDirections}
                      /> */}
                    {
                      <PaymentFormAddressGoogle
                        enterprise
                        onSubmit={addDirections}
                      />
                    }
                  </div>
                </CardInfo>
              </ProtectedRoutesDirections>
            </Col>
          </Row>
        </div>
      </div>
    </MainLayout>
  );
};

export default addDirection;
