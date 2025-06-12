import { Button, Modal } from 'antd';
import { NextPage } from 'next';
import React, { useState } from 'react';
import { DataBox } from '../../app/interfaces/Response/Admin/Box';
import { boxesAdminTable } from '../../app/models/TableAdmin/Boxes';
import { ProtectAdmin } from '../../app/modules/Admin';
import { ModalAddBox } from '../../app/modules/Admin/ModalAddBox';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';
import { CtxDispatch } from '../../app/services/redux/store';

const Boxes: NextPage = () => {
  const dispatch = CtxDispatch();
  const [open, setOpen] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  const [box, setBox] = useState<DataBox>({
    name: 'nombre',
    active: true,
    id: 'novalido',
    depht: 0,
    height: 0,
    width: 0,
    colorCode: '#00ff0f'
  });
  const showModal = () => {
    setOpen(true);
  };
  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
  };
  const editBox = (box: DataBox) => {
    setModalEdit(true);
    setBox(box);
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
            <h1 className="color-main pb-5 pt-4 text-3xl">Cajas</h1>
            <div>
              <Button
                type="primary"
                className="button-ctx w-full"
                onClick={showModal}
              >
                Agregar Caja
              </Button>
            </div>
          </div>
          <div className="">
            <TableAdmin
              colums={boxesAdminTable}
              url={'box'}
              onCickRow={editBox}
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
          <ModalAddBox onSubmit={onSubmit} />
        </Modal>
        <Modal
          title="Editar caja"
          visible={modalEdit}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose={true}
        >
          <ModalAddBox onSubmit={onSubmit} modal={box} />
        </Modal>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default Boxes;
