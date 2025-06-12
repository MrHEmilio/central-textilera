import { useEffect, useState } from 'react';
import { CtxSelector } from '../../services/redux/store';
import { itemCartFull } from '../../interfaces/State/Cart';
import { Skeleton } from 'antd';
import { formatNumber } from '../../services/utils';
import { Sampler } from '../../interfaces/Response/Cloth/Cloth';

export const FooterCart = () => {
  const [loadingFooter, setLoadingFooter] = useState(true);
  const [totalNormalPrice, setTotalNormalPrice] = useState(0);
  const [totalPrice, setTotalPrice] = useState(0);
  const { products, loading, samplers } = CtxSelector(
    (state: any) => state.cart
  );

  const getTotal = () => {
    if (totalNormalPrice === totalPrice) {
      return <div>{`$${formatNumber(totalPrice)}`}</div>;
    } else {
      return (
        <div>
          <h4 className="text-[#006EB2] line-through ">{`$${formatNumber(
            totalNormalPrice
          )}`}</h4>
          <h4>{`$${formatNumber(totalPrice)}`}</h4>
        </div>
      );
    }
  };

  useEffect(() => {
    setTimeout(() => {
      setLoadingFooter(false);
    }, 800);
  }, []);

  useEffect(() => {
    if (products) {
      let SumaNormal = 0;
      let SumaDescuento = 0;
      let SumaSamplers = 0;
      products.map(
        (product: itemCartFull) => (
          (SumaNormal =
            SumaNormal +
            (product?.totalSellPriceNormal
              ? product?.totalSellPriceNormal
              : 0)),
          (SumaDescuento =
            SumaDescuento +
            (product?.totalSellPrice ? product?.totalSellPrice : 0))
        )
      );

      const samplersF = samplers as Sampler[];
      if (samplersF.length > 0) {
        samplersF.map(sam => (SumaSamplers += sam.price * (sam.quantity || 1)));
      }

      setTotalNormalPrice(SumaNormal + SumaSamplers),
        setTotalPrice(SumaDescuento + SumaSamplers);
    }
  }, [products, samplers]);

  return (
    <>
      <div className="footer-container">
        <p style={{ color: '#898989', fontSize: '16px', fontWeight: '700' }}>
          Subtotal
        </p>
        <div
          style={{ marginTop: '.2rem', fontWeight: '700', color: '#282727' }}
        >
          <div className={loading ? 'hidden' : 'block'}>
            {loadingFooter ? (
              <Skeleton.Input active={true} size="small" />
            ) : (
              getTotal()
            )}
          </div>
          <div className={loading ? 'block' : 'hidden'}>
            <Skeleton.Input active={true} size="small" />
          </div>
        </div>
      </div>
    </>
  );
};
