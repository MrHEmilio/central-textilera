import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import Error from '/public/img/noImage.jpg';
import { CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons';

export const results = Object.freeze({
  img: { key: 'img' },
  active: { key: 'active', label: 'Activo' },
  name: { key: 'name', label: 'Nombre' },
  ventas: { key: 'amount', label: 'Ventas' }
});

export const DashboardTable: ColumnsType = [
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
        <div className="flex justify-start">
          <p>{cloth.name}</p>
        </div>
      );
    }
  },
  {
    title: ()=> (<>Total de {results.ventas.label}</>),
    dataIndex: results.ventas.key,
    align: 'center',
    key: results.ventas.key,
    render: (_, record) => {
      const { amount, cloth: { billing: { unitLabel: clothUnitLabel } } } = record as {
        amount: string;
        cloth: { billing: { unitLabel: string } };
      };

      return (
        <div className="flex justify-center">
          <p>{`${Number(amount).toLocaleString()} ${clothUnitLabel} 's`}</p>
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
            <Tag icon={<CheckCircleOutlined />} color="success">Activo</Tag>
          ) : (
            <Tag icon={<CloseCircleOutlined />} color="error">Desactivado</Tag>
          )}
        </div>
      );
    }
  }
];
