import { Button, Modal } from 'antd';
import React, { useState } from 'react';
import { useAdminTable } from '../../app/models/TableAdmin/Usos';
import { ModalAddUse } from '../../app/modules/Admin/ModalAddUse';
import { ProtectAdmin } from '../../app/modules/Admin/ProtectAdmin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { DataUse } from '../../app/interfaces/Response/Admin/Use';
import { CtxDispatch } from '../../app/services/redux/store';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';

const use = () => {
  const dispatch = CtxDispatch();
  const [open, setOpen] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  
  const [use, setUse] = useState<DataUse>({
    name: 'nombre',
    active: true,
    id: 'novalido'
  });

  const showModal = () => {
    setOpen(true);
  };

  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
  };

  const editUse = (use: DataUse) => {
    setModalEdit(true);
    setUse(use);
  };

  const onSubmit = () => {
    setModalEdit(false);
    setOpen(false);
    dispatch(rechargeTableAction(true));
  };

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Usos</h1>
            <div>
              <Button
                type="primary"
                className="button-ctx w-full"
                onClick={showModal}
              >
                Agregar Uso
              </Button>
            </div>
          </div>
          <div className="">
            <TableAdmin
              colums={useAdminTable}
              url={'use'}
              onCickRow={editUse}
            />
          </div>
        </div>
        <Modal
          title="Agregar"
          visible={open}
          onCancel={handleCancel}
          footer={null}
        >
          <ModalAddUse onSubmit={onSubmit} />
        </Modal>
        <Modal
          title="Editar"
          visible={modalEdit}
          onCancel={handleCancel}
          footer={null}
        >
          <ModalAddUse onSubmit={onSubmit} use={use} />
        </Modal>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default use;
