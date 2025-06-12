import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import { GoogleIcon } from '../../modules/shared';

export const results = Object.freeze({
  name: { key: 'name', label: 'Nombre' },
  phone: { key: 'phone', label: 'Telefono' },
  email: { key: 'email', label: 'Correo' },
  active: { key: 'active', label: 'Activo' }
});

export const AdminTable: ColumnsType = [
  //   {
  //     title: '',
  //     dataIndex: results.img.key,
  //     key: results.img.key,
  //     render: (_, record) => {
  //       const cloth = record as { image: string };
  //       return (
  //         <div className="flex justify-center">
  //           <img style={{ width: '4rem', height: '2rem' }} src={cloth?.image} />
  //         </div>
  //       );
  //     }
  //   },

  {
    title: results.name.label,
    dataIndex: results.name.key,
    align: 'left',
    key: results.name.key,
    width: '500px',
    render: (_, record) => {
      const client = record as {
        name: string;
        firstLastname: string;
        secondLastname: string;
        root: boolean;
      };

      return (
        <div className="flex items-center gap-2">
          {client.name} {client.firstLastname}{' '}
          {client.secondLastname ? client.secondLastname : ''}
          {client.root && (
            <GoogleIcon className={'text-sm text-main'} icon={'settings'} />
          )}
        </div>
      );
    }
  },
  //   {
  //     title: results.phone.label,
  //     dataIndex: results.phone.key,
  //     align: 'center',
  //     key: results.phone.key,
  //     width: '500px',
  //     render: (_, record) => {
  //       const client = record as {
  //         phone: string;
  //         countryCode: {
  //           id: string;
  //           name: string;
  //           code: string;
  //         };
  //       };

  //       const firstPart = client.phone.slice(0, 5);
  //       const secondPart = client.phone.slice(5, 10);

  //       return `${client.countryCode.code} ${firstPart} - ${secondPart}`;
  //     }
  //   },
  {
    title: results.email.label,
    dataIndex: results.email.key,
    align: 'center',
    key: results.email.key,
    width: '500px'
  },
  {
    title: results.active.label,
    dataIndex: results.active.key,
    key: results.active.key,
    align: 'center',
    width: '500px',
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
