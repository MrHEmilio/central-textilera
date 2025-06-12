import { Skeleton } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import {
  deleteItemCart,
  deleteSamplerCart,
  setQuantityCart
} from '../../services/redux/actions/CartActions';
import { CtxDispatch, CtxSelector } from '../../services/redux/store';
import { ItemCounter } from '../shared/ItemCounter/ItemCounter';
import { formatNumber, setShoppingCart } from '../../services/utils';
import { itemCartFull } from '../../interfaces/State/Cart';
import { setTimeout } from 'timers';
import { DeleteOutlined } from '@ant-design/icons';
import { cleanProductDetail } from '../../services/redux/actions/ProductDetailActions';
import { useRouter } from 'next/router';
import noImage from '/public/img/noSampler1.jpg';
import { Variant } from '../../interfaces/Response/Cloth/Cloth';
import AvailableItems from '../shared/AvailableItems/AvailableItems';

interface Props {
  productItemCard: itemCartFull;
  // eslint-disable-next-line no-unused-vars
  onChangeQuantity: (arg0: string) => void;
  isSampler?: boolean;
}

export const BodyCart: FC<Props> = ({
  productItemCard,
  onChangeQuantity,
  isSampler
}) => {
  const {
    variant,
    quantity,
    img,
    nameTela,
    nameColor,
    totalSellPriceNormal,
    discount,
    totalSellPrice,
    nameUrl
  } = productItemCard;
  const navigate = useRouter();

  const [loadingBody, setLoadingBody] = useState(true);
  const dispatch = CtxDispatch();
  const [srcImage, setSrcImage] = useState(img);
  const [fullVariant, setfullVariant] = useState<Variant | undefined>();
  const { loading } = CtxSelector((state: any) => state.cart);

  const deleteItem = () => {
    if (isSampler) {
      dispatch(deleteSamplerCart({ variant, quantity }));
    } else {
      dispatch(deleteItemCart({ variant, quantity }));
    }
    setShoppingCart();
    onChangeQuantity(variant);
    // setShoppingCart();
  };

  const getPriceForRender = () => {
    if (!productItemCard) {
      return (
        <div>
          <h4 className="text-red-600">No se pudo cargar el precio</h4>
        </div>
      );
    }
    if (discount === 0) {
      return <h4>{`$${formatNumber(totalSellPriceNormal)}`}</h4>;
    } else {
      return (
        <div>
          <h4 className="text-[#006EB2] line-through ">{`$${formatNumber(
            totalSellPriceNormal
          )}`}</h4>
          <h4>{`$${formatNumber(totalSellPrice)}`}</h4>
        </div>
      );
    }
  };

  const getDescountRender = () => {
    if (!productItemCard) {
      return <div></div>;
    }
    if (discount === 0) {
      return <div></div>;
    } else {
      return (
        <div>
          <span className="hidden text-[#006EB2] sm:block">{`Usted ahorró $${formatNumber(
            discount
          )}`}</span>
        </div>
      );
    }
  };

  useEffect(() => {
    setTimeout(() => {
      setLoadingBody(false);
    }, 600);
  }, [discount]);

  const setQuantity = (q: number) => {
    onChangeQuantity(q.toString());
    if (!isSampler) {
      dispatch(setQuantityCart({ quantity: q, variant: variant }));
    }
    setShoppingCart();
  };

  const onProduct = () => {
    dispatch(cleanProductDetail());
    const dir = isSampler
      ? `/products/${nameUrl}/sampler`
      : `/products/${nameUrl}`;
    navigate.push(dir);
  };

  return (
    // <Row className="container-bodyCart">
    <div className="container-bodyCart grid grid-cols-2 flex-col pb-4 pt-4 sm:grid-cols-4">
      <div className="header-col flex justify-center">
        <img
          src={srcImage || noImage.src}
          alt=""
          onError={() => setSrcImage(noImage.src)}
        />
      </div>

      <div className="header-col flex items-center justify-center">
        <div>
          <div onClick={onProduct}>
            <h4 className="cursor-pointer text-main">
              {nameTela?.toUpperCase()}
            </h4>
          </div>
          <p>{nameColor}</p>
          <p
            className="cursor-pointer underline opacity-60 hover:text-main"
            onClick={deleteItem}
          >
            Eliminar
          </p>
        </div>
      </div>

      <div>
        <div className="quantity-cart flex items-center justify-center">
          <div className="flex-col">
            <ItemCounter
              quantity={Number(quantity)}
              currentQuantity={setQuantity}
            />
          </div>
        </div>
      </div>

      <div className=" quantity-cart flex flex-row items-center justify-center">
        <div className={loading ? 'hidden' : 'block'}>
          {loadingBody ? (
            <Skeleton.Input active={true} size="small" />
          ) : (
            getDescountRender()
          )}
        </div>
        <div className={loading ? 'block' : 'hidden'}>
          <Skeleton.Input active={true} size="small" />
        </div>
        <div className="header-col flex flex-col sm:flex-row">
          <div className={`${loading ? 'hidden' : 'block'} mr-4`}>
            {loadingBody ? (
              <Skeleton.Input active={true} size="small" />
            ) : (
              getPriceForRender()
            )}
          </div>
          <div>
            <DeleteOutlined
              className="deleteicon-directioncard"
              onClick={deleteItem}
            />
          </div>
        </div>
      </div>
    </div>
  );
};
