import React, { FC, useState } from 'react';
import { ColorPicker } from '../ProductDetail';
import { ActionModalDetail } from '../ProductDetail/ActionModalDetail';
import { Collection } from '../../interfaces/Response/Collections/CollectionResponses';
import { CtxDispatch, CtxSelector } from '../../services/redux/store';
import {
  CartActions,
  incrementQuantityCart
} from '../../services/redux/actions/CartActions';
import { useRouter } from 'next/router';
import { setShoppingCart } from '../../services/utils';
import AvailableItems from '../shared/AvailableItems/AvailableItems';

interface Props {
  product: Collection;
}

export const DetailModal: FC<Props> = ({ product }) => {
  const { products } = CtxSelector((state: any) => state.cart);
  const navigate = useRouter();
  const dispatch = CtxDispatch();

  const {
    name,
    nameUrl,
    descriptions,
    mainDescription,
    image,
    prices = [],
    variants = [],
    sale
  } = product;
  const [addCart, setAddCart] = useState({
    quantity: 1,
    variant: product.variants[0].id,
    nameColor: '',
    nameTela: '',
    img: '',
    nameUrl: '',
    abbreviation: ''
  });

  const onSubmitToCart = (buy?: boolean) => {
    let addvariant = true;
    if (buy) {
      navigate.push('/payment');
    } else {
      navigate.push('/cart');
    }
    products.map((pro: any) => {
      if (pro.variant === addCart.variant) {
        dispatch(incrementQuantityCart(addCart));
        addvariant = false;
      }
    });
    if (addvariant) {
      dispatch(CartActions(addCart));
    }

    setShoppingCart();
  };

  const selectColor = (variant: string, _: string, nameColor: string) => {
    setAddCart({
      ...addCart,
      variant,
      nameColor,
      nameTela: name,
      img: image,
      nameUrl: nameUrl || '',
      abbreviation: sale.abbreviation
    });
  };

  return (
    <>
      <h4>{name}</h4>

      <p className={`detail-price `}>{`$ ${prices[0]?.price}.00` || ''}</p>

      <div className="ml-4 w-full" style={{ color: '#777' }}>
        <ul className="list-disc">
          {descriptions?.map((desc, i) => (
            <li key={desc.name + i.toString()}>{desc.name}</li>
          ))}
        </ul>
        <span>{mainDescription}</span>
      </div>

      <div className="mt-5 pl-4 pr-4">
        <h2 className="title-colorpicker">
          {variants.length > 1 ? 'Colores' : 'Color'}
        </h2>

        <div className={`detail-price '}`}>
          <ColorPicker variants={variants} selectColor={selectColor} />
        </div>
      </div>

      {/* product.variants && (
        <div>
          <AvailableItems
            clothUrlName={product.nameUrl}
            variantId={addCart.variant}
          />
        </div>
      ) */}

      <div>
        <ActionModalDetail name={nameUrl || ''} onSubmitCart={onSubmitToCart} />
      </div>
    </>
  );
};
