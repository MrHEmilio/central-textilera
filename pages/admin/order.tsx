import { Col, Modal, Row, DatePicker, Button } from 'antd';
import React, { useEffect, useState } from 'react';
import { ProtectAdmin } from '../../app/modules/Admin';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';

import { orderAdminTable } from '../../app/models/TableAdmin/Orders';
import { ModalEditorder } from '../../app/modules/Admin/ModalEditorder';
import { getOrderClientAdmin } from '../../app/interfaces/Response/Admin/OrderClient';
import { CtxDispatch } from '../../app/services/redux/store';
import { rechargeTableAction } from '../../app/services/redux/actions/AdminTable';
import { getStatusCatalog } from '../../app/services/Catalogs';
import { CatalogStatus } from '../../app/interfaces/Response/Catalog/Catalog';
import {
  getPaymentMethod,
  getDeliveryMethod
} from '../../app/services/Catalogs/CatalogsService';
import { FilterOrders } from '../../app/modules/Admin/FilterOrders';
import { RangeDate } from '../../app/models/TableAdmin/FiltroOrder';

const { RangePicker } = DatePicker;

const order = () => {
  // const [open, setOpen] = useState(false);
  const dispatch = CtxDispatch();
  const [modalEdit, setModalEdit] = useState(false);
  // const [url, setUrl] = useState('order?filterDate=ALWAYS&');
  const [rangeDate, setRangeDate] = useState(true);//False
  const [orderState, setOrderState] = useState<getOrderClientAdmin | null>();
  const [status, setStatus] = useState<CatalogStatus[]>([]);
  const [payment, setPayment] = useState<CatalogStatus[]>([]);
  const [delivery, setDelivery] = useState<CatalogStatus[]>([]);
  const [renderDate, setRenderDate] = useState(true);//True
  const [filters, setFilters] = useState({
    status: '',
    paymentMethod: '',
    delivery: '',
    date: 'ALWAYS',//ALWAYS THIS_WEEK
    dataStart: '',
    dateEnd: '',
    cleanValue: false
  });
  const editModal = (order: getOrderClientAdmin) => {
    setOrderState(order);
    setModalEdit(true);
  };

  const handleCancel = () => {
    setModalEdit(false);
    dispatch(rechargeTableAction(true));
  };

  const onSubmit = () => {
    setModalEdit(false);
    dispatch(rechargeTableAction(true));
  };
  const handleStatus = (e: string | boolean, filter: string) => {

    switch (filter) {
      case 'status':
        setFilters({ ...filters, status: e  as string});
        break;
      case 'payment':
        setFilters({ ...filters, paymentMethod: e as string});
        break;
      case 'delivery':
        setFilters({ ...filters, delivery: e as string });
        break;
      case 'date':
        if (e === 'RANGE') {
          setRangeDate(true);
          return;
        } else {
          setRenderDate(false);
          setRangeDate(false);
          setFilters({ ...filters, date: e as string, dataStart: '', dateEnd: '' });
          setTimeout(() => {
            setRenderDate(true);
          }, 200);
        }
        break;
      default:
        break;
    }

    dispatch(rechargeTableAction(true));
  };

  const catalogStatus = async () => {
    const response = await getStatusCatalog();
    if (response) {
      setStatus(response.content);
    }
  };

  const catalogPayment = async () => {
    const response = await getPaymentMethod();
    if (response) {
      setPayment(response.content);
    }
  };

  const catalogDelivery = async () => {
    const response = await getDeliveryMethod();
    if (response) {
      setDelivery(response.content);
    }
  };

  useEffect(() => {
    catalogStatus();
    catalogPayment();
    catalogDelivery();
  }, []);
  
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const onDateChange = (e: any) => {
    if (e) {
      const f = e[0].format('DD/MM/YYYY');
      let s = e[1].format('DD/MM/YYYY');
      if (f === s) {
        const ss = e[1].add(1, 'days');
        s = ss.format('DD/MM/YYYY');
      }
      setFilters({
        ...filters,
        dataStart: f,
        dateEnd: s,
        date: 'RANGE',//THIS_WEEK'
      });
      dispatch(rechargeTableAction(true));
    }
  };

  const onClean = () => {
    setRenderDate(false);
    setFilters({
      status: '',
      paymentMethod: '',
      delivery: '',
      date: 'ALWAYS',//ALWAYS THIS_WEEK
      dataStart: '',
      dateEnd: '',
      cleanValue: true
    });
    setRangeDate(false);
    dispatch(rechargeTableAction(true));
    setTimeout(() => {
      setRenderDate(true);
    }, 300);
  };

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Pedidos</h1>
          </div>
          <div>
            <Row gutter={[{}, { lg: 16, md: 16 }]}>
              <Col lg={6} xl={4} md={{ span: 6 }}>
                <FilterOrders
                  options={status}
                  onChange={e => handleStatus(e, 'status')}
                  labelStr="Estatus:"
                  value={filters.status}
                />
              </Col>
              <Col lg={6} md={6} xl={4}>
                <FilterOrders
                  options={payment}
                  labelStr="Método de Pago:"
                  onChange={e => handleStatus(e, 'payment')}
                  value={filters.paymentMethod}
                />
              </Col>
              <Col lg={6} md={6} xl={4}>
                <FilterOrders
                  options={delivery}
                  labelStr="Método de Envío:"
                  onChange={e => handleStatus(e, 'delivery')}
                  value={filters.delivery}
                />
              </Col>

              <Col lg={6} md={6} xl={4}>
                <FilterOrders
                  options={RangeDate}
                  labelStr="Por Fecha:"
                  onChange={e => handleStatus(e, 'date')}
                  defaultValue={'ALWAYS'}
                  value={rangeDate ? 'RANGE' : filters.date}
                />
              </Col>

              <Col lg={6} md={{ span: 6 }} xl={6}>
                <div className="mr-6 flex flex-col">
                  <label className="mr-4 font-bold text-black">
                    Rango de fechas
                  </label>
                  {renderDate && (
                    <RangePicker
                      placeholder={['Fecha inicio', 'Fecha fin']}
                      disabled={!rangeDate}
                      onChange={onDateChange}
                    // value={[moment(), moment()]}
                    />
                  )}
                </div>
              </Col>

              <Col lg={6} md={6} xl={2} className="flex justify-start">
                <Button
                  type="primary"
                  onClick={onClean}
                  className="button-ctx mt-3 mr-0"
                >
                  Limpiar
                </Button>
              </Col>
            </Row>
          </div>
          <div className="">
            <TableAdmin
              searchValue={() => {
                const toSend = filters.cleanValue;
                setFilters({ ...filters, cleanValue: false });
                return toSend;
              }}
              orderBy={false}
              colums={orderAdminTable}
              url={`order?&typeSort=ASC&filterDate=${filters.date}${rangeDate
                  ? `&dateStart=${filters.dataStart}&dateEnd=${filters.dateEnd}`
                  : ''
                }&orderStatus=${filters.status}&paymentMethod=${filters.paymentMethod
                }&deliveryMethod=${filters.delivery}&`}
              onCickRow={editModal}
            />
          </div>
          {modalEdit && (
            <Modal
              title={
                <div className=" flex ">
                  <p className="mr-2 font-bold text-main underline">
                    Pedido: <span>{`#${orderState?.number}`}</span>
                  </p>
                </div>
              }
              visible={modalEdit}
              onCancel={handleCancel}
              footer={null}
              width={1000}
              bodyStyle={{
                padding: 0,
                paddingLeft: '.5rem',
                paddingRight: '.5rem',
                borderRadius: '29px'
              }}
            >
              {orderState && (
                <ModalEditorder order={orderState} onSubmit={onSubmit} />
              )}
            </Modal>
          )}
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default order;
