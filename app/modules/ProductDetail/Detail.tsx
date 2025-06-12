import { Skeleton } from 'antd';
import { FC, useEffect, useState } from 'react';
import { useReady } from '../../Hooks/useReady';
import { Cloth } from '../../interfaces/Response/Cloth/Cloth';
import { getClothBySearchUrl } from '../../services/cloth';
import { ActionsDetail } from './ActionsDetail';
import { ProductRange } from './ProductRange';

interface Props {
  cloth: Cloth;
}

export const Detail: FC<Props> = ({ cloth }) => {
  const ready = useReady();
  const [pruebaCloth, setPruebaCloth] = useState<Cloth | null>();
  const [loading, setLoading] = useState<boolean>(true);
  const [quatityCloth, setQuatityCloth] = useState(1);
  const [codeColor, setCodeColor] = useState('');

  const getPricing = async (name: string) => {
    setLoading(true);
    const response = await getClothBySearchUrl(name);
    if (response) {
      setPruebaCloth(response.content[0]);
    }
    setLoading(false);
  };

  const quatityC = (quantity: number) => {
    setQuatityCloth(quantity);
  };

  const selectColor = (code: string) => {
    setCodeColor(code);
  };

  useEffect(() => {
    if (!ready) return;
    getPricing(cloth.nameUrl);
  }, [ready]);

  return (
    <div>
      <div className="container-list-product">
        <ul className="list-disc">
          {cloth?.descriptions.map((desc, index) => (
            <li key={desc.name + index.toString()}>{desc.name}</li>
          ))}
        </ul>
      </div>
      <div>
        <div className={loading ? 'hidden' : 'block'}>
          {cloth?.sale && !loading && (
            <ProductRange
              prices={cloth.prices}
              quantityCloth={quatityCloth}
              loading={loading}
              codeColor={codeColor}
              sale={cloth.sale}
            />
          )}
        </div>
        <div className={`${loading ? 'block' : 'hidden'} mt-4`}>
          <Skeleton.Input active={true} block={true} />
          <Skeleton.Input active={true} block={true} />
          <Skeleton.Input active={true} block={true} />
          <Skeleton.Input active={true} block={true} />
          <Skeleton.Input active={true} block={true} />
        </div>
      </div>
      <div className="description-product">
        <p>{cloth.mainDescription}</p>
      </div>
      <div className="description-messageproduct">
        <p>
          Antes de realizar tu orden, mándanos un <span>mensaje</span> para
          garantizar existencia y envío inmediato.
        </p>
      </div>
      <div className={loading ? 'hidden' : 'block'}>
        {cloth?.variants && (
          <ActionsDetail
            // variants={newCloth[0]?.variants}
            variants={cloth.variants}
            quantityC={quatityC}
            selectColor={selectColor}
            loading={loading}
            cloth={cloth}
          />
        )}
      </div>
      <div className={`${loading ? 'block' : 'hidden'} mt-4`}>
        <Skeleton.Input active={true} block={true} />
        <Skeleton.Input active={true} block={true} />
        <Skeleton.Input active={true} block={true} />
        <Skeleton.Input active={true} block={true} />
        <Skeleton.Input active={true} block={true} />
      </div>
    </div>
  );
};
