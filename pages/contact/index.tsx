import { Breadcrumb, Col, Row } from 'antd';
import Link from 'next/link';
import { InfoContact } from '../../app/modules/Contact/InfoContact';
import {
  CardInfo,
  ContactForm,
  Location,
  MainLayout
} from '../../app/modules/shared';
import style from '../../styles/Contact.module.css';

const Contac = () => {
  return (
    <MainLayout title={'Contacto'} pageDescription={'Contactanos!!'}>
      <div className="color-main container mx-auto" style={{}}>
        <div className="mt-8">
          <Breadcrumb>
            <Breadcrumb.Item>
              <Link href="/">Inicio</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>Contacto</Breadcrumb.Item>
          </Breadcrumb>
        </div>
        <h1 className="color-main pb-2 pt-4 text-4xl">Contacto</h1>
        <div>
          <Row>
            <Col
              span={12}
              className={style.ColFlex}
              xs={{ span: 24 }}
              md={{ span: 12 }}
            >
              <div className={style.CardCointainer}>
                <Row align="stretch">
                  <Col span={24}>
                    <CardInfo title="INFORMACIÓN">
                      <Row>
                        <InfoContact />
                      </Row>
                      <Row className="min-h-[14rem] pt-6">
                        <Col span={24}>
                          <Location height={17} />
                        </Col>
                      </Row>
                    </CardInfo>
                  </Col>
                </Row>
              </div>
            </Col>
            <Col
              span={12}
              xs={{ span: 24 }}
              md={{ span: 12 }}
              className={style.ColFlex}
            >
              <div className={style.CardCointainer}>
                <CardInfo title="CONTÁCTANOS">
                  <ContactForm />
                </CardInfo>
              </div>
            </Col>
          </Row>
        </div>
      </div>
    </MainLayout>
  );
};

export default Contac;
