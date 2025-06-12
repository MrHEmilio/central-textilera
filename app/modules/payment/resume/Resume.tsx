import { Skeleton } from 'antd';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { Sampler } from '../../../interfaces/Response/Cloth/Cloth';
import { CartState, itemCartFull } from '../../../interfaces/State/Cart';
import {
  getPricesCart,
  GetPricesResponse,
  itemCart
} from '../../../services/Order';
import { CtxSelector, ReduxStore } from '../../../services/redux/store';
import { PriceLabel } from '../../shared';
import { ProductResume } from './ProductResume';

export const Resume: FC = () => {
  const { shippingInfo, deliveryMethod }: CartState = CtxSelector(
    state => state.cart!
  );
  const router = useRouter();
  const [productList, setProductList] = useState<any[]>([]);
  const [totalNormal, setTotalNormal] = useState(0);
  // const [ivacalc, setIvacalc] = useState(0);
  const [loading, setLoading] = useState(false);
  const [totalWDiscount, setTotalWDiscount] = useState<number>(0);
  const [samplerList, setSamplerList] = useState<itemCartFull[]>([]);
  // const [shipp, setShipp] = useState<ShippmentProviderResponse>();

  const { isReady } = router;

  const getPrices = async () => {
    if (productList.length === 0 && samplerList.length === 0) return;

    setLoading(true);

    const mapped = productList.map(i => ({
      variant: i.variant,
      amount: i.quantity
    }));
    const samplerMapped = samplerList.map(i => ({
      sampler: i.variant,
      amount: i.quantity
    }));

    const res: GetPricesResponse[] = (await getPricesCart({
      cloths: mapped,
      samplers: samplerMapped
    })) as any;

    setLoading(false);

    if (!res) return;

    let total = 0;
    let totalDiscount = 0;
    let totalSamplers = 0;

    const ls = productList as itemCartFull[];
    res.map(i => {
      samplerList.map(sampler => {
        if (i.product === sampler.variant) {
          totalSamplers += i.sellPrice * sampler.quantity;
        }
      });
      ls.map(pr => {
        if (pr.variant === i.product) {
          total += i.priceNormal * pr.quantity;
          totalDiscount += i.sellPrice * pr.quantity;
        }
      });
    });

    setTotalNormal(total + totalSamplers);
    setTotalWDiscount(totalDiscount + totalSamplers);
    // setIvacalc((totalDiscount + totalSamplers) * IVA_TAX_PERCENTAGE);
  };

  useEffect(() => {
    if (!isReady) return;
    const { cart } = ReduxStore.getState();

    // const uInfo = getUserInfo();
    if (!cart) {
      router.push('/');
      toast.warning('No ha añadido telas a su carrito');
      return;
    }

    if (cart.samplers.length > 0) {
      const item: itemCartFull[] = cart.samplers.map((i: Sampler) => ({
        quantity: i.quantity!,
        sellPrice: i.price,
        variant: i.id,
        img: i.image,
        totalSellPriceNormal: i.price,
        priceNormal: i.price,
        totalSellPrice: i.price,
        amount: i.price,
        nameTela: `Muestrario ${i.clothName}`,
        discount: 0
      }));

      setSamplerList(item || []);
    }

    setProductList((cart.products as itemCart[]) || []);
  }, [isReady]);

  useEffect(() => {
    if (!isReady) return;
    getPrices();
  }, [productList, isReady]);

  return (
    <div>
      {productList.map(prod => (
        <ProductResume key={prod.variant} product={prod as itemCartFull} />
      ))}
      {samplerList.map(samp => (
        <ProductResume
          isSampler
          key={samp.variant + 'sampler'}
          product={samp}
        />
      ))}
      <div className="resume-section">
        <div className="grid grid-cols-10">
          <div className="col-span-5 font-bold">Subtotal</div>
          <div className="col-span-5 self-end text-end">
            {loading && <Skeleton.Input />}
            {!loading && (
              <div className="flex justify-end gap-6 font-bold">
                {totalWDiscount !== totalNormal && (
                  <h4>
                    <PriceLabel
                      className="text-[#006EB2] line-through"
                      coin
                      price={totalNormal}
                    />
                  </h4>
                )}
                <h4>
                  <PriceLabel coin price={totalWDiscount} />
                </h4>
              </div>
            )}
          </div>
        </div>
        {deliveryMethod === 'SHIPPING' && (
          <div className="mt-2 flex">
            <div className="font-bold">Envíos</div>
            <div className="flex-1 self-end text-end">
              {shippingInfo?.price && (
                <h4 className="font-bold">
                  <PriceLabel coin price={shippingInfo.price} />
                </h4>
              )}
              {!shippingInfo?.price && (
                <h4 className="font-bold">Calculado en el siguiente paso</h4>
              )}
            </div>
          </div>
        )}
      </div>

      <div className="resume-section mt-2">
        <div className="flex content-between font-bold">
          <p>Nuestros precios ya incluyen IVA</p>
        </div>
      </div>

      <div className="resume-section">
        <div className="flex">
          <div className="font-bold">Total</div>
          <div className=" flex-1 text-end font-bold">
            {loading && <Skeleton.Input />}
            {!loading && (
              <PriceLabel
                coin
                price={totalWDiscount + (shippingInfo?.price || 0)}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
