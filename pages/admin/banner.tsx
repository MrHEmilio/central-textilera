import { Alert, Button, Modal } from 'antd';
import { ProtectAdmin } from '../../app/modules/Admin/ProtectAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout/AdminLayout';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { bannerAdminTable } from '../../app/models/TableAdmin/Banners';
import { useState } from 'react';
import { ModalAddBanner } from '../../app/modules/Admin/ModalAddBanner';
import { DataBanner } from '../../app/interfaces/Response/Admin/Banner';
import { CtxDispatch } from '../../app/services/redux/store';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';

const banner = () => {
  const dispatch = CtxDispatch();
  const [open, setOpen] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  const [modal, setModal] = useState<DataBanner>({
    active: true,
    id: 'novalido',
    description: 'Hola',
    image: 'imagen',
    waitTime: 1
  });

  const showModal = () => {
    setOpen(true);
  };
  
  const editModal = (modal: DataBanner) => {
    setModalEdit(true);
    setModal(modal);
  };

  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
  };

  const onSubmit = () => {
    setOpen(false);
    setModalEdit(false);
    dispatch(rechargeTableAction(true));
  };

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="grid grid-rows-2">
            <div className="flex items-center justify-between px-4">
              <h1 className="color-main pb-5 pt-4 text-3xl">Banners</h1>
              <div>
                <Button
                  type="primary"
                  className="button-ctx w-full"
                  onClick={showModal}
                >
                  Agregar Banner
                </Button>
              </div>
            </div>
            <Alert
              className="mx-4 rounded-md"
              message={
                <p className="text-yellow-500">
                  Todos los cambios efectuados en este módulo se verán
                  reflejados <strong>24 horas después</strong>
                </p>
              }
              type="warning"
              showIcon
            />
          </div>
          <div className="">
            <TableAdmin
              orderBy={false}
              colums={bannerAdminTable}
              url={'banner'}
              onCickRow={editModal}
              search={false}
            />
          </div>
          {open && (
            <Modal
              title="Agregar"
              visible={open}
              onCancel={handleCancel}
              footer={null}
            >
              <ModalAddBanner onSubmit={onSubmit} />
            </Modal>
          )}
          {modalEdit && (
            <Modal
              title="Editar"
              visible={modalEdit}
              onCancel={handleCancel}
              footer={null}
            >
              <ModalAddBanner onSubmit={onSubmit} modal={modal} />
            </Modal>
          )}
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default banner;
