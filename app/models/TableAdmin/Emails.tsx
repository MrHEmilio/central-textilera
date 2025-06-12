import { Button, Tag, Tooltip } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import { SingleEmailTemplate } from '../../interfaces/Response/Admin/EmailServicesResponses';
import { ModalSendEmailTemplate } from '../../modules/Admin';
import { GoogleIcon } from '../../modules/shared';

export const EmailAdminTable: ColumnsType = [
  {
    title: 'Nombre',
    dataIndex: 'name'
  },
  {
    title: 'Asunto',
    dataIndex: 'subject'
  },
  {
    title: 'Activo',
    dataIndex: 'active',
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
  },
  {
    title: 'Enviar correo',
    align: 'center',
    render: (_, record) => {
      return (
        <ModalSendEmailTemplate template={record as SingleEmailTemplate} />
      );
    }
  }
];
