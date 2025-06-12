import { Col, Row } from 'antd';
import { NextPage } from 'next';
import { ProtectAdmin } from '../../../app/modules/Admin';
import { CreateClothActionsSection } from '../../../app/modules/Admin/Cloth/CreateClothActionsSection';
import { FormCloth } from '../../../app/modules/Admin/FormCloth';
import { AdminLayout } from '../../../app/modules/shared/AdminLayout';
import { CtxSelector } from '../../../app/services/redux/store';

const CreateClothPage: NextPage = () => {
  const clothToEdit = CtxSelector(s => s.clothForm);
  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex flex-row items-center justify-between px-4">
            <div>
              <h1 className="color-main pb-5 pt-4 text-3xl">
                {clothToEdit?.id ? 'Editar' : 'Crear'} Tela
              </h1>
            </div>
            <div>
              <CreateClothActionsSection />
            </div>
          </div>
          <div>
            <Row className="continer-login pt-4">
              <Col xs={{ span: 24 }} lg={{ span: 1 }}></Col>
              <Col
                xs={{ span: 24 }}
                lg={{ span: 22 }}
                className="container-col"
              >
                <FormCloth />
              </Col>
              <Col xs={{ span: 24 }} lg={{}}></Col>
            </Row>
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default CreateClothPage;
