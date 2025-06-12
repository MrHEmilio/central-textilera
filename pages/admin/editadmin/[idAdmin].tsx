import { Col, Modal, Row, Spin } from 'antd';
import { useRouter } from 'next/router';
import React, { useEffect, useState } from 'react';
import { ProtectAdmin } from '../../../app/modules/Admin';
import { FormAdmin } from '../../../app/modules/Admin/FormAdmin';
import { AdminLayout } from '../../../app/modules/shared/AdminLayout';
import { InfoAdmin } from '../../../app/interfaces/Response/Admin/AdminResposeAdmin';
import {
  getIdAdmin,
  editAdmin,
  deleteAdmin
} from '../../../app/services/admin/adminServices';
import { CreateRequestAdmin } from '../../../app/interfaces/Request/Admin/AdminResponseAdmin';
import { toast } from 'react-toastify';
import { ExclamationCircleOutlined } from '@ant-design/icons';
const { confirm } = Modal;

const AdminId = () => {
  const [infoUser, setInfoUser] = useState<InfoAdmin | null>();
  const [loadingC, setLoadingC] = useState(false);
  const router = useRouter();
  const { query, isReady } = router;

  /*eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const onEditAdmin = async (value: CreateRequestAdmin, setLoading: any) => {
    setLoadingC(true);
    delete value.email;
    value.id = infoUser?.id;
    const response = await editAdmin(value);
    setLoadingC(false);
    setLoading(false);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      getAdminInfo(infoUser?.id || '');
    }else{
      toast.error(`NO se pudo editar el registro`, {
        theme: 'colored'
      });
    }
  };

  const getAdminInfo = async (idAdmin: string) => {
    const response = await getIdAdmin(idAdmin);
    if (response) {
      setInfoUser(response.info);
    }
  };

  const onDelete = async () => {
    confirm({
      icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
      content: <h1>¿Desea eliminar esta Administrador?</h1>,
      async onOk() {
        setLoadingC(true);
        const response = await deleteAdmin(infoUser?.id || '');
        if (response) {
          toast.success(`${response.message}`, {
            theme: 'colored'
          });
          getAdminInfo(infoUser?.id || '');
        }
        setLoadingC(false);
      },
      onCancel() {
        setLoadingC(false);
      },
      cancelText: 'Cancelar'
    });
  };

  useEffect(() => {
    if (!query || !isReady) return;
    const { idAdmin } = query as { idAdmin: string };
    getAdminInfo(idAdmin);
  }, [query, isReady]);

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">
              Editar Administrador
            </h1>
          </div>
          <div>
            <div
              className={` absolute bottom-0 right-0 top-0 left-0 z-[9999]
              ${loadingC ? 'grid' : 'hidden'}
                place-content-center
                backdrop-blur-sm
              `}
            >
              <Spin size={'large'} />
            </div>
            <Row className="continer-login">
              <Col xs={{ span: 24 }} lg={{ span: 4, offset: 2 }}></Col>
              <Col
                xs={{ span: 24 }}
                lg={{ span: 12, offset: 1 }}
                className="container-col"
              >
                <FormAdmin
                  onSubmit={onEditAdmin}
                  admin={infoUser}
                  onDelete={onDelete}
                />
              </Col>
              <Col xs={{ span: 24 }} lg={{ span: 4, offset: 1 }}></Col>
            </Row>
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default AdminId;
