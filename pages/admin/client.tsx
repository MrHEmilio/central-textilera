import React from 'react';
import { ProtectAdmin } from '../../app/modules/Admin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { clientAdminTable } from '../../app/models/TableAdmin/Client';
import { useRouter } from 'next/router';

const client = () => {
  const navigate = useRouter();
  
  const editClient = (client: {id: string}) => {
    
    const { id } = client;
    navigate.push(`/admin/client/${id}`);
  };

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Clientes</h1>
          </div>
          <div className="">
            <TableAdmin
              orderBy
              colums={clientAdminTable}
              url={'client'}
              onCickRow={editClient}
            />
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default client;
