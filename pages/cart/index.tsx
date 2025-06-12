import { Spin } from 'antd';
import { MainLayout } from '../../app/modules/shared';
import { Cart, EmptyCart } from '../../app/modules/Cart';
import { CtxSelector } from '../../app/services/redux/store';
import { useState, useEffect } from 'react';

const index = () => {
  const [lengthCart, setLengthCart] = useState(0);
  const [samplersLength, setSamplersLength] = useState(0);
  const [loading, setLoading] = useState(true);
  
  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const { products, samplers } = CtxSelector((state: any) => state.cart);
  useEffect(() => {
    if (products) {
      setLengthCart(products.length);
    }
    setLoading(false);
  }, [products]);

  useEffect(() => {
    if (samplers) {
      setSamplersLength(samplers.length);
    }
    setLoading(false);
  }, [samplers]);

  return (
    
    <MainLayout title={'Tu carrito'} pageDescription={'Tu carrito'}>
      <div className="color-main container mx-auto ">
        <h1 className="color-main pb-5 pt-4 text-4xl">Carrito de compra</h1>
        <div>
          <div
            className={
              lengthCart === 0 && samplersLength === 0 ? 'hidden' : 'block'
            }
          >
            <Cart />
          </div>
          <div
            className={
              lengthCart === 0 && samplersLength === 0 ? 'block' : 'hidden'
            }
          >
            {loading ? (
              <div className="flex content-center justify-center py-24 ">
                <Spin size="large" className="" />
              </div>
            ) : (
              <EmptyCart />
            )}
          </div>
        </div>
      </div>
    </MainLayout>
  );
};

export default index;
