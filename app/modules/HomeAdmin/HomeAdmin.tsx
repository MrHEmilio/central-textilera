import { Col, Row } from 'antd';
import React, { useEffect, useState } from 'react';

import { HomecardInfo } from './HomecardInfo';

import {
  getReportSale,
  getReportClient,
  getReportCloth,
  getClothSoldOut
} from '../../services/admin/Dashboard';
import { formatNumber, formatNumberComma } from '../../services/utils';
import { getAllOrder } from '../../services/Order/CalculatePrice';
import { HomeCardInfoDouble } from '../Admin/HomeCardInfoDouble';

import { HomoCardTable } from './HomoCardTable';

export const HomeAdmin = () => {
  // const [filterSale, setFilterSale] = useState('ALWAYS');
  const [sales, setSales] = useState(0);
  const [client, setClient] = useState(0);
  const [registerCloth, setRegisterCloth] = useState(0);
  const [totalOrder, setTotalOrder] = useState(0);
  const [pendingOrder, setPendingOrder] = useState(0);
  const [loading, setLoading] = useState(false);
  const [clothSO, setClothSO] = useState(0);

  const onChangeFilter = (date: string, dateStart: string, dateEnd: string) => {
    if (date !== 'RANGE') {
      sale(date);
    } else {
      sale(date, dateStart, dateEnd);
    }
  };

  const sale = async (date: string, dateStart = '', dateEnd = '') => {
    setLoading(true);
    const response = await getReportSale(date, dateStart, dateEnd);
    if (response) {
      setSales(response.total);
    }
    setLoading(false);
  };

  const clietReport = async () => {
    const response = await getReportClient();
    if (response) {
      setClient(response.total);
    }
  };

  const clothRegister = async () => {
    const response = await getReportCloth();
    if (response) {
      setRegisterCloth(response.total);
    }
  };

  const getAllOrderDasboard = async () => {
    const response = await getAllOrder(1);
    if (response) {
      setTotalOrder(response.pagination.totalRecords);
    }
  };

  const getAllOrderPending = async () => {
    const response = await getAllOrder(1, 'REVISION');
    if (response) {
      setPendingOrder(response.pagination.totalRecords);
    }
  };

  const getClotSO = async () => {
    const respose = await getClothSoldOut();
    if (respose) {
      setClothSO(respose.pagination.totalRecords);
    }
  };

  useEffect(() => {
    sale('THIS_MONTH');
    clietReport();
    clothRegister();
    getAllOrderDasboard();
    getAllOrderPending();
    getClotSO();
  }, []);

  return (
    <div className="mb-6">
      <h1 className="text-l ml-8 mb-8 font-bold text-main">
        Este es el estado de tu tienda el día de hoy:
      </h1>
      <div className="flex justify-center">
        <Row className="w-[90%]" gutter={[24, 16]}>
          <Col span={14}>
            <HomecardInfo
              body={`$${formatNumber(sales)}`}
              title="Total de ventas"
              icon={'payments'}
              colorFooter="#64748b"
              filter={true}
              onChange={onChangeFilter}
              loading={loading}
            />
          </Col>
          <Col span={10}>
            <HomeCardInfoDouble
              bodyLeft={`${formatNumberComma(totalOrder)}`}
              titleLeft="Pedidos"
              iconLeft={'shopping_bag'}
              footerLink
              url="/admin/order"
              colorFooter="#d8dfe8"
              bodyRight={`${formatNumberComma(pendingOrder)}`}
              iconRight="lock_Clock"
              titleRight="Pendientes"
            />
          </Col>
          <Col span={14}>
            <HomoCardTable />
          </Col>
          <Col span={10}>
            <Row gutter={[0, 24]}>
              <Col span={24}>
                <HomeCardInfoDouble
                  bodyLeft={`${formatNumberComma(registerCloth)}`}
                  titleLeft="Telas registradas"
                  iconLeft={'sell'}
                  footerLink
                  url="/admin/cloth"
                  colorFooter="#d8dfe8"
                  bodyRight={`${formatNumberComma(clothSO)}`}
                  iconRight="cancel"
                  titleRight="Telas Agotadas"
                />
              </Col>
              <Col span={24}>
                <HomecardInfo
                  labelDate={false}
                  body={`${formatNumberComma(client)}`}
                  title="Clientes registrados"
                  icon={'group'}
                  colorFooter="#d8dfe8"
                  footerLink
                  url="/admin/client"
                />
              </Col>
            </Row>
          </Col>
        </Row>
      </div>
    </div>
  );
};
