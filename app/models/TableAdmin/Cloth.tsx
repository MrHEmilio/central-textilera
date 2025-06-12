// export const results =

import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';

export const clothAdminTable: ColumnsType = [
  {
    title: '',
    dataIndex: 'img',
    render: (_, record) => {
      const cloth = record as { image: string };
      return (
        <div className="flex justify-center">
          <img style={{ width: '4rem', height: '4rem' }} src={cloth?.image} />
        </div>
      );
    }
  },
  {
    title: 'Nombre',
    dataIndex: 'name'
  },
  {
    title: 'Activo',
    dataIndex: 'active',
    render: (_, record) => {
      const cloth = record as { active: boolean };
      return (
        <div>
          {cloth?.active ? (
            <Tag color="green">Activo</Tag>
          ) : (
            <Tag color="red">Desactivado</Tag>
          )}
        </div>
      );
    }
  },
  {
    title: 'Descripción',
    dataIndex: 'mainDescription',
    width: '500px'
  }
];
