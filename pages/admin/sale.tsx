import { Button, Modal } from 'antd';
import React, { useState } from 'react';
import { salesAdminTable } from '../../app/models/TableAdmin/Sale';
import { ProtectAdmin } from '../../app/modules/Admin';
import { ModalAddSale } from '../../app/modules/Admin/ModalAddSale';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';
import { CtxDispatch } from '../../app/services/redux/store';
import { DataTypeSale } from '../../app/interfaces/Response/Admin/typeSale';

const sale = () => {
  const dispatch = CtxDispatch();
  const [open, setOpen] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);

  const [sale, setSale] = useState<DataTypeSale>({
    name: 'nombre',
    active: true,
    id: 'novalido',
    abbreviation: 'abreviatura'
  });

  const showModal = () => {
    setOpen(true);
  };

  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
  };
  
  const editUse = (sale: DataTypeSale) => {
    
    setModalEdit(true);
    setSale(sale);
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
            <h1 className="color-main pb-5 pt-4 text-3xl">Tipo de venta</h1>
            <div>
              <Button
                type="primary"
                className="button-ctx w-full"
                onClick={showModal}
              >
                Agregar tipo de venta
              </Button>
            </div>
          </div>
          <div className="">
            <TableAdmin
              colums={salesAdminTable}
              url={'sale'}
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
          <ModalAddSale onSubmit={onSubmit} />
        </Modal>
        <Modal
          title="Editar"
          visible={modalEdit}
          onCancel={handleCancel}
          footer={null}
        >
          <ModalAddSale onSubmit={onSubmit} modal={sale} />
        </Modal>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default sale;
