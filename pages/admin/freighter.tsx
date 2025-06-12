import { Button, Modal } from 'antd';
import { NextPage } from 'next';
import { FreighterColumsTable } from '../../app/models/TableAdmin/Freighter';
import { ProtectAdmin } from '../../app/modules/Admin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { useState } from 'react';
import { dataFreighter } from '../../app/interfaces/Response/Admin/Freighter';
import { CtxDispatch } from '../../app/services/redux/store';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';
import { ModalAddFreighter } from '../../app/modules/Admin/ModalAddFreighter';

const Boxes: NextPage = () => {
  const dispatch = CtxDispatch();
  const [open, setOpen] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  
  const [freighter, setFreighter] = useState<dataFreighter>({
    id: 'novalido',
    name: 'string',
    image: 'string',
    costPerDistance: 0,
    costPerWeight: 0,
    active: true,
  });

  const showModal = () => {
    setOpen(true);
  };

  const handleCancel = () => {
    setOpen(false);
    setModalEdit(false);
  };

  const editFreighter = (freighter: dataFreighter) => {
    setModalEdit(true);
    setFreighter(freighter);
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
            <h1 className="color-main pb-5 pt-4 text-3xl">Fleteras</h1>
            <div>
              <Button
                type="primary"
                className="button-ctx w-full"
                onClick={showModal}
              >
                Agregar fletera
              </Button>
            </div>
          </div>
          <div className="">
            <TableAdmin
              colums={FreighterColumsTable}
              url={'freighter'}
              onCickRow={editFreighter}
            />
          </div>
        </div>
        <Modal
          title="Agregar fletera"
          visible={open}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose={true}
        >
          <ModalAddFreighter onSubmit={onSubmit} />
        </Modal>
        <Modal
          title="Editar fletera"
          visible={modalEdit}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose={true}
        >
          <ModalAddFreighter onSubmit={onSubmit} modal={freighter} />
        </Modal>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default Boxes;
