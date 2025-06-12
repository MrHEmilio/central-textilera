import { useRouter } from 'next/router';
import { FC, ReactNode, useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { PaginationResponse } from '../../../interfaces/paginationResponse';
import { dynamicPaginationService } from '../../../services/cloth';
import { FabricsActionLoading } from '../../../services/redux/actions/FabricsActions';
import { CtxDispatch } from '../../../services/redux/store';

interface Props {
  // eslint-disable-next-line no-unused-vars
  listchange: (paginationList: PaginationResponse<unknown[]>) => void;
  serviceUrl: string;
  itemName: string;
  children: ReactNode | ReactNode[];
  loading?: boolean;
  gridResponsive?: boolean;
  height?: number;
  lengthResponse?: number;
  errorFisrtResponse?: () => void;
}

/**
 * @description Implemetation for InfiniteScroll
 * @param serviceUrl, afet domain ex => /users/list
 * @param itemName name to show on loader
 * @param listchange returns instance of paginationResponse<unknown>
 * so need to be mapped if you're using interface
 * @param children needs to be a list of items to work
 * @param height optional for specific height
 * @param lengthResponse optional items for lenth response
 * @param errorFisrtResponse? optional for error in the first call api
 * @author Abel
 */
export const InfiniteSImpFilters: FC<Props> = ({
  listchange,
  itemName,
  children,
  gridResponsive,
  height,
  serviceUrl,
  loading,
  errorFisrtResponse,
  lengthResponse = 50
}) => {
  const dispatch = CtxDispatch();

  const { isReady } = useRouter();
  const formatUrl = serviceUrl.includes('?') ? serviceUrl : serviceUrl + '?';
  const [finalUrl, setFinalUrl] = useState<string>(
    formatUrl + `&size=${lengthResponse}`
  );
  const [paginationL, setPaginationL] =
    useState<PaginationResponse<unknown[]>>();

  const handleNext = () => {
    if (!paginationL) return;
    dynamicPaginationService(
      `${finalUrl}&page=${++paginationL.pagination.page}`
    ).then(r => {
      if (!r) return;
      setPaginationL({
        pagination: r.pagination,
        content: paginationL.content.concat(r.content)
      });
    });
  };

  useEffect(() => {
    if (loading) {
      dispatch(FabricsActionLoading(true));
    }

    dynamicPaginationService(`${finalUrl}&page=${1}`).then(r => {
      if (!r) {
        if (errorFisrtResponse) {
          errorFisrtResponse();
        }
        return;
      }
      setPaginationL(r);
    });
  }, [finalUrl]);

  useEffect(() => {
    // if (isCalling) return;
    if (!isReady) return;
    const formatUrl = serviceUrl.includes('?') ? serviceUrl : serviceUrl + '?';
    setFinalUrl(formatUrl + `&size=${lengthResponse}`);
  }, [serviceUrl]);

  useEffect(() => {
    if (!paginationL) return;
    listchange(paginationL);
  }, [paginationL]);

  const renderLoader = (
    <div className="col-span-full mt-4 w-full text-center text-main">
      Obteniendo más {itemName}...
    </div>
  );

  return (
    <div>
      {paginationL && (
        <InfiniteScroll
          loader={renderLoader}
          hasMore={
            !(paginationL.content.length >= paginationL.pagination.totalRecords)
          }
          next={handleNext}
          className={gridResponsive ? 'grid_responsive' : ''}
          dataLength={paginationL.content.length || 1}
          height={height}
        >
          {children}
        </InfiniteScroll>
      )}
    </div>
  );
};
