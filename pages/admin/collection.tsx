import { Button, Modal } from 'antd';
import React, { useState } from 'react';
import { collectionsAdminTable } from '../../app/models/TableAdmin/Colecciones';
import { ProtectAdmin } from '../../app/modules/Admin/ProtectAdmin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { ModalAddColection } from '../../app/modules/Admin/ModalAddColection';
import { DataCollection } from '../../app/interfaces/Response/Admin/Collection';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';
import { CtxDispatch } from '../../app/services/redux/store';

const collection = () => {
  const dispatch = CtxDispatch();
  const [modalEdit, setModalEdit] = useState(false);
  const [open, setOpen] = useState(false);
  
  const [collection, setCollection] = useState<DataCollection>({
    active: true,
    id: 'prueba',
    image: 'image',
    name: 'name'
  });

  const showModal = () => {
    setOpen(true);
  };

  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
  };

  const editUse = (collection: DataCollection) => {
    setModalEdit(true);
    setCollection(collection);
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
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Usos</h1>
            <div>
              <Button
                type="primary"
                className="button-ctx w-full"
                onClick={showModal}
              >
                Agregar usos
              </Button>
            </div>
          </div>
          <div className="">
            <TableAdmin
              colums={collectionsAdminTable}
              url={'collection'}
              onCickRow={editUse}
            />
          </div>
          {open && (
            <Modal
              title="Agregar"
              visible={open}
              onCancel={handleCancel}
              footer={null}
            >
              <ModalAddColection onSubmit={onSubmit} />
            </Modal>
          )}
          {modalEdit && (
            <Modal
              title="Editar"
              visible={modalEdit}
              onCancel={handleCancel}
              footer={null}
            >
              <ModalAddColection onSubmit={onSubmit} modal={collection} />
            </Modal>
          )}
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default collection;
