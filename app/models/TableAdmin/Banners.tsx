import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import Error from '/public/img/noImage.jpg';

export const results = Object.freeze({
  img: { key: 'img' },
  active: { key: 'active', label: 'Activo' },
  waitTime: { key: 'waitTime', label: 'Tiempo (s)' },
  description: { key: 'description', label: 'Descripción' }
});

export const bannerAdminTable: ColumnsType = [
  {
    title: '',
    dataIndex: results.img.key,
    key: results.img.key,
    render: (_, record) => {
      const cloth = record as { image: string };

      return (
        <div className="flex justify-center">
          <img
            style={{ width: '4rem', height: '2rem' }}
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
    title: results.description.label,
    dataIndex: results.description.key,
    align: 'left',
    key: results.description.key,
    width: '500px'
  },
  {
    title: results.waitTime.label,
    dataIndex: results.waitTime.key,
    key: results.waitTime.key,
    align: 'center'
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
