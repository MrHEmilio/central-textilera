import { FC, ReactNode } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { PaginationResponse } from '../../../interfaces/paginationResponse';

interface Props {
  list: PaginationResponse<unknown[]>;
  itemName: string;
  handleNext: (
    pagination: PaginationResponse<unknown>,
    nextPage: number
  ) => void;
  children: ReactNode | ReactNode[];
  gridResponsive?: boolean;
  height?: number;
}

/**
 * @description Implemetation for InfiniteScroll
 * @param list an instance of PaginationResponse
 * @param itemName name to show on loader
 * @param handleNext returns current list content and next page
 * @example handleNext={(data, nextPage) => {
                      const { content } = data as { content: Catalog[] };
                      getClothUsesFilters(nextPage).then(r => {
                        if (!r) return;
                        r.content = content.concat(r.content);
                        setUsesFilter(r);
                      });
                    }}

 * @param children needs to be a list of items to work
 * @param height optional for specific height
 * @author Abel
 */
export const InfiniteSImp: FC<Props> = ({
  list,
  itemName,
  handleNext,
  children,
  gridResponsive,
  height
}) => {
  const renderLoader = (
    <div className="col-span-full mt-4 w-full text-center text-main">
      Obteniendo más {itemName}...
    </div>
  );
  return (
    <InfiniteScroll
      loader={renderLoader}
      hasMore={!(list.content.length >= list.pagination.totalRecords)}
      next={() => handleNext(list, ++list.pagination.page)}
      className={gridResponsive ? 'grid_responsive' : ''}
      dataLength={list.content.length || 1}
      height={height}
    >
      {children}
    </InfiniteScroll>
  );
};
