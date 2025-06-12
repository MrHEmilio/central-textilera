import { Col, Row } from 'antd';
import { useRouter } from 'next/router';
import React from 'react';
import { toast } from 'react-toastify';
import { CreateRequestAdmin } from '../../../app/interfaces/Request/Admin/AdminResponseAdmin';
import { ProtectAdmin } from '../../../app/modules/Admin';
import { FormAdmin } from '../../../app/modules/Admin/FormAdmin';
import { AdminLayout } from '../../../app/modules/shared/AdminLayout';
import { createAdmin } from '../../../app/services/admin/adminServices';

const index = () => {
  const navigate = useRouter();
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const onCreateAdmin = async (value: CreateRequestAdmin, setLoading: any) => {
    const response = await createAdmin(value);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      navigate.replace('/admin/admin');
    }
    setLoading(false);
  };

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">
              Crear Administrador
            </h1>
          </div>
          <div>
            <Row className="continer-login">
              <Col xs={{ span: 24 }} lg={{ span: 4, offset: 2 }}></Col>
              <Col
                xs={{ span: 24 }}
                lg={{ span: 12, offset: 1 }}
                className="container-col"
              >
                <FormAdmin onSubmit={onCreateAdmin} />
              </Col>
              <Col xs={{ span: 24 }} lg={{ span: 4, offset: 1 }}></Col>
            </Row>
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default index;
