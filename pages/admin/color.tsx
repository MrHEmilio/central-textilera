import { Button, Modal } from 'antd';
// import { useRouter } from 'next/router';
import React, { useState } from 'react';
import { ProtectAdmin } from '../../app/modules/Admin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { colorsAdminTable } from '../../app/models/TableAdmin/Colors';
import { ModalAddColor } from '../../app/modules/Admin/ModalAddColor';
import { CtxDispatch } from '../../app/services/redux/store';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';
import { DataColors } from '../../app/interfaces/Response/Admin/Colors';

const color = () => {
  const dispatch = CtxDispatch();
  const [open, setOpen] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  
  const [color, setColor] = useState<DataColors>({
    active: true,
    id: 'prueba',
    name: 'name',
    code: ''
  });

  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
  };

  const showModal = () => {
    setOpen(true);
  };

  const onSubmit = () => {
    setOpen(false);
    setModalEdit(false);
    dispatch(rechargeTableAction(true));
  };

  const editColorRow = (color: DataColors) => {
    setModalEdit(true);
    setColor(color);
  };

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Colores</h1>
            <div>
              <Button
                type="primary"
                className="button-ctx w-full"
                onClick={showModal}
              >
                Agregar Color
              </Button>
            </div>
          </div>
          <div className="">
            <TableAdmin
              colums={colorsAdminTable}
              url={'color'}
              onCickRow={editColorRow}
            />
          </div>
        </div>
        {open && (
          <Modal
            title="Agregar"
            visible={open}
            onCancel={handleCancel}
            footer={null}
          >
            {/* <ModalAddUse onSubmit={onSubmit} /> */}
            <ModalAddColor onSubmit={onSubmit} />
          </Modal>
        )}
        {modalEdit && (
          <Modal
            title="Editar"
            visible={modalEdit}
            onCancel={handleCancel}
            footer={null}
          >
            {/* <ModalAddUse onSubmit={onSubmit} use={use} /> */}
            <ModalAddColor onSubmit={onSubmit} modal={color} />
          </Modal>
        )}
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default color;
