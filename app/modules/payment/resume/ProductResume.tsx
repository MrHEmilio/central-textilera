import { Badge, Skeleton } from 'antd';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { itemCartFull } from '../../../interfaces/State/Cart';
import { getPricesCart, GetPricesResponse } from '../../../services/Order';
import { PriceLabel } from '../../shared';
import noImg from '/public/img/noSampler1.jpg';

interface Props {
  product: itemCartFull;
  isSampler?: boolean;
}

// get price getPricesCart

export const ProductResume: FC<Props> = ({ product, isSampler }) => {
  const router = useRouter();

  const { isReady } = router;
  const [loading, setLoading] = useState(false);
  const [prices, setPrices] = useState<GetPricesResponse[]>();
  const [srcImage, setSrcImage] = useState(product.img);

  const getPrices = async () => {
    setLoading(true);
    let res;
    if (!isSampler) {
      res = await getPricesCart({
        cloths: [{ variant: product.variant, amount: product.quantity }],
        samplers: []
      });
    } else {
      res = await getPricesCart({
        cloths: [],
        samplers: [{ sampler: product.variant, amount: product.quantity }]
      });
    }
    if (res) {
      setPrices(res);
    }
    setLoading(false);
  };

  useEffect(() => {
    
    if (!isReady) return;
    getPrices();
  }, [isReady]);

  return (
    <div className="resume-section">
      <div className="flex flex-row items-center gap-6">
        <div>
          {product.abbreviation && (
            <Badge
              count={`${product.quantity}  ${product.abbreviation}`}
              color="#7f7f7f"
            >
              <img
                onError={() => setSrcImage(noImg.src)}
                src={srcImage}
                className="max-w-[7rem]"
              />
            </Badge>
          )}
          {!product.abbreviation && (
            <Badge count={`${product.quantity}`} color="#7f7f7f">
              <img src={noImg.src} className="max-w-[7rem]" />
            </Badge>
          )}
        </div>
        <div className="flex max-w-[5rem] flex-col">
          <p className="font-bold uppercase sm:text-sm md:text-sm 2xl:text-lg">
            {product.nameTela}
          </p>
          <p>{product.nameColor}</p>
        </div>
        <div className="flex-grow-[10] self-end text-end font-bold">
          {loading && <Skeleton.Input />}
          {!loading && !prices && (
            <p className="text-red-200">
              No pudimos obtener el precio de este artículo
            </p>
          )}
          {!loading && prices && (
            <div className="flex justify-end gap-6">
              {prices[0]?.totalSellPriceNormal !==
                prices[0]?.totalSellPrice && (
                <PriceLabel
                  coin
                  className="text-[#006EB2] line-through"
                  price={prices[0]?.totalSellPriceNormal || 0}
                />
              )}
              <PriceLabel price={prices[0]?.totalSellPrice || 0} coin />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
