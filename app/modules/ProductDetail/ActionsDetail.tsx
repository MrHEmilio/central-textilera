import { Button, Skeleton } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { FacebookLogo, InstagramLogo, ItemCounter } from '../shared';
import { ColorPicker } from './ColorPicker';
import { Variant, Cloth } from '../../interfaces/Response/Cloth/Cloth';
import {
  CartActions,
  incrementQuantityCart
} from '../../services/redux/actions/CartActions';
import { CtxDispatch, CtxSelector } from '../../services/redux/store';
import { useRouter } from 'next/router';
import { formatNumber, setShoppingCart } from '../../services/utils';
import { useReady } from '../../Hooks/useReady';

interface Props {
  variants: Variant[];
  loading: boolean;
  cloth: Cloth;
  // eslint-disable-next-line no-unused-vars
  selectColor: (arg0: string) => void;
  // eslint-disable-next-line no-unused-vars
  quantityC: (arg0: number) => void;
}
export const ActionsDetail: FC<Props> = ({
  variants = [],
  quantityC,
  loading,
  selectColor,
  cloth
}) => {
  const ready = useReady();
  const navigate = useRouter();
  const { products } = CtxSelector((state: any) => state.cart);
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const { prices } = cloth;
  const dispatch = CtxDispatch();
  const [quantityClothState, setQuantityClothState] = useState(1);
  const [selectedVariant, setselectedVariant] = useState<Variant | undefined>(
    cloth.variants[0]
  );
  const [addCart, setAddCart] = useState({
    quantity: 1,
    variant: 'qwe',
    nameColor: '',
    nameTela: cloth.nameUrl,
    img: '',
    nameUrl: '',
    abbreviation: ''
  });
  const [variant, setVariant] = useState<Variant[]>([]);

  useEffect(() => {
    setVariant(variants);
  }, [loading]);

  const quantityCloth = (quantity: number) => {
    quantityC(quantity);
    setQuantityClothState(quantity);
    setAddCart({ ...addCart, quantity });
  };

  const variantColor = (
    color: string,
    codeColor: string,
    nameColor: string
  ) => {
    setAddCart({
      ...addCart,
      variant: color,
      nameColor: nameColor,
      img: cloth.image,
      nameTela: cloth.name,
      nameUrl: cloth.nameUrl || '',
      abbreviation: cloth.sale.abbreviation
    });
    selectColor(codeColor);
    // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
    setselectedVariant(cloth.variants.find(x => x.id === color)!);
  };

  const getPriceRender = () => {
    const unitPrice = prices.find(price => {
      if (price.lastAmountRange == undefined || price.lastAmountRange === 0)
        return price.price;
      if (
        price.firstAmountRange <= quantityClothState &&
        price.lastAmountRange >= quantityClothState
      ) {
        return price.price;
      }
    });
    if (!unitPrice) return 'Hubo un error al obtener el precio';
    return `$${formatNumber(quantityClothState * unitPrice.price)}`;
  };

  const sendProduct = () => {
    let addvariant = true;
    products.map((pro: any) => {
      if (pro.variant === addCart.variant) {
        dispatch(incrementQuantityCart(addCart));
        addvariant = false;
      }
    });
    if (addvariant) {
      dispatch(CartActions(addCart));
    }
    navigate.push('/cart');
    setShoppingCart();
  };

  if (!ready) return <div></div>;

  return (
    ready && (
      <div>
        <div style={{ marginTop: '1rem' }}>
          <h2 className="title-colorpicker">
            {variants.length > 1 ? 'Colores' : 'Color'}
          </h2>
          {loading && (
            <div>
              <Skeleton.Input active={true} block={true} />
              <Skeleton.Input active={true} block={true} />
            </div>
          )}
          {!loading && (
            <div
              className={`detail-price ${
                variants.length > 0 ? '' : 'text-red-700'
              }`}
            >
              {variants.length > 0 ? (
                <ColorPicker variants={variants} selectColor={variantColor} />
              ) : (
                'No se pudieron cargar los Colores'
              )}
            </div>
          )}
          {/* start available cloths */}
          {/* selectedVariant && (
          <AvailableItems
            clothUrlName={cloth.nameUrl}
            variantId={selectedVariant.id || cloth.variants[0].id}
          />
        ) */}
          {/* end available cloths */}
        </div>
        <div style={{ marginTop: '2rem' }}>
          <ItemCounter
            // maxAmount={selectedVariant?.amount}
            quantity={1}
            currentQuantity={quantityCloth}
          />
        </div>
        <div className="container-button-addcart ">
          <div className="ml-3">
            <p className="pt-3 text-xl font-bold">{getPriceRender()}</p>
          </div>
          {userInfo?.role !== 'ADMIN' && userInfo?.role !== 'ADMIN_ROOT' && (
            <Button
              type="primary"
              className="button-ctx"
              onClick={sendProduct}
              disabled={addCart.quantity === 0 || cloth.variants.length === 0}
            >
              Agregar al carrito
            </Button>
          )}
        </div>
        <div className="mt-4 flex justify-center px-4 sm:justify-end">
          <a href="https://www.facebook.com/centraltextilera/">
            {/* <img
            src={facebookLogo.src}
            alt=""
            className="h-10 w-10 hover:text-main"
          /> */}
            <div className=" h-10 w-10 sm:mr-6">
              <FacebookLogo />
            </div>
          </a>
          <a
            href="https://www.instagram.com/centraltextilera_mx/"
            className="ml-2 "
          >
            {/* <img src={instagramLogo.src} alt="" className="h-10 w-10 " /> */}
            <div className=" h-10 w-10 sm:mr-7">
              <InstagramLogo />
            </div>
          </a>
        </div>
      </div>
    )
  );
};
