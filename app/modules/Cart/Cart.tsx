import { useEffect, useState } from 'react';
import useDebounce from '../../Hooks/useDebouncer';
import { useReady } from '../../Hooks/useReady';
import { Sampler } from '../../interfaces/Response/Cloth/Cloth';
import { itemCartFull } from '../../interfaces/State/Cart';
import {
  getPricesCart,
  GetPricesResponse,
  itemSampler,
  payloadCart
} from '../../services/Order/CalculatePrice';
import {
  changeSamplerQuantity,
  clearCart,
  deleteItemCart,
  deleteSamplerCart,
  loadingPrices,
  saveCartOnStorage,
  setPricesCart,
  setSamplersCart
} from '../../services/redux/actions/CartActions';
import {
  CtxDispatch,
  CtxSelector,
  ReduxStore
} from '../../services/redux/store';
import { CardInfo } from '../shared';
import { ActionsCart } from './ActionsCart';
import { BodyCart } from './BodyCart';
import { FooterCart } from './FooterCart';
import { HeaderCart } from './HeaderCart';

export const Cart = () => {
  const isReady = useReady();
  const [value, setValue] = useState<string>('');
  const debouncedValue = useDebounce<string>(value, 600);
  const [itemsCarts, setItemsCarts] = useState<itemCartFull[]>([]);
  const [pricesOk, setPricesOk] = useState<GetPricesResponse[]>([]);
  const [samplersS, setSamplers] = useState<itemCartFull[]>([]);
  const dispatch = CtxDispatch();
  const { products, samplers } = CtxSelector(state => state.cart!);

  const getPrice = async (payload: payloadCart) => {
    dispatch(loadingPrices(true));
    const response = await getPricesCart(payload);
    dispatch(loadingPrices(false));
    if (!response) return;
    if (response.length === 0) {
      dispatch(clearCart());
    }
    setPricesOk(response || []);
    if (response.length > 0) {
      dispatch(setPricesCart(response as any));
      dispatch(saveCartOnStorage());
    }
  };

  const changeQuantity = (q: string) => {
    setValue(q.toString());
  };

  const changeQuantitySam = (q: string, samplerId: string) => {
    // dispatch(loadingPrices(true));
    dispatch(changeSamplerQuantity({ id: samplerId, quantity: Number(q) }));
    const payload = products.map(({ variant, quantity }: itemCartFull) => ({
      variant,
      amount: quantity
    }));
    const samplersPayload = samplers.map((v: any) => ({
      sampler: v.id,
      amount: v.quantity
    }));
    getPrice({ cloths: payload, samplers: samplersPayload });
  };

  useEffect(() => {
    if (products) {
      setItemsCarts(products);
    }
  }, [products]);

  useEffect(() => {
    if (samplers.length > 0) {
      const item = samplers.map((i: Sampler) => ({
        quantity: i.quantity,
        sellPrice: i.price,
        variant: i.id,
        img: i.image,
        totalSellPriceNormal: i.price * (i.quantity || 1),
        priceNormal: i.price,
        totalSellPrice: i.price * (i.quantity || 1),
        amount: i.price,
        nameTela: `Muestrario ${i.clothName}`,
        discount: 0,
        nameUrl: i.nameUrl
      }));

      setSamplers(item as itemCartFull[]);
      setSamplersCart(samplers);
    } else {
      setSamplers([]);
      setSamplersCart([]);
    }
  }, [samplers]);

  const callGetPrices = () => {
    const { cart } = ReduxStore.getState();
    if (!cart) return;
    const payload = cart.products.map(
      ({ variant, quantity }: itemCartFull) => ({
        variant,
        amount: quantity
      })
    );
    const samplersPayload = cart.samplers.map(v => ({
      sampler: v.id,
      amount: v.quantity
    }));
    getPrice({ cloths: payload, samplers: samplersPayload as itemSampler[] });
  };

  let timer: NodeJS.Timer;
  // for calling prices every 5 secs
  useEffect(() => {
    if (!isReady) return;
    clearInterval(timer);
    timer = setInterval(() => {
      callGetPrices();
    }, 10000);
    return () => {
      clearInterval(timer);
    };
  }, [isReady, products, samplers]);

  useEffect(() => {
    callGetPrices();
  }, [debouncedValue]);

  useEffect(() => {
    if (pricesOk.length === 0) return;

    const itemsCom: string[] = [];

    products.map(x => itemsCom.push(x.variant));
    samplers.map(s => itemsCom.push(s.id));

    itemsCom.map(id => {
      const found = pricesOk.find(x => x.product === id);
      if (!found) {
        const productToDelete = products.find(x => x.variant === id);
        const samplerToDelete = samplers.find(x => x.id === id);
        if (productToDelete) {
          dispatch(deleteItemCart(productToDelete));
        } else if (samplerToDelete) {
          dispatch(deleteSamplerCart(samplerToDelete as any));
        }
      }
    });
  }, [pricesOk, products, samplers]);

  return (
    <div className="container-emptycart mb-10">
      <CardInfo title={<HeaderCart />}>
        <div
          style={{ width: '100%', textAlign: 'center' }}
          className="ant-card-head"
        >
          {itemsCarts.map((productCart: itemCartFull) => (
            <BodyCart
              key={productCart.variant}
              productItemCard={productCart}
              onChangeQuantity={changeQuantity}
            />
          ))}
          {samplersS.map(i => (
            <BodyCart
              key={i.variant}
              productItemCard={i}
              onChangeQuantity={q => changeQuantitySam(q, i.variant)}
              isSampler
            />
          ))}
        </div>
        <div>
          <FooterCart />
        </div>
        <div>
          <ActionsCart />
        </div>
      </CardInfo>
    </div>
  );
};
