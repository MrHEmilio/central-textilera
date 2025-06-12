import { Breadcrumb, Button, Select, Skeleton } from 'antd';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { PaginationResponse } from '../../app/interfaces/paginationResponse';
import { Collection } from '../../app/interfaces/Response/Collections/CollectionResponses';
import { IFabricsReducer } from '../../app/interfaces/State/FabricsReducer';
import { OrderByEnum } from '../../app/models';
import { FabricsFilter } from '../../app/modules/fabrics';
import { MainLayout, ProductCard } from '../../app/modules/shared';
import { dynamicPaginationService } from '../../app/services/cloth';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../app/services/redux/actions';
import { ClearCollectionSelectState } from '../../app/services/redux/actions/CollectionSelectActions';
import {
  FabricsActionsCollectionFilter,
  FabricsActionsColumSort,
  FabricsActionsUpdateOrder
} from '../../app/services/redux/actions/FabricsActions';
import { CtxDispatch, CtxSelector } from '../../app/services/redux/store';
import { useReady } from '../../app/Hooks/useReady';

const Fabrics: FC = () => {
  const ready = useReady();
  const { Option } = Select;

  const [queryCloth, setQueryCloth] = useState<{
    typeSort: OrderByEnum.asc;
    columnSort: string;
    uses: string;
    fibers: string;
    sales: string;
    collections: string;
  }>();

  const [page, setPage] = useState(1);
  const [moreLoading, setMoreLoading] = useState(false);
  const [lastQuery, setLastQuery] = useState('');

  const dispatch = CtxDispatch();
  const fabricsState: IFabricsReducer | undefined = CtxSelector(
    state => state.fabrics
  );
  const collectionState = CtxSelector(state => state.collectionSelect);
  const mockItems = Array.from(Array(20).keys());
  const router = useRouter();
  const { query, isReady } = router;
  const [fabrics, setFabrics] = useState<PaginationResponse<Collection[]>>({
    content: [],
    pagination: { page: 1, size: 0, totalPage: 0, totalRecords: 0 }
  });
  const [show, setShow] = useState(false);
  const [loading, setLoading] = useState(false);

  const [title, setTitle] = useState({
    name: 'Telas',
    image: '',
    id: ''
  });

  const handleChangeOrderBy = (newVal: string) => {
    switch (newVal) {
      case 'DESC':
        dispatch(FabricsActionsColumSort('name'));
        dispatch(FabricsActionsUpdateOrder(OrderByEnum.desc));
        break;
      case 'ASC':
        dispatch(FabricsActionsColumSort('name'));
        dispatch(FabricsActionsUpdateOrder(OrderByEnum.asc));
        break;
      case 'DescPrice':
        dispatch(FabricsActionsColumSort('price'));
        dispatch(FabricsActionsUpdateOrder(OrderByEnum.desc));
        break;
      case 'AscPrice':
        dispatch(FabricsActionsColumSort('price'));
        dispatch(FabricsActionsUpdateOrder(OrderByEnum.asc));
        break;
      default:
        break;
    }
  };

  useEffect(() => {
    return () => {
      setQueryCloth({
        typeSort: OrderByEnum.asc,
        columnSort: 'name',
        uses: '',
        fibers: '',
        sales: '',
        collections: ''
      });
    };
  }, []);

  useEffect(() => {
    if (!router.query['collections']) {
      callService(false, true);
    } else {
      setQueryCloth({
        typeSort: OrderByEnum.asc,
        columnSort: 'name',
        uses: '',
        fibers: '',
        sales: '',
        collections: router.query['collections'].toString()
      });
    }
  }, [router]);

  useEffect(() => {
    if (fabrics && fabrics.content.length > 0) {
      dispatch(ClearCollectionSelectState());
    }
    if (router.query['name']) {
      setTitle({ ...title, name: router.query['name'].toString() });
    }
    if (router.query['collections']) {
      /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
      fabrics?.content.map((con: any) => {
        /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
        const { collections } = con as { collections: any[] };
        const res = collections.find(
          ({ id }) => id === router.query['collections']
        );
        if (res) {
          setTitle({ ...res });
        }
      });
    } else {
      setTitle({
        name: 'Telas',
        image: '',
        id: ''
      });
    }
  }, [router, fabrics]);

  useEffect(() => {
    if (isReady) {
      if (query['collections']) {
        const id = query['collections'].toString();
        dispatch(FabricsActionsCollectionFilter(id));
        setShow(true);
        return;
      } else {
        setQueryCloth({
          typeSort: OrderByEnum.asc,
          columnSort: 'name',
          uses: '',
          fibers: '',
          sales: '',
          collections: ''
        });
        setShow(true);
      }
    }
  }, [isReady]);

  useEffect(() => {
    if (!ready) return;

    const timer: NodeJS.Timeout = setTimeout(() => {
      setLoading(true);
      callService();
    })

    return () =>{
      clearTimeout(timer);
    };

  }, [ready, queryCloth]);

  useEffect(() => {
    if (!fabricsState) return;

    if (lastQuery === JSON.stringify(fabricsState.filters)) return;
    setLastQuery(JSON.stringify(fabricsState.filters));
    const { sales, collections, uses, orderBy, fibers, columnSort } =
      fabricsState.filters;

    setQueryCloth({
      collections,

      /*eslint-disable-next-line @typescript-eslint/no-explicit-any */
      typeSort: orderBy as any,
      fibers,
      uses,
      columnSort,
      sales
    });
    setPage(1);
  }, [fabricsState]);

  const callService = async (addPage?: boolean, reload?: boolean) => {
    if (!queryCloth) return;
    setMoreLoading(true);
    dispatch(LoaderActionsShow());
    let str =
      `cloth?typeSort=${queryCloth.typeSort}` +
      `&columnSort=${queryCloth.columnSort}` +
      `&uses=${queryCloth.uses}` +
      `&fibers=${queryCloth.fibers}` +
      `&sales=${queryCloth.sales}` +
      `&page=${addPage ? page + 1 : 1}` +
      `&size=${50}` +
      `&collections=${queryCloth.collections}` +
      `&responseStructure=PRICES,SAMPLER,COLLECTIONS`;
    if (reload) {
      str =
        `cloth?typeSort=${OrderByEnum.asc}` +
        `&columnSort=${'name'}` +
        `&uses=${''}` +
        `&fibers=${''}` +
        `&sales=${''}` +
        `&page=${addPage ? page + 1 : 1}` +
        `&size=${50}` +
        `&collections=${''}` +
        `&responseStructure=PRICES,SAMPLER,COLLECTIONS`;
    }

    if (moreLoading === true) return;
    const res = await dynamicPaginationService(str);

    dispatch(LoaderActionsHide());
    setLoading(false);
    setMoreLoading(false);
    if (res?.content) {
      const response = res as PaginationResponse<Collection[]>;
      if (addPage && fabrics) {
        setPage(page + 1);
        const current = fabrics.content;
        const newContent = current.concat(response.content);
        response.content = newContent;
        setFabrics(response);
        return;
      }
      setFabrics(response);
    }
  };

  return (
    <MainLayout title={'Telas'} pageDescription={'Catalogo de Telas'}>
      {show && fabricsState && (
        <div className="color-main container mx-auto mb-[3rem] grid">
          <div className="mt-8">
            <Breadcrumb>
              <Breadcrumb.Item>
                <Link href="/">Inicio</Link>
              </Breadcrumb.Item>
              <Breadcrumb.Item>{title.name ?? 'All'}</Breadcrumb.Item>
            </Breadcrumb>
          </div>
          <div className="grid w-full grid-cols-2 items-center pb-5 pt-4 ">
            <div className="mt-4">
              <h1 className="color-main flex self-center text-2xl sm:text-4xl">
                {collectionState?.name || title.name}
              </h1>
            </div>
            <div className="flex-col items-baseline justify-end sm:flex sm:flex-row">
              <label className="mr-4 font-bold text-black">Ordenar por</label>
              <Select
                onChange={handleChangeOrderBy}
                className="w-full md:w-[16.5rem] "
                defaultValue={OrderByEnum.asc}
              >
                <Option value={OrderByEnum.asc}>A-Z</Option>
                <Option value={OrderByEnum.desc}>Z-A</Option>
                <Option value={'DescPrice'}>
                  Precio: del más alto al más bajo
                </Option>
                <Option value={'AscPrice'}>
                  Precio: del más bajo al más alto
                </Option>
              </Select>
            </div>
          </div>
          <div className="fabrics_layout">
            <div className="hidden md:block">
              <FabricsFilter
                imageSrc={collectionState?.image || (title.image as string)}
              />
            </div>
            <div>
              {(loading || !fabrics) && (
                <div className="grid_responsive">
                  {mockItems.map(i => (
                    <Skeleton.Image
                      key={i}
                      active
                      className="mx-auto h-[18rem] w-[10rem] rounded-xl sm:w-[12rem]"
                    />
                  ))}
                </div>
              )}

              {fabrics.content.length > 0 ? (
                <>
                  <div className="grid_responsive">
                    {fabrics.content.map(id => (
                      <ProductCard key={id.id} product={id} showPrice />
                    ))}
                  </div>
                  <div className="flex w-full justify-center">
                    {fabrics.pagination.page !==
                      fabrics.pagination.totalPage && (
                        <Button
                          loading={moreLoading}
                          onClick={() => callService(true, false)}
                          htmlType="button"
                          className="button-ctx"
                        >
                          Mostrar más
                        </Button>
                      )}
                  </div>
                </>
              ) : (
                <>
                  <div>
                    <h1 className="color-main text-center text-2xl sm:text-2xl">
                      ¡Ups! por el momento no hay stock de esta tela
                    </h1>
                  </div>
                </>
              )}
            </div>
          </div>
        </div>
      )}
    </MainLayout>
  );
};

export default Fabrics;
