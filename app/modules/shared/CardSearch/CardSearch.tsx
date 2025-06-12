import { useRouter } from 'next/router';
import { FC } from 'react';
import { Cloth } from '../../../interfaces/Response/Cloth/Cloth';
import {
  addProductDetail,
  cleanProductDetail
} from '../../../services/redux/actions/ProductDetailActions';
import { CtxDispatch } from '../../../services/redux/store';

interface Props {
  cloth: Cloth;
  closeModal?: () => void;
}

export const CardSearch: FC<Props> = ({ cloth, closeModal }) => {
  const navigate = useRouter();
  const dispatch = CtxDispatch();
  // const prevRoute = () => {
  //   const pathname = navigate.pathname;
  //   return pathname.split('/').includes('products');
  // };

  const onClickSearch = () => {
    dispatch(cleanProductDetail());
    dispatch(addProductDetail(cloth));
    navigate.push(`/products/${cloth.nameUrl}`);
    if (closeModal) {
      closeModal();
    }
  };


  return (
    <div
      className=" cs-container mt-4 flex cursor-pointer flex-col"
      onClick={onClickSearch}
    >
      <div className="mb-3 flex flex-col items-center  xl:mb-0 xl:flex-row">
        <div className="cs-img mb-3 w-24">
          <img
            src={cloth.image}
            alt={cloth.mainDescription}
            style={{ borderRadius: '9px' }}
          />
        </div>
        <div className="cs-info ">
          <a>{cloth.name}</a>
          {/* </Link> */}
          <div className=" cs-description">
            {cloth.descriptions?.map((desc, i) => (
              <p key={desc.name + i.toString()}>{desc.name}</p>
            ))}
          </div>
        </div>
      </div>
      <div className=" cs-price">
        <p>{`$ ${cloth.prices[0].price}.00`}</p>
      </div>
    </div>
  );
};
