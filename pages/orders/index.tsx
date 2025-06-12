import { NextPage } from 'next';
import { MainLayout } from '../../app/modules/shared';
import { CtxSelector } from '../../app/services/redux/store';
import { ProtectedRoutesDirections } from '../../app/modules/shared/ProtectedRoutesDirections/ProtectedRoutesDirections';
import { useState } from 'react';
// import { getAllOrder } from '../../app/services/Order/CalculatePrice';
import { OrderEmpty } from '../../app/modules/Orders/OrderCard/OrderEmpty';
import { Select, Spin } from 'antd';
import { InfiniteSImpFilters } from '../../app/modules/shared/InfiniteScroll/InfiniteSImpFilters';
import { PaginationResponse } from '../../app/interfaces/paginationResponse';
import { CardOrderAdmin } from '../../app/modules/Admin/CardOrderAdmin';
import { getOrderClientAdmin } from '../../app/interfaces/Response/Admin/OrderClient';
import { OrderByEnum } from '../../app/models';
// import { getAllOrder } from '../../app/services/Order';
const { Option } = Select;

const OrderPage: NextPage = () => {
  const [usesOrders, setUsesOrders] =
    useState<PaginationResponse<getOrderClientAdmin[]>>();
  const [orderBy, setOrderBy] = useState<string>(OrderByEnum.desc);
  const [loading, setloading] = useState(true);
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const { auth } = userInfo;

  const handleChangeOrderBy = (e: string) => {
    setloading(true);
    setOrderBy(e);
  };

  const errorForEmptyResponse = () => {
    setloading(false);
  };

  const getEmptyDirections = () => {
    if (!loading && usesOrders?.content.length === 0) {
      return (
        <div
          className={`flex content-center justify-center py-24 ${usesOrders ? 'block' : 'hidden'}`}>
          <OrderEmpty />
        </div>
      );
    } else {
      <div></div>;
    }
  };

  return (
    <MainLayout title="Pedidos" pageDescription="Historial de pedidos">
      <div>
        <div className="grid w-full grid-cols-2 items-center pb-5 pt-4 ">
          <h1 className="color-main  ml-8  flex self-center text-2xl lg:text-4xl">
            Pedidos
          </h1>
          <div className="m-0 flex-col items-baseline justify-end sm:m-8 sm:flex sm:flex-row">
            <label className="mr-4 font-bold text-black">Ordenar por</label>
            <Select
              onChange={handleChangeOrderBy}
              className="min-w-[10rem]"
              defaultValue={OrderByEnum.desc}
            >
              <Option value={OrderByEnum.desc}>Mas Actual</Option>
              <Option value={OrderByEnum.asc}>Mas Antiguo</Option>
            </Select>
          </div>
        </div>
        <div className="flex flex-col gap-6">
          <ProtectedRoutesDirections auth={auth}>
            {getEmptyDirections()}
            <div
              className={`flex h-96 content-center justify-center py-24 ${loading ? 'block' : 'hidden'
                }`}
            >
              <div className="flex content-center justify-center py-24 ">
                <Spin size="large" className="" />
              </div>
            </div>
            <div className={`${!loading ? 'flex' : 'hidden'} justify-center`}>
              <div className="w-4/5">
                <InfiniteSImpFilters
                  listchange={pagList => {
                    setloading(false);
                    setUsesOrders({
                      pagination: pagList.pagination,
                      content: pagList.content as getOrderClientAdmin[]
                    });
                  }}
                  itemName="Pedidos"
                  height={1000}
                  serviceUrl={`order?filterDate=ALWAYS&typeSort=${orderBy}`}
                  errorFisrtResponse={errorForEmptyResponse}
                >
                  {usesOrders?.content.map(order => (
                    // <OrderCard key={order.id} order={order} />
                    <CardOrderAdmin order={order} key={order.id} />
                  ))}
                </InfiniteSImpFilters>
              </div>
            </div>
          </ProtectedRoutesDirections>
        </div>
      </div>
    </MainLayout>
  );
};

export default OrderPage;
