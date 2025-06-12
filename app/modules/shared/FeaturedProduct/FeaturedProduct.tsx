import { Skeleton } from 'antd';
import { CSSProperties, FC, useEffect, useState } from 'react';
import { getFeaturedProduct } from '../../../services/cloth';
import { CardFeaturedProduct } from '../CardFeaturedProduct';
import { CarruselCard } from '../CarruselCard/CarruselCard';
import { Cloth } from '../../../interfaces/Response/Cloth/Cloth';
import { useRouter } from 'next/router';

interface Props {
  styleContainer?: CSSProperties;
  title: string;
  withoutPreview?: boolean;
  samplers?: boolean;
}

export const FeaturedProduct: FC<Props> = ({
  styleContainer,
  title,
  withoutPreview = false,
  samplers = false
}) => {
  const [loading, setLoading] = useState(false);
  const [fProducts, setfProducts] = useState<Cloth[]>([]);
  const arrayLoading = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0];
  const navigate = useRouter();
  const callFeaturedProduct = async () => {
    setfProducts([]);
    setLoading(true);
    const featuredProduct = await getFeaturedProduct();
    if (featuredProduct) {
      setfProducts(featuredProduct.content);
    }

    setTimeout(() => {
      setLoading(false);
    }, 300);
  };

  useEffect(() => {
    callFeaturedProduct();
  }, [navigate]);

  return (
    <div className="w-full">
      <div
        className="container-featuredproduct container m-auto "
        style={styleContainer}
      >
        <CarruselCard title={title}>
          {!loading &&
            fProducts.map(product => (
              <CardFeaturedProduct
                key={product.id}
                product={product}
                withoutPreview={withoutPreview}
                samplershow={samplers}
              />
            ))}

          {loading &&
            arrayLoading.map(number => (
              <div key={number} className="mb-3">
                <Skeleton.Image
                  active
                  className="mx-5 mt-3 mb-3 h-80 w-64 rounded-xl"
                />
              </div>
            ))}
        </CarruselCard>
      </div>
    </div>
  );
};
