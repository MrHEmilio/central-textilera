import { Breadcrumb } from 'antd';
import { GetStaticProps, NextPage } from 'next';
import Link from 'next/link';
import { useEffect, useState } from 'react';
import { PaginationResponse } from '../../app/interfaces/paginationResponse';
import { Collection } from '../../app/interfaces/Response/Collections/CollectionResponses';
import { MainLayout } from '../../app/modules/shared';
import { InfiniteSImpFilters } from '../../app/modules/shared/InfiniteScroll/InfiniteSImpFilters';
import { ProductCard } from '../../app/modules/shared/ProductCard/ProductCard';
import { getCollections } from '../../app/services';

interface Props {
  collections: PaginationResponse<Collection[]>;
}

const ApplicationsPage: NextPage<Props> = ({ collections }) => {
  const [collectionHandler, setCollectionHandler] =
    useState<PaginationResponse<Collection[]>>();

  useEffect(() => {
    setCollectionHandler(collections);
  }, []);

  return (
    <MainLayout
      title={'Usos'}
      pageDescription={'Telas por sus diferentes usos'}
    >
      <div className="color-main container mx-auto mb-[3rem]">
        <div className="mt-8">
          <Breadcrumb>
            <Breadcrumb.Item>
              <Link href="/">Inicio</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>Usos</Breadcrumb.Item>
          </Breadcrumb>
        </div>
        <h1 className="color-main pb-5 pt-4 text-4xl">Usos</h1>
        {collectionHandler && (
          <InfiniteSImpFilters
            itemName={'Usos'}
            listchange={l =>
              setCollectionHandler({
                pagination: l.pagination,
                content: l.content as Collection[]
              })
            }
            serviceUrl="collection?columnSort=name&typeSort=ASC"
            gridResponsive
          >
            {collectionHandler.content.map(i => (
              <ProductCard product={i} key={i.id} />
            ))}
          </InfiniteSImpFilters>
        )}
      </div>
    </MainLayout>
  );
};

export const getStaticProps: GetStaticProps = async () => {
  const collections = await getCollections();
  return {
    props: {
      collections
    },
    revalidate: 86400
  };
};
export default ApplicationsPage;
