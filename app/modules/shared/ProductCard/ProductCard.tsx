import { Modal, Skeleton } from 'antd';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { FC, useState } from 'react';
import { Cloth } from '../../../interfaces/Response/Cloth/Cloth';
import { Collection } from '../../../interfaces/Response/Collections/CollectionResponses';
import { SetCollectionSelectState } from '../../../services/redux/actions/CollectionSelectActions';
import { addProductDetail } from '../../../services/redux/actions/ProductDetailActions';
import { CtxDispatch } from '../../../services/redux/store';
import { ModalProduct } from '../../fabrics';
import { PriceLabel } from '../PriceLabel';
import style from './ProductCard.module.css';

interface Props {
  product: Collection;
  showPrice?: boolean;
}
export const ProductCard: FC<Props> = ({ product, showPrice }) => {
  const dispatch = CtxDispatch();
  const router = useRouter();
  const [showModal, setShowModal] = useState<boolean>(false);
  const [load, setLoad] = useState<boolean>(true);
  const formatedUrl = '/' + product.nameUrl;

  return (
    <div
      className={
        style.product_card + ' mx-auto  p-3 shadow-md shadow-slate-300'
      }
      onClick={() => {
        if (router.pathname.includes('/applications'))
          dispatch(SetCollectionSelectState(product));
      }}
    >
      <div className={'relative min-h-[10rem] '}>
        {product?.variants !== undefined && (
          <div
            className="
              absolute z-10  grid
              h-full
              w-full
              cursor-pointer
              place-content-center
              opacity-100 transition-all
              duration-500
              hover:opacity-100
              md:opacity-0
              "
            onClick={e => {
              dispatch(addProductDetail(product as Cloth));
              if (e.target !== e.currentTarget) return;
              const link = `/products/${product.nameUrl}`;
              router.push(link);
            }}
          >
            <a
              onClick={() => setShowModal(!showModal)}
              className="z-50 hidden rounded-3xl bg-main px-3 py-3 text-white sm:block"
            >
              Vista previa
            </a>
          </div>
        )}
        {formatedUrl && (
          <Link
            className="max-w-full overflow-auto"
            href={
              showPrice ? formatedUrl : `/fabrics?collections=${product.id}`
            }
          >
            <a className={'grid max-h-[14rem] max-w-full overflow-hidden'}>
              {load && (
                <Skeleton.Image active className={style.ant_skeleton_image} />
              )}
              <img
                onLoad={() => setLoad(false)}
                height={300}
                width={300}
                className={
                  'mx-auto  aspect-square w-full max-w-full transition-all duration-500 hover:scale-150 ' +
                  style.child
                }
                src={product.image}
              />
            </a>
          </Link>
        )}
      </div>
      <Link
        className="cursor-pointer"
        href={
          showPrice
            ? `/products${formatedUrl}`
            : `/fabrics?collections=${product.id}`
        }
      >
        <h4
          className="
          text-uppercase
          max-w-[150px]
          overflow-hidden
          text-ellipsis
          whitespace-nowrap
          text-lg
          font-bold
          text-main
          hover:max-w-none
          hover:cursor-pointer
          hover:whitespace-normal
          md:max-w-[180px]
        "
        >
          {product.name || 'hola'}
        </h4>
      </Link>
      {showPrice && (
        <div className="mt-auto w-full text-left font-extrabold text-black">
          {product.prices ? (
            <PriceLabel
              price={product.prices[0].price}
              className="font-famBold "
            />
          ) : (
            <p className="text-red-700">
              No pudimos obtener el precio, intente de nuevo
            </p>
          )}
        </div>
      )}

      <Modal
        visible={showModal}
        footer={null}
        onCancel={() => setShowModal(false)}
        width={767}
      >
        <ModalProduct productNameUrl={product.nameUrl} />
      </Modal>
    </div>
  );
};
