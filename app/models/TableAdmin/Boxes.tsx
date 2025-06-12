import Icon, {
  CustomIconComponentProps
} from '@ant-design/icons/lib/components/Icon';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';

export const boxesAdminTable: ColumnsType = [
  {
    title: 'Nombre',
    dataIndex: 'name'
  },
  {
    title: 'Ancho (cm)',
    dataIndex: 'width'
  },
  {
    title: 'Altura (cm)',
    dataIndex: 'height'
  },
  {
    title: 'Profundidad (cm)',
    dataIndex: 'depth'
  },
  {
    title: 'Color',
    dataIndex: 'colorCode',
    render: (_, record) => {
      const boxColor = record as { colorCode: string };
      const boxSvg = () => (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="35.992"
          height="36"
          viewBox="0 0 35.992 36"
        >
          <path
            id="cajas"
            d="M3.045,34.443V12.3a1.942,1.942,0,0,1-1.212-.9A2.964,2.964,0,0,1,1.25,9.645V3.934a2.643,2.643,0,0,1,.81-1.871A2.526,2.526,0,0,1,3.946,1.23H34.538a2.609,2.609,0,0,1,1.871.833,2.554,2.554,0,0,1,.833,1.871V9.645a2.981,2.981,0,0,1-.583,1.757,1.915,1.915,0,0,1-1.212.9V34.443a2.731,2.731,0,0,1-.833,1.909,2.524,2.524,0,0,1-1.871.879h-27a2.5,2.5,0,0,1-1.886-.879,2.768,2.768,0,0,1-.81-1.909ZM34.538,9.652V3.934H3.939V9.645h30.6ZM13.838,21.8h10.8l-10.8,7.832V21.8Z"
            transform="translate(-1.25 -1.23)"
            fill={boxColor.colorCode || '#ae843a'}
          />
        </svg>
      );
      const BoxIcon = (props: Partial<CustomIconComponentProps>) => (
        <Icon component={boxSvg} />
      );

      return (
        <div>
          <BoxIcon />
        </div>
      );
    }
  },
  {
    title: 'Activo',
    dataIndex: 'active',
    render: (_, record) => {
      const box = record as { active: boolean };
      return (
        <div>
          {box?.active ? (
            <Tag color="green">Activo</Tag>
          ) : (
            <Tag color="red">Desactivado</Tag>
          )}
        </div>
      );
    }
  }
];
