import { CaretDownOutlined } from '@ant-design/icons';
import { Collapse, Skeleton } from 'antd';
import React, {
  Dispatch,
  FC,
  SetStateAction,
  useEffect,
  useState
} from 'react';
import { Cloth } from '../../../interfaces/Response/Cloth/Cloth';
import { CardSearch } from '../CardSearch';
import { InfiniteSImpFilters } from '../InfiniteScroll/InfiniteSImpFilters';
import { PaginationResponse } from '../../../interfaces/paginationResponse';
import { Collection } from '../../../interfaces/Response/Collections/CollectionResponses';
import { CardSearchCollection } from '../CardSearchCollection/CardSearchCollection';

const { Panel } = Collapse;
interface Props {
  loading: boolean;
  endPointCloth: { value: string };
  setLoading: Dispatch<SetStateAction<boolean>>;
  modalSearch?: Dispatch<SetStateAction<boolean>>;
  renderAll?: boolean;
  onCLoseModalMoible?: () => void;
}
export const SearchContainer: FC<Props> = ({
  loading,
  endPointCloth,
  setLoading,
  modalSearch,
  onCLoseModalMoible,
  renderAll = true
}) => {
  const [clothsResponse, setClothsResponse] =
    useState<PaginationResponse<Cloth[]>>();
  const [collectionResponse, setCollectionResponse] =
    useState<PaginationResponse<Collection[]>>();
  const [valueSearch, setValueSearch] = useState('p');
  const [clothLoading, setClothLoading] = useState(true);
  const [collectionLoading, setCollectionLoading] = useState(true);

  const closeModal = () => {
    if (onCLoseModalMoible) {
      onCLoseModalMoible();
    }
    if (modalSearch) {
      modalSearch(false);
    }
  };

  useEffect(() => {
    if (endPointCloth.value) {
      setValueSearch(endPointCloth.value);
    }
  }, [endPointCloth]);

  useEffect(() => {
    if (loading) {
      setClothLoading(true);
      setCollectionLoading(true);
    }
  }, [loading]);

  const getRenderAll = () => {
    if (!renderAll) {
      return {
        activeKey: ['search_products', 'search_collection']
      };
    }
  };

  const getLoading = () => {
    if (loading || clothLoading || collectionLoading) {
      return true;
    }
    return false;
  };

  return (
    <>
      <div
        className="overflow-hidden"
        onBlur={e => {
          if (
            e.relatedTarget?.className === 'ant-input' ||
            e.relatedTarget?.className === 'ant-collapse-header'
          ) {
            return;
          }
          setTimeout(() => {
            if (modalSearch) {
              modalSearch(false);
            }
          }, 150);
        }}
      >
        {!(valueSearch === 'p') && (
          <div
            className={`${
              getLoading() ? 'hidden' : 'block'
            } max-h-[700px] overflow-y-scroll`}
          >
            <Collapse
              expandIconPosition="end"
              className="w-80 border-none bg-transparent shadow-sm shadow-slate-400 lg:w-[400px]"
              defaultActiveKey={'search_products'}
              expandIcon={() => <CaretDownOutlined />}
              {...getRenderAll()}
            >
              <Panel
                className="parent-panel  w-80 px-4 py-3 lg:w-[400px]"
                header={`${
                  clothsResponse?.pagination.totalRecords || 0
                } Productos`}
                key={'search_products'}
              >
                <InfiniteSImpFilters
                  listchange={pagList => {
                    setLoading(false);
                    setClothLoading(false);

                    setClothsResponse({
                      pagination: pagList.pagination,
                      content: pagList.content as Cloth[]
                    });
                  }}
                  serviceUrl={`cloth?search=${valueSearch}&responseStructure=PRICES`}
                  height={500}
                  itemName="Productos"
                  lengthResponse={10}
                >
                  {clothsResponse && clothsResponse?.content.length > 0 ? (
                    clothsResponse?.content.map((cloth, index) => (
                      <CardSearch
                        key={index}
                        cloth={cloth}
                        closeModal={closeModal}
                      />
                    ))
                  ) : (
                    <p
                      style={{ maxWidth: '400px', width: '100%' }}
                      className="py-4 text-center font-bold"
                    >
                      No se encontraron resultados
                    </p>
                  )}
                </InfiniteSImpFilters>
              </Panel>

              <Panel
                className="parent-panel  w-80 px-4 py-3 lg:w-[400px]"
                header={`${
                  collectionResponse?.pagination.totalRecords || 0
                } Colecciones`}
                key={'search_collection'}
              >
                <InfiniteSImpFilters
                  listchange={pagList => {
                    setLoading(false);
                    setCollectionLoading(false);

                    setCollectionResponse({
                      pagination: pagList.pagination,
                      content: pagList.content as Collection[]
                    });
                  }}
                  serviceUrl={`collection?search=${valueSearch}`}
                  height={500}
                  lengthResponse={10}
                  itemName="Colleciónes"
                >
                  {collectionResponse &&
                  collectionResponse?.content.length > 0 ? (
                    collectionResponse?.content.map((cloth, index) => (
                      <CardSearchCollection
                        key={index}
                        cloth={cloth}
                        closeModal={closeModal}
                      />
                    ))
                  ) : (
                    <p
                      style={{ maxWidth: '400px', width: '100%' }}
                      className="py-4 text-center font-bold"
                    >
                      No se encontraron resultados
                    </p>
                  )}
                </InfiniteSImpFilters>
              </Panel>
            </Collapse>
          </div>
        )}
        <div
          className={`${
            !loading && !clothLoading && !collectionLoading ? 'hidden' : 'block'
          } p-4`}
        >
          <Skeleton.Input active={true} block style={{ width: '400px' }} />
        </div>
      </div>
    </>
  );
};
