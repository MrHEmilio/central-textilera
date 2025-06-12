import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';

export const results = Object.freeze({
  active: { key: 'active', label: 'Activo' },
  name: { key: 'name', label: 'Nombre' },
  code: { key: 'code', label: 'Color' }
});

export const colorsAdminTable: ColumnsType = [
  {
    title: results.code.label,
    align: 'center',
    dataIndex: results.code.key,
    key: results.code.key,
    render: (_, record) => {
      const cloth = record as { code: string };
      return (
        <div className="flex justify-center">
          <div
            style={{
              width: '4rem',
              height: '4rem',
              backgroundColor: cloth.code
            }}
          />
        </div>
      );
    }
  },

  {
    title: results.name.label,
    dataIndex: results.name.key,
    align: 'left',
    key: results.name.key,
    width: '500px'
  },
  {
    title: results.active.label,
    dataIndex: results.active.key,
    key: results.active.key,
    align: 'center',
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
  }
];
