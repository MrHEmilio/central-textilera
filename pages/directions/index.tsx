import { Col, Row } from 'antd';
import Link from 'next/link';
import React, { useEffect, useState } from 'react';
import { DirectionCard } from '../../app/modules/Directions';
import { MainLayout } from '../../app/modules/shared';
import { getAllClientAddress } from '../../app/services/Client/address/addressService';

import { ProtectedRoutesDirections } from '../../app/modules/shared/ProtectedRoutesDirections';
import { CtxDispatch, CtxSelector } from '../../app/services/redux/store';
import { EmptyDirections } from '../../app/modules/Directions/EmptyDirections';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../app/services/redux/actions';
import { NewAddress } from '../../app/interfaces/Request/Client/Address';

const index = () => {
  const dispatch = CtxDispatch();

  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const { auth } = userInfo;
  const [directions, setDirections] = useState<NewAddress[]>([]);

  const callAdress = async () => {
    dispatch(LoaderActionsShow());
    const response = await getAllClientAddress();

    if (response) {
      setDirections(response.content);
    }
    if (!response) {
      setDirections([]);
    }
    setTimeout(() => {
      dispatch(LoaderActionsHide());
    }, 600);
  };

  useEffect(() => {
    if (auth) {
      callAdress();
    }
  }, []);

  return (
    <MainLayout
      title={'Mis direcciones'}
      pageDescription={'Direcciones guardadas'}
    >
      <div className="color-main container mx-auto" style={{}}>
        <h1 className="color-main pb-2 pt-4 text-4xl">Direcciones</h1>
        <ProtectedRoutesDirections auth={auth}>
          {
            <div>
              <Row>
                {directions.length > 0 ? (
                  <Col
                    xs={{ span: 24, offset: 0 }}
                    lg={{ span: 12, offset: 6 }}
                  >
                    <div className="container-buttonaddnewdirection mb-8">
                      <Link href="/directions/addDirection">
                        <a className="button-link-ctx buttonaddnewdirection mt-0 w-40">
                          Agregar
                        </a>
                      </Link>
                    </div>
                    <div
                      style={{ boxShadow: ' 0px 4px 3px #00000029' }}
                      className="mb-16"
                    >
                      {directions.map(direction => (
                        <DirectionCard
                          callAdress={callAdress}
                          key={direction.id}
                          direction={direction}
                        />
                      ))}
                    </div>
                  </Col>
                ) : (
                  <Col
                    xs={{ span: 24, offset: 0 }}
                    lg={{ span: 24, offset: 0 }}
                  >
                    <EmptyDirections />
                  </Col>
                )}
              </Row>
            </div>
          }
        </ProtectedRoutesDirections>
      </div>
    </MainLayout>
  );
};

export default index;
