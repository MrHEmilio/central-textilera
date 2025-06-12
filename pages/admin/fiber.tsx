import { Button, Modal } from 'antd';
import React, { useState } from 'react';
import { fiberAdminTable } from '../../app/models/TableAdmin/Fiber';
import { ProtectAdmin } from '../../app/modules/Admin';
import { ModalAddFiber } from '../../app/modules/Admin/ModalAddFiber';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { DataFiber } from '../../app/interfaces/Response/Admin/Fiber';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';
import { CtxDispatch } from '../../app/services/redux/store';

const fiber = () => {
  const dispatch = CtxDispatch();
  const [open, setOpen] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  
  const [fiber, setFiber] = useState<DataFiber>({
    name: 'nombre',
    active: true,
    id: 'novalido'
  });

  const showModal = () => {
    setOpen(true);
  };

  const editUse = (fiber: DataFiber) => {
    setModalEdit(true);
    setFiber(fiber);
  };

  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
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
            <h1 className="color-main pb-5 pt-4 text-3xl">Fibras</h1>
            <div>
              <Button
                type="primary"
                className="button-ctx w-full"
                onClick={showModal}
              >
                Agregar Fibra
              </Button>
            </div>
          </div>
          <div className="">
            <TableAdmin
              colums={fiberAdminTable}
              url={'fiber'}
              onCickRow={editUse}
            />
          </div>
        </div>
        <Modal
          title="Agregar"
          visible={open}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose={true}
        >
          <ModalAddFiber onSubmit={onSubmit} />
        </Modal>
        <Modal
          title="Editar"
          visible={modalEdit}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose={true}
        >
          <ModalAddFiber onSubmit={onSubmit} modal={fiber} />
        </Modal>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default fiber;
