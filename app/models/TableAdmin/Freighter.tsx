import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';

export const FreighterColumsTable: ColumnsType = [
  { title: 'Nombre', dataIndex: 'name' },
  {
    title: 'Fletera',
    dataIndex: 'image',
    render: (_, record) => {
      const freighter = record as { image: string };
      return (
        <div className="flex justify-center">
          <img
            style={{ width: '4rem', height: '4rem' }}
            src={freighter?.image}
          />
        </div>
      );
    }
  },
  {
    title: 'Costo por distancia (Km)',
    dataIndex: 'costPerDistance'
  },
  {
    title: 'Costo por peso (Kg)',
    dataIndex: 'costPerWeight'
  },
  {
    title: 'Estatus',
    dataIndex: 'active',
    render: (_, record) => {
      const freighter = record as { active: boolean };
      return (
        <div>
          {freighter?.active ? (
            <Tag color="green">Activo</Tag>
          ) : (
            <Tag color="red">Desactivado</Tag>
          )}
        </div>
      );
    }
  }
];
