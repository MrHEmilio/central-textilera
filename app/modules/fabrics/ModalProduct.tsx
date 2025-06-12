import { FC } from 'react';
import { DetailModal } from './DetailModal';
import { Collection } from '../../interfaces/Response/Collections/CollectionResponses';
import { useGetRequest } from '../../Hooks/useGetRequest';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import { Skeleton } from 'antd';

interface Props {
  productNameUrl: string;
}
export const ModalProduct: FC<Props> = ({ productNameUrl }) => {
  const [response, isLoading] = useGetRequest<PaginationResponse<Collection[]>>(
    `cloth?page=1&size=1&searchUrl=${productNameUrl}` +
      `&responseStructure=PRICES,VARIANTS,SALE`
  );
  return isLoading || !response ? (
    <Skeleton.Image active className=" h-[30rem] w-full rounded-xl" />
  ) : (
    <div className="container-modal">
      <div className="container-img-detail-Modal">
        <img
          src={response.content[0].image}
          alt="Tela"
          className="img-detail-Modal"
        />
      </div>
      <div className="container-detail-Modal">
        <DetailModal product={response.content[0]} />
      </div>
    </div>
  );
};
