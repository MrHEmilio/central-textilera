import React, { useState } from 'react';
import { DashboardTable } from '../../models/TableAdmin/DashboardTable';
import { rechargeTableAction } from '../../services/redux/actions/AdminTable';
import { CtxDispatch } from '../../services/redux/store';
import TableAdmin from '../Admin/TableAdmin';
import { HomecardInfo } from './HomecardInfo';
import { DashboardSold } from '../../models/TableAdmin/DashboardSold';

export const HomoCardTable = () => {
  const dispatch = CtxDispatch();
  const [typeSearch, setTypeSearch] = useState('mostSold');
  const [date, setDate] = useState<{
    dateName: string;
    dateStart: string;
    dateEnd: string;
  }>({
    dateName: 'THIS_MONTH',
    dateStart: '',
    dateEnd: ''
  });
  const onChangeFilter = (date: string, dateStart: string, dateEnd: string) => {
    if (date !== 'RANGE') {
      setDate({ dateName: date, dateEnd: '', dateStart: '' });
      dispatch(rechargeTableAction(true));
    } else {
      setDate({ dateName: date, dateEnd: dateEnd, dateStart: dateStart });
      dispatch(rechargeTableAction(true));
      //   sale(date, dateStart, dateEnd);
    }
  };
  return (
    <>
      <HomecardInfo
        body={
          typeSearch === 'mostSold' ? 'Telas más vendidas' : 'Telas agotadas'
        }
        title=""
        icon={typeSearch === 'mostSold' ? 'stars' : 'cancel'}
        colorFooter="#64748b"
        filter={true}
        onChange={onChangeFilter}
        filterType={true}
        onChangeType={e => {
          setTypeSearch(e);
          dispatch(rechargeTableAction(true));
        }}
        classNameBody="block"
        renderTitle={false}
        childrenBody={
          <TableAdmin
            colums={typeSearch === 'mostSold' ? DashboardTable : DashboardSold}
            url={`report/cloth/${
              typeSearch === 'mostSold' ? 'most/sold' : 'sold/out'
            }?filterDate=${date.dateName}&${
              date.dateName === 'RANGE'
                ? `&dateStart=${date.dateStart}&dateEnd=${date.dateEnd}&`
                : ''
            }`}
            orderBy={false}
            search={false}
            rowKey={(obj: { cloth: { id: string } }) => obj.cloth.id}
            paginationTop={false}
            mb={false}
            defaultPageSize={5}
            rowClassName=""
          />
        }
      />
    </>
  );
};
