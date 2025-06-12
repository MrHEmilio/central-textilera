import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';

export const results = Object.freeze({
  //   img: { key: 'img' },
  active: { key: 'active', label: 'Activo' },
  name: { key: 'name', label: 'Nombre' },
  abbreviation: { key: 'abbreviation', label: 'Abreviatura' }
  //   description: { key: 'description', label: 'Descripción' }
});

export const salesAdminTable: ColumnsType = [
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
    width: '500px'
  },
  {
    title: results.abbreviation.label,
    dataIndex: results.abbreviation.key,
    align: 'left',
    key: results.abbreviation.key,
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
