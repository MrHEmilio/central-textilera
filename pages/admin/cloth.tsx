import { Button } from 'antd';
import { NextPage } from 'next';
import Link from 'next/link';
import { useRouter } from 'next/router';
import React, { useEffect } from 'react';
import { clothAdminTable } from '../../app/models/TableAdmin/Cloth';
import { ProtectAdmin } from '../../app/modules/Admin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import {
  CleanClothForm,
  SetClothForm
} from '../../app/services/redux/actions/ClothFormActions';
import { CtxDispatch } from '../../app/services/redux/store';

interface DataType {
  key: React.Key;
  name: string;
  age: number;
  address: string;
}

const data: DataType[] = [];
for (let i = 0; i < 46; i++) {
  data.push({
    key: i,
    name: `Edward King ${i}`,
    age: 32,
    address: `London, Park Lane no. ${i}`
  });
}
const ClothPage: NextPage = () => {
  const dispatch = CtxDispatch();
  const router = useRouter();
  useEffect(() => {
    dispatch(CleanClothForm());
  }, []);
  return (
    <AdminLayout title={'Administración'} pageDescription={'Administración'}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Telas</h1>
            <div>
              <Link href={'/admin/createcloth'}>
                <Button type="primary" className="button-ctx w-full">
                  Agregar Tela
                </Button>
              </Link>
            </div>
          </div>
          <div className="">
            {/* <Table columns={columns} dataSource={data} /> */}
            <TableAdmin
              colums={clothAdminTable}
              url={'cloth?responseStructure=ALL&'}
              onCickRow={v => {
                dispatch(SetClothForm(v));
                router.push('createcloth');
              }}
            />
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default ClothPage;
