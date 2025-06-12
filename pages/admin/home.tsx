import React from 'react';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { HomeAdmin } from '../../app/modules/HomeAdmin/HomeAdmin';
import { ProtectAdmin } from '../../app/modules/Admin/ProtectAdmin';

const home = () => {
  return (
    <AdminLayout title={'Administración'} pageDescription={'Administración'}>
      <ProtectAdmin>
        <HomeAdmin />
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default home;
