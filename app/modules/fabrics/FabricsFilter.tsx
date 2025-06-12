import { CaretDownOutlined } from '@ant-design/icons';
import { Button, Checkbox, Collapse, Form, List, Skeleton } from 'antd';
import { useRouter } from 'next/router';
import { FC, ReactNode, useEffect, useState } from 'react';
import collection from '../../../pages/admin/collection';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { Catalog } from '../../interfaces/Utils/IUtils';
// import { mockArr } from '../../models';
import {
  FabricsActionsCollectionFilter,
  FabricsActionsFibersFilter,
  FabricsActionsReset,
  FabricsActionsSalesFilter,
  FabricsActionsUsesFilter
} from '../../services/redux/actions/FabricsActions';
import { CtxDispatch, CtxSelector } from '../../services/redux/store';
import { InfiniteSImpFilters } from '../shared/InfiniteScroll/InfiniteSImpFilters';
import styles from './FabricsFilter.module.css';

const { Panel } = Collapse;

interface CtxProps {
  children: ReactNode | ReactNode[];
  activeKey?: string;
}

const renderLoadingList = (
  <div className="grid grid-cols-1 gap-4 px-4 pt-5">
    <Skeleton.Input className="w-full" />
    <Skeleton.Input className="w-full" />
    <Skeleton.Input className="w-full" />
  </div>
);
const CtxPanelChild: FC<CtxProps> = ({ activeKey, children }) => {
  return (
    <Collapse
      accordion
      expandIconPosition="end"
      className=""
      defaultActiveKey={activeKey}
    >
      {children}
    </Collapse>
  );
};

interface FIProps {
  parent: 'uses' | 'fibers' | 'sales';
  name: string;
  id: string;
}
const CtxFormItem: FC<FIProps> = ({ name, id, parent }) => {
  return (
    <Form.Item
      name={[parent, id]}
      className={styles.formItem}
      valuePropName="checked"
    >
      <Checkbox value={id}>{name}</Checkbox>
    </Form.Item>
  );
};

interface ObjDynamic {
  [key: string]: boolean;
}
interface FilterForm {
  uses: ObjDynamic;
  fibers: ObjDynamic;
  sales: ObjDynamic;
}

interface FabricsFilterProps {
  imageSrc?: string;
}

export const FabricsFilter: FC<FabricsFilterProps> = ({ imageSrc }) => {
  const [load, setLoad] = useState<boolean>(true);
  const router = useRouter();
  const { query, isReady } = router;

  useEffect(() => {
    if (!isReady || !query['collections'] || query['collections'] === '') {
      setShowErase(false);
      return;
    }
    setShowErase(true);
  }, [isReady, query]);

  useEffect(() => {
    if (imageSrc !== '' || !router.query['collections']) {
      setLoad(false);
    }
  }, [imageSrc, router]);

  const dispatch = CtxDispatch();
  const filtersRX = CtxSelector(s => s.fabrics);

  const [form] = Form.useForm();
  // const [usesFilter, setUsesFilter] = useState<PaginationResponse<Catalog[]>>();
  const [fibersFilter, setFibersFilter] =
    useState<PaginationResponse<Catalog[]>>();
  const [salesFilter, setSalesFilter] =
    useState<PaginationResponse<Catalog[]>>();
  const [collectionFilter, setCollectionFilter] =
    useState<PaginationResponse<Catalog[]>>();
  const [showErase, setShowErase] = useState<boolean>(false);
  const collectionSelected = CtxSelector(
    state => state.fabrics?.filters.collections
  );

  const mapFilter = (
    key: 'uses' | 'fibers' | 'sales',
    formval: FilterForm
  ): string => {
    if (!formval[key]) return '';
    const propNames = Object.keys(formval[key]);
    const trueValues = propNames.filter(i => formval[key][i] === true);
    return trueValues.join(',');
  };

  const resetForm = () => {
    form.resetFields();
    dispatch(FabricsActionsReset());
    // buildFilters({ uses: {}, fibers: {} });
    setShowErase(false);
    if (query['collections']) {
      router.push('/fabrics');
    }
  };

  const buildFilters = (formVal: FilterForm): void => {
    const usesM = mapFilter('uses', formVal) || '';
    const fibers = mapFilter('fibers', formVal) || '';
    const sales = mapFilter('sales', formVal) || '';
    dispatch(FabricsActionsUsesFilter(usesM));
    dispatch(FabricsActionsFibersFilter(fibers));
    dispatch(FabricsActionsSalesFilter(sales));
    if (usesM === '' && fibers === '' && sales === '') {
      resetForm();
      return;
    }
    setShowErase(true);
  };

  useEffect(() => {
    return () => {
      dispatch(FabricsActionsReset());
    };
  }, []);

  return (
    <Form
      form={form}
      onFinish={val => buildFilters(val)}
      onChange={() => form.submit()}
      className="flex flex-col gap-[3rem]"
    >
      {load && <Skeleton.Image active className={'min-h-[20rem] w-auto'} />}
      {imageSrc !== '' && <img src={imageSrc} width={350} height={350} />}
      <Collapse
        accordion
        expandIconPosition="end"
        className="rounded-lg border-none bg-transparent shadow-sm shadow-slate-400"
        defaultActiveKey={'filter_products'}
        expandIcon={() => <CaretDownOutlined />}
      >
        <Panel
          className="parent-panel"
          header="FILTRAR PRODUCTOS"
          key={'filter_products'}
        >
          {showErase && (
            <Button
              className="
                  w-full
                  border-none
                  bg-transparent
                  text-center
                  text-main
                "
              onClick={resetForm}
            >
              <span className="mr-1 font-famBold">x</span> Borrar filtros
            </Button>
          )}
          {/* <CtxPanelChild key="usosPanel" activeKey="usos">
            <Panel header={'Usos'} key={'usos'}>
              {!usesFilter && mockArr(4).map(() => renderLoadingList)}
              <div className="pl-3">
                <InfiniteSImpFilters
                  listchange={pagList => {
                    setUsesFilter({
                      pagination: pagList.pagination,
                      content: pagList.content as Catalog[]
                    });
                  }}
                  serviceUrl={'use?size=1000&columnSort=name&typeSort=ASC'}
                  itemName="Usos"
                  height={375}
                >
                  {!usesFilter && renderLoadingList}
                  {usesFilter?.content.map(({ id, name }) => (
                    <List key={id}>
                      <List.Item className="py-[4px]">
                        <CtxFormItem parent="uses" name={name} id={id} />
                      </List.Item>
                    </List>
                  ))}
                </InfiniteSImpFilters>
              </div>
            </Panel>
          </CtxPanelChild> */}
          <CtxPanelChild key="fiberPanel">
            <Panel header="Fibras" key="fibras">
              {!fibersFilter && renderLoadingList}
              <div className="pl-3">
                <InfiniteSImpFilters
                  serviceUrl="fiber?size=1000&columnSort=name&typeSort=ASC"
                  listchange={l =>
                    setFibersFilter({
                      content: l.content as Catalog[],
                      pagination: l.pagination
                    })
                  }
                  itemName="fibras"
                >
                  {fibersFilter?.content.map(i => (
                    <List key={i.id}>
                      <div className="py-2">
                        <CtxFormItem parent="fibers" name={i.name} id={i.id} />
                      </div>
                    </List>
                  ))}
                </InfiniteSImpFilters>
              </div>
            </Panel>
          </CtxPanelChild>
          <CtxPanelChild key="salePanel">
            <Panel header="Venta" key="sales">
              {!salesFilter && renderLoadingList}
              <div className="pl-3">
                <InfiniteSImpFilters
                  serviceUrl="sale?size=1000&columnSort=name&typeSort=ASC"
                  listchange={l =>
                    setSalesFilter({
                      content: l.content as Catalog[],
                      pagination: l.pagination
                    })
                  }
                  itemName="ventas"
                >
                  {salesFilter?.content.map(i => (
                    <List key={i.id}>
                      <List.Item>
                        <CtxFormItem parent="sales" name={i.name} id={i.id} />
                      </List.Item>
                    </List>
                  ))}
                </InfiniteSImpFilters>
              </div>
            </Panel>
          </CtxPanelChild>
        </Panel>
      </Collapse>
      <Collapse
        accordion
        defaultActiveKey={'collections'}
        className="child-panel rounded-lg border-none bg-transparent p-0 shadow-sm shadow-slate-400"
        expandIconPosition="end"
        expandIcon={() => <CaretDownOutlined />}
      >
        <Panel header="COLECCIONES" className="p-0" key={'collections'}>
          {!collection && renderLoadingList}
          <InfiniteSImpFilters
            serviceUrl="collection?size=1000&columnSort=name&typeSort=ASC"
            itemName="colecciones"
            listchange={l =>
              setCollectionFilter({
                pagination: l.pagination,
                content: l.content as Catalog[]
              })
            }
            height={400}
          >
            {collectionFilter?.content.map(i => (
              <List key={i.id}>
                <List.Item
                  onClick={() => {
                    const exists = i.id === collectionSelected ? '' : i.id;

                    dispatch(FabricsActionsCollectionFilter(exists));
                    if (exists) {
                      router.push(`fabrics?collections=${i.id}`);
                    } else {
                      router.push('fabrics');
                    }
                  }}
                  className={`cursor-pointer pl-3 hover:bg-gray-100 ${
                    filtersRX?.filters.collections === i.id
                      ? 'bold bg-gray-200 text-main'
                      : ''
                  }`}
                >
                  {i.name}
                </List.Item>
              </List>
            ))}
          </InfiniteSImpFilters>
        </Panel>
      </Collapse>
    </Form>
  );
};
