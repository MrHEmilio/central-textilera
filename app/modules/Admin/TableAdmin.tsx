import { SearchOutlined } from '@ant-design/icons';
import { Empty, Input, Select, Table } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import React, { ChangeEvent, FC, useEffect, useState } from 'react';
import useDebounce from '../../Hooks/useDebouncer';
import { Pagination } from '../../interfaces/paginationResponse';
import { getDataTable } from '../../services/TableAdmin/TableAdmin';
import { OrderByEnum } from '../../models/Enums';
import { CtxDispatch, CtxSelector } from '../../services/redux/store';
import { rechargeTableAction } from '../../services/redux/actions/AdminTable';
import {
  LoaderActionsShow,
  LoaderActionsHide
} from '../../services/redux/actions/LoaderActions';

interface Props {
  colums: ColumnsType<any>;
  url: string;
  rowKey?: any;
  orderBy?: boolean;
  // eslint-disable-next-line no-unused-vars
  onCickRow?: (arg0: any) => void;
  search?: boolean;
  paginationTop?: boolean;
  mb?: boolean;
  defaultPageSize?: number;
  urlSearch?: string;
  rowClassName?: string;
  searchValue?: () => boolean;
}

const TableAdmin: FC<Props> = ({
  colums,
  rowKey = 'id',
  url,
  onCickRow,
  orderBy = true,
  search = true,
  paginationTop = true,
  mb = true,
  defaultPageSize = 10,
  urlSearch = 'search',
  rowClassName = 'cursor-pointer',
  searchValue
}) => {
  const { Option } = Select;
  const { recharge } = CtxSelector((state: any) => state.admintable);
  const dispatch = CtxDispatch();
  const [allData, setAllData] = useState<any[]>([]);
  const [searchCurrent, setSearchCurrent] = useState('');
  const [orderCurrent, setOrderCurrent] = useState(orderBy ? 'ASC' : '');
  const [loading, setLoading] = useState<boolean>(false);
  const [value, setValue] = useState<string>('');
  const debouncedValue = useDebounce<string>(value, 1500);
  const [paginationState, setPaginationState] = useState<Pagination>({
    page: 1,
    size: 5,
    totalPage: 1,
    totalRecords: 5
  });

  const getBanner = async (
    size = defaultPageSize,
    page = 1,
    search = '',
    order = orderBy ? 'ASC' : ''
  ) => {
    setOrderCurrent(order);
    dispatch(LoaderActionsShow());
    setLoading(true);
    let urlFinal = url.includes('?') ? url : url + '?';
    if (search) {
      setSearchCurrent(search);
      urlFinal = `${urlFinal}${urlSearch}=${search}&`;
    }
    if (order) {
      urlFinal = `${urlFinal}columnSort=name&typeSort=${order}&`;
    }
    const response = await getDataTable(size, page, urlFinal);

    if (response) {
      setAllData(response.content);
      setPaginationState(response.pagination);
    } else {
      setAllData([]);
      setPaginationState({
        page: 1,
        size: defaultPageSize,
        totalPage: 1,
        totalRecords: 5
      });
    }
    dispatch(LoaderActionsHide());
    setLoading(false);
  };

  useEffect(() => {
    if (searchValue) {
      setValue('');
      setLoading(true);
    }
  }, [searchValue]);

  useEffect(() => {
    if (recharge === true) {
      getBanner(
        paginationState.size,
        paginationState.page,
        searchCurrent,
        orderCurrent
      );
      dispatch(rechargeTableAction(false));
    }
  }, [recharge]);

  useEffect(() => {
    if (!debouncedValue) {
      setSearchCurrent(debouncedValue);
      getBanner(defaultPageSize, 1, '', orderCurrent);
      return;
    }
    getBanner(defaultPageSize, 1, debouncedValue, orderCurrent);
  }, [debouncedValue]);

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    setLoading(true);
    setValue(event.target.value);
  };
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const handleOnChange = (_p: any) => {
    const { current, pageSize } = _p;
    getBanner(pageSize, current, searchCurrent, orderCurrent);
  };

  const handleChangeOrderBy = (e: any) => {
    getBanner(paginationState.size, paginationState.page, searchCurrent, e);
  };

  const setCurrentChange = (page: any, pageSize: any) => {
    if (typeof window !== 'undefined') {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // getBanner(pageSize, page, searchCurrent);
    setPaginationState({ ...paginationState, page, size: pageSize });
  };

  const setPagination = (): any => {
    return {
      pagination: {
        current: paginationState.page,
        defaultPageSize: defaultPageSize,
        pageSize: paginationState.size,
        showSizeChanger: true,
        pageSizeOptions: ['10', '15', '20'],
        onChange: setCurrentChange,
        total: paginationState.totalRecords,
        position: paginationTop ? ['topRight', 'bottomRight'] : ['bottomRight']
      }
    };
  };

  return (
    <div
      className={`relative  ${orderBy ? 'sm:mt-12' : 'sm:mt-0'} ${
        orderBy ? 'lg:mt-8' : 'lg:mt-0'
      }  2xl:mt-0`}
    >
      <div
        className={`absolute z-50 mt-4 flex justify-end ${
          orderBy ? 'sm:top-[-4.5rem]' : 'sm:top-[-.3rem]'
        } ${orderBy ? 'lg:top-[-3.2rem]' : 'lg:top-[-.3rem]'} 2xl:top-[-.3rem]`}
      >
        <div className="flex items-center sm:flex-col 2xl:flex-row">
          {orderBy && (
            <div className=" mr-6 sm:mb-2 sm:flex sm:flex-col lg:block 2xl:mb-0 ">
              <label className="mr-4 font-bold text-black">Ordenar por</label>
              <Select
                onChange={handleChangeOrderBy}
                className="min-w-[10rem]"
                defaultValue={OrderByEnum.asc}
              >
                <Option value={OrderByEnum.asc}>A-Z</Option>
                <Option value={OrderByEnum.desc}>Z-A</Option>
              </Select>
            </div>
          )}
          {search && (
            <Input
              placeholder="Buscar"
              // onSearch={onSearch}
              onChange={handleChange}
              value={value}
              className={`custom-input  custom-searchinput sm:w-48 lg:w-72`}
              suffix={<SearchOutlined />}
            />
          )}
        </div>
      </div>
      <div className={`${mb ? 'mb-12' : 'mb-0'}  `}>
        <Table
          onChange={handleOnChange}
          locale={{
            emptyText: (
              <Empty
                description="No existen datos para la tabla"
                image={Empty.PRESENTED_IMAGE_SIMPLE}
              />
            )
          }}
          rowClassName={rowClassName}
          columns={colums}
          dataSource={allData}
          rowKey={rowKey}
          // rowKey={obj => obj.cloth.id}
          loading={loading}
          {...setPagination()}
          onRow={record => {
            return {
              onClick: () => {
                if (onCickRow) {
                  onCickRow(record);
                }
              }
            };
          }}
        />
      </div>
    </div>
  );
};

export default TableAdmin;
