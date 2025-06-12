import { ColumnsType } from 'antd/lib/table';
import { getOrderClientAdmin } from '../../interfaces/Response/Admin/OrderClient';
import { formatNumber, getDate } from '../../services/utils';

export const results = Object.freeze({
  number: { key: 'number', label: 'Pedido' },
  date: { key: 'date', label: 'Fecha' },
  nameClient: { key: 'nameClient', label: 'Cliente' },
  total: { key: 'total', label: 'Total' },
  status: { key: 'status', label: 'Estatus' },
  article: { key: 'article', label: 'Artículos' },
  deliveryMethod: { key: 'deliveryMethod', label: 'Forma de entrega' }
});

export const orderAdminTable: ColumnsType = [
  {
    title: results.number.label,
    dataIndex: results.number.key,
    align: 'center',
    key: results.number.key
    // width: '500px'
  },
  {
    title: results.nameClient.label,
    dataIndex: results.nameClient.key,
    key: results.nameClient.key,
    width: '500px',
    render: (_, record: any) => {
      const order = record as getOrderClientAdmin;
      return <div>{`${order.client.name} ${order.client.firstLastname}`}</div>;
    }
  },
  {
    title: results.article.label,
    dataIndex: results.article.key,
    align: 'center',
    key: results.article.key,
    render: (_, record: any) => {
      const order = record as getOrderClientAdmin;
      return <div>{`${order.products.length} `}</div>;
    }
  },
  {
    title: results.total.label,
    dataIndex: results.total.key,
    align: 'center',
    key: results.total.key,
    width: '500px',
    render: (_, record: any) => {
      const order = record as getOrderClientAdmin;
      return <div>{`$${formatNumber(order.total)} `}</div>;
    }
  },
  {
    title: results.status.label,
    dataIndex: results.status.key,
    align: 'center',
    key: results.status.key,
    width: '500px',
    render: (_, record: any) => {
      const order = record as getOrderClientAdmin;
      return <div>{`${order.statusHistory[order.statusHistory.length-1].status} `}</div>;
    }
  },
  {
    title: results.date.label,
    dataIndex: results.date.key,
    key: results.date.key,
    width: '500px',
    render: (_, record: any) => {
      const order = record as getOrderClientAdmin;
      return (
        <div>
          {getDate(order.statusHistory[order.statusHistory.length-1].date)}
        </div>
      );
    }
  },
  {
    title: results.deliveryMethod.label,
    dataIndex: results.deliveryMethod.key,
    align: 'center',
    key: results.deliveryMethod.key,
    width: '500px'
  }
];
