import { DatePicker, Skeleton } from 'antd';
import Link from 'next/link';
import { FC, useState } from 'react';
import { RangeDate } from '../../models/TableAdmin/FiltroOrder';
import { FilterOrders } from '../Admin/FilterOrders';
import { GoogleIcon } from '../shared';
import Arrow from '/public/img/arrow_forward.svg';
import { sellAdmin } from '../../models/Menu';
import { MonthsOfYear } from '../../models';
import { getDateUtc } from '../../services/utils';

const { RangePicker } = DatePicker;
interface Props {
  // eslint-disable-next-line no-unused-vars, @typescript-eslint/no-explicit-any
  onChange?: (arg0: any, arg1: any, arg2: any) => void;
  // eslint-disable-next-line no-unused-vars, @typescript-eslint/no-explicit-any
  onChangeType?: (arg0: any) => void;
  title: string;
  body: string;
  icon: string;
  colorFooter?: string;
  footerLink?: boolean;
  url?: string;
  childrenBody?: React.ReactNode;
  filter?: boolean;
  loading?: boolean;
  filterType?: boolean;
  classNameBody?: string;
  renderTitle?: boolean;
  labelDate?: boolean;
}

/**
 * @description Implemetation for HomecardInfo
 * @param title string para las letras principales del componente
 * @param body string para las letras secundarias del componente
 * @param icon string para el icono de googleFont
 * @param colorFooter string con el codigo del color de fonto del footer
 * @param footerLink opcional para un enlace de ver mas en el footer
 * @param url? opcional direccion url para el enlace del footer
 * @param childrenBody? opcional ReactNode para desplegar un elemento en el body de la card
 * @param filter? opcional boolean pora desplegar un filtro con opciones de busqueda por fecha
 * @param loading? opcional boolean para el loading  del title
 * @param filterType? opcional boolean despliega un select como filtro para telas mas vendidas y agotadas
 * @param classNameBody? opcional string clase para  el container de los filtros
 * @author GuillemoSoto
 */

export const HomecardInfo: FC<Props> = ({
  title,
  body,
  icon,
  colorFooter,
  footerLink,
  url,
  childrenBody,
  filter,
  onChange,
  loading = false,
  filterType,
  onChangeType,
  classNameBody = 'flex justify-between',
  renderTitle = true,
  labelDate = true
}) => {
  const [filterSale, setFilterSale] = useState('THIS_MONTH');
  const [renderDate, setRenderDate] = useState(false);
  const [typeSearch, setTypeSearch] = useState('mostSold');
  const [startDtae, setStartDtae] = useState('');
  const [endDte, setEndDte] = useState('');

  const onDateChange = (e: any) => {
    if (e) {
      const dateStart = e[0].format('DD/MM/YYYY');
      const dateEnd = e[1].format('DD/MM/YYYY');
      setEndDte(dateEnd);
      setStartDtae(dateStart);
      const date = 'RANGE';

      if (onChange) {
        onChange(date, dateStart, dateEnd);
      }
    }
  };

  const getWeek = (date: Date) => {
    const oneJan = new Date(date.getFullYear(), 0, 1);
    const numberOfDays = Math.floor((date.getTime() - oneJan.getTime()) / (24 * 60 * 60 * 1000));
    return Math.ceil((date.getDay() + 1 + numberOfDays) / 7);
  }

  const getLabel = () => {
    const hoy = new Date();

    switch (filterSale) {
      case 'ALWAYS':
        return '';
      case 'TODAY':
        return ` hoy ${getDateUtc(hoy.toLocaleString())}`;
      case 'THIS_WEEK':
        return `de la semana ${getWeek(hoy)} de ${hoy.getUTCFullYear()}`;
      case 'THIS_MONTH':
        return `del mes de ${MonthsOfYear[hoy.getMonth()].charAt(0).toUpperCase()}${MonthsOfYear[hoy.getMonth()].slice(1)}
        ${hoy.getUTCFullYear()}`;
      case 'THIS_YEAR':
        return `del ${hoy.getFullYear()}`;
      case 'RANGE':
        return `del ${startDtae} al ${endDte}`;
      default:
        break;
    }
    return '';
  };

  return (
    <>
      <div className="body-adminCard">
        <div
          className={`${classNameBody} py-4 px-4 md:flex md:flex-col ${filterType ? '2xl:flex-col' : ' 2xl:flex-row'
            }`}
        >
          <div>
            <div className="flex items-center">
              <GoogleIcon className="text-[44px]" icon={icon} />
              {loading ? (
                <Skeleton.Input
                  className="ml-2"
                  active={true}
                  size={'large'}
                  block={true}
                />
              ) : (
                <p className=" ml-2 text-[25px] font-bold">{body}</p>
              )}
            </div>
            {renderTitle && (
              <p className="ml-[52px] text-[#797979]">{`${title} ${labelDate ? getLabel() : ''
                }`}</p>
            )}
          </div>
          <div
            className={`${filterType ? 'mt-2' : 'mt-0'
              } flex md:flex-col 2xl:flex-row`}
          >
            {filterType && (
              <div
                className={`mt-2 mr-4 md:w-full 2xl:${typeSearch === 'mostSold' ? 'w-full' : 'w-1/2'
                  }`}
              >
                <FilterOrders
                  className="mt-0"
                  options={sellAdmin}
                  labelStr="Tipo"
                  onChange={e => {
                    if (onChangeType) {
                      onChangeType(e);
                    }
                    if (onChange) {
                      onChange('THIS_MONTH', '', '');
                    }
                    setFilterSale('THIS_MONTH');
                    setRenderDate(false);
                    setTypeSearch(e);
                  }}
                  optionSelect={false}
                  value={typeSearch}
                />
              </div>
            )}
            {filter && typeSearch === 'mostSold' && (
              <div className="mt-2 w-full">
                <FilterOrders
                  className="mt-0"
                  options={RangeDate}
                  labelStr="Por Fecha:"
                  onChange={e => {
                    if (e === 'RANGE') {
                      setRenderDate(true);
                    } else {
                      setEndDte('');
                      setStartDtae('');
                      setRenderDate(false);
                      if (onChange) {
                        onChange(e, '', '');
                      }
                    }
                    setFilterSale(e);
                  }}
                  defaultValue={'AlWAYS'}
                  labelDefault={'Siempre'}
                  // value={rangeDate ? 'RANGE' : filters.date}
                  value={filterSale}
                />
                <div className="mt-2 flex w-52 flex-col md:w-full">
                  {renderDate && (
                    <RangePicker
                      // disabled={!rangeDate}
                      allowClear={false}
                      onChange={onDateChange}
                      placeholder={['Fecha inicio', 'Fecha fin']}
                    // value={[moment(), moment()]}
                    />
                  )}
                </div>
              </div>
            )}
          </div>
        </div>
        {childrenBody && <div className="p-4">{childrenBody}</div>}
      </div>
      {/* <div className={`footer-adminCard h-12 bg-[${colorFooter}]`}></div> */}
      <div
        className={`footer-adminCard  flex h-12 items-center justify-end`}
        style={{ backgroundColor: colorFooter }}
      >
        {footerLink && (
          <Link href={url || ''}>
            <a className="mr-4 flex font-semibold text-[#006EB2]">
              Ver más <img className="ml-2" src={Arrow.src} />
            </a>
          </Link>
        )}
      </div>
    </>
  );
};
