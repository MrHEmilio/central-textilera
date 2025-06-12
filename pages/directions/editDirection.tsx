import { ArrowLeftOutlined } from '@ant-design/icons';
import { Col, Row } from 'antd';
import { useRouter } from 'next/router';
import React from 'react';
import { NewAddress } from '../../app/interfaces/Request/Client/Address';
import { CardInfo, MainLayout } from '../../app/modules/shared';
import { LinkCtx } from '../../app/modules/shared/Link';
import { CtxSelector } from '../../app/services/redux/store';
import { editAddressService } from '../../app/services/Client/address/addressService';
import { toast } from 'react-toastify';
import { ProtectedRoutesDirections } from '../../app/modules/shared/ProtectedRoutesDirections';
import PaymentFormAddressGoogle from '../../app/modules/shared/PaymentFormAddress/PaymentFormAddressGoogle';

const editDirection = () => {
  const navigate = useRouter();

  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const directionToEdit = CtxSelector(
      /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
    (state: { session: any; editDirection: any }) => state.editDirection
  );

  const { auth } = userInfo;
  const onEditDirections = async (value: NewAddress) => {
    if (directionToEdit) {
      value.id = directionToEdit.id;
    }
    const response = await editAddressService(value);
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
      <div className="color-main container mx-auto" style={{}}>
        <div>
          <Row>
            <Col xs={{ span: 24, offset: 0 }} lg={{ span: 12, offset: 6 }}>
              <div className="flex pt-20 pl-3">
                <LinkCtx href={'/directions'}>
                  <ArrowLeftOutlined className="mr-4 cursor-pointer text-lg" />
                </LinkCtx>
                <h1 className="mb-4  pb-2 text-2xl">Editar dirección</h1>
              </div>
              <ProtectedRoutesDirections auth={auth}>
                <CardInfo>
                  <div className="container-formAdress">
                    <PaymentFormAddressGoogle
                      onSubmit={onEditDirections}
                      enterprise
                      directionEdit={directionToEdit}
                    />
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

export default editDirection;
