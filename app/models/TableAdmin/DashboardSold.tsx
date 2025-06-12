import { ColumnsType } from 'antd/lib/table';
import Error from '/public/img/noImage.jpg';
import { Tag } from 'antd';

export const results = Object.freeze({
  img: { key: 'img' },
  active: { key: 'active', label: 'Activo' },
  name: { key: 'name', label: 'Nombre' }
});

export const DashboardSold: ColumnsType = [
  {
    title: '',
    dataIndex: results.img.key,
    key: results.img.key,
    render: (_, record) => {
      const clothM = record as { cloth: { image: string } };

      const { cloth } = clothM;

      return (
        <div className="flex justify-center">
          <img
            style={{ width: '2rem', height: '2rem' }}
            src={cloth.image}
            onError={e => {
              e.currentTarget.onerror = null;
              e.currentTarget.src = Error.src;
            }}
          />
        </div>
      );
    }
  },
  {
    title: results.name.label,
    dataIndex: results.name.key,
    align: 'center',
    key: results.name.key,
    render: (_, record) => {
      const clothM = record as { cloth: { name: string } };

      const { cloth } = clothM;

      return (
        <div className="flex justify-center">
          <p>{cloth.name}</p>
        </div>
      );
    }
  },
  {
    title: results.active.label,
    dataIndex: results.active.key,
    key: results.active.key,
    align: 'center',
    render: (_, record) => {
      const clothM = record as { cloth: { name: string; active: boolean } };

      const { cloth } = clothM;

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
