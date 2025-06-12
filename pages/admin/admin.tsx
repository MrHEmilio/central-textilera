import { Button } from 'antd';
import Link from 'next/link';
import React from 'react';
import { ProtectAdmin } from '../../app/modules/Admin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { useRouter } from 'next/router';
import { AdminTable } from '../../app/models/TableAdmin/AdminTable';

const admin = () => {
  const navigate = useRouter();
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const editAdmin = (infoAdmin: any) => {
    const { id } = infoAdmin;
    
    navigate.push(`/admin/editadmin/${id}`);

  }

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Administrador</h1>
            <div>
              <Link href={'/admin/createadmin'}>
                <Button type="primary" className="button-ctx w-full">
                  Agregar Administrador
                </Button>
              </Link>
            </div>
          </div>
          <div className="">
            <TableAdmin
              orderBy
              colums={AdminTable}
              url={'admin'}
              onCickRow={editAdmin}
            />
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default admin;
