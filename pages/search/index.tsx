import { Breadcrumb, Button, Card, Col, Input, Row, Skeleton, Spin } from 'antd';
import Link from 'next/link';
import React, { ChangeEvent, useEffect, useState } from 'react';
import { Cloth } from '../../app/interfaces/Response/Cloth/Cloth';
import { MainLayout } from '../../app/modules/shared';

import { getClothByName } from '../../app/services/cloth/FeaturedProductService';
import { CardSearch } from '../../app/modules/shared/CardSearch';
import { CtxDispatch } from '../../app/services/redux/store';
import { Pagination } from '../../app/interfaces/paginationResponse';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../app/services/redux/actions';

import useDebounce from '../../app/Hooks/useDebouncer';
import { WarningOutlined } from '@ant-design/icons';

const index = () => {
  const dispatch = CtxDispatch();
  const [valueSearch, setValueSearch] = useState('');
  const [results, setResults] = useState<Cloth[]>([]);
  const [loading, setLoading] = useState(false);
  const debouncedValue = useDebounce<string>(valueSearch, 1000);
  const [pagination, setPagination] = useState<Pagination>({
    page: 1,
    size: 1,
    totalPage: 1,
    totalRecords: 1
  });
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    setLoading(true);
    setValueSearch(event.target.value);
  }

  const nextPage = async () => {
    dispatch(LoaderActionsShow());
    setPagination({ ...pagination, page: pagination.page + 1 });
    const response = await getClothByName(valueSearch, pagination.page + 1, 10);
    if (response) {
      setResults([...results, ...response.content]);
    }
    dispatch(LoaderActionsHide());
  };

  const handleSearch = async (term: string) => {

    setLoading(true);
    const response = await getClothByName(term, 1, 10);
    if (response) {
      setResults(response?.content || []);
      setResults(response.content);
      setPagination(response.pagination);
    }
    setLoading(false);
  };
  useEffect(() => {
    if (debouncedValue !== '') {
      handleSearch(debouncedValue);
    }
    else {
      setResults([])
      setLoading(false)
      return
    }
  }, [debouncedValue])
  return (
    <MainLayout title={'Search'} pageDescription={'Search Cloth'}>
      <div className="color-main container mx-auto" key={'Search'}>
        <div className="mt-8">
          <Breadcrumb>
            <Breadcrumb.Item>
              <Link href="/">Inicio</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>Buscar</Breadcrumb.Item>
          </Breadcrumb>
        </div>
        <h1 className="color-main pb-2 pt-4 text-4xl">Buscar</h1>
        <div>
          <Row>
            <Col xs={{ span: 24 }} md={{ span: 24 }} className="mt-2 flex justify-center sm:justify-center">
              <Input
                placeholder="Buscar"
                onChange={handleChange}
                value={valueSearch}
                className={`custom-input custom-searchinput mr-4 mt-6`}
              />
            </Col>
            <Col span={12} xs={{ span: 24 }} md={{ span: 24 }} className="mt-2">
              <Card key={valueSearch} style={{ borderRadius: '0.7rem', boxShadow: '0.5rem 0.7rem 0.5rem rgba(0, 0, 0, 0.2)', margin: '0.5rem 0.5rem 1.5rem 0.5rem'}}>
                {results.length > 0 ? (
                  <>
                    {results.map(clth => (
                      <div key={`${clth.id}${clth.name}`} className='flex flex-col items-center w-full justify-center' style={{
                        borderRadius: '1rem',
                        margin: '0.5rem 0.5rem 1.5rem 0.5rem'
                      }}>
                        <CardSearch key={clth.id} cloth={clth} />
                      </div>))}
                    {pagination.totalPage > 1 &&
                      pagination.page < pagination.totalPage && (
                        <div className="flex w-full justify-center">
                          <Button
                            onClick={nextPage}
                            htmlType="button"
                            className="button-ctx">
                            Mostrar más
                          </Button>
                        </div>
                      )}
                  </>
                ) : (
                  <>
                    {loading ? (
                      <div className='relative flex flex-col items-center w-full'>
                        <Skeleton.Image active style={{ width: '96px', height: '96px' }} />
                        <br />
                        <Skeleton paragraph={{ rows: 2 }} active style={{ width: '260px', height: '120px' }} />
                        <div className='absolute inset-0 flex items-center justify-center'>
                          <Spin size="large" spinning />
                        </div>
                      </div>
                    ) : (
                      <div className="p-20 text-center text-xl font-bold text-[#ccc]">
                        <WarningOutlined />
                        <p>Sin resultados.</p>
                      </div>
                    )}
                  </>
                )}
              </Card>
            </Col>
          </Row>
        </div>
      </div>
    </MainLayout >
  );
};
export default index;
