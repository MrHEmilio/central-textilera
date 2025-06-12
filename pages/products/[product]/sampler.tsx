import { Breadcrumb, Button } from 'antd';
import { GetStaticPaths, GetStaticProps, NextPage } from 'next';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useState } from 'react';
import { Cloth, Sampler } from '../../../app/interfaces/Response/Cloth/Cloth';
import { Collection } from '../../../app/interfaces/Response/Collections/CollectionResponses';
import { REVALIDATE_ITERATION } from '../../../app/models/constants';
import { SocialMediaDots } from '../../../app/modules/ProductDetail';
import {
  CardInfo,
  FeaturedProduct,
  ItemCounter,
  MainLayout,
  PriceLabel,
  ProductCard
} from '../../../app/modules/shared';
import AvailableItems from '../../../app/modules/shared/AvailableItems/AvailableItems';
import {
  getClothBySearchUrl,
  getOrderedCloths
} from '../../../app/services/cloth';
import {
  addSamplerToCart,
  saveCartOnStorage
} from '../../../app/services/redux/actions/CartActions';
import { CtxDispatch } from '../../../app/services/redux/store';
import noImg from '/public/img/noSampler1.jpg';

interface Props {
  sampler: Sampler;
  clothName: string;
  cloth: Cloth;
}

const SamplerPage: NextPage<Props> = ({ sampler, clothName, cloth }) => {
  const { description, price } = sampler;
  // const [showModalZoom, setShowModalZoom] = useState(false);
  const [quantity, setQuantity] = useState(1);
  // const [imgSrc, setImgSrc] = useState(image);
  // useEffect(() => {
  //   setImgSrc(image)
  // }, [image])

  const dispatch = CtxDispatch();
  const router = useRouter();

  return (
    <MainLayout title={'Muestrario tela'} pageDescription={'Muestario de tela'}>
      <div className="color-main container my-8 mx-auto">
        <Breadcrumb>
          <Breadcrumb.Item>
            <Link href="/">Inicio</Link>
          </Breadcrumb.Item>
          <Breadcrumb.Item>Muestrario {clothName}</Breadcrumb.Item>
        </Breadcrumb>

        <h1 className="color-main mt-6 font-famBold text-2xl">
          Muestrario {clothName}
        </h1>

        <div className="mt-[4rem] mb-20 grid w-full grid-cols-1 items-start gap-4 md:grid-cols-2">
          <div>
            <CardInfo>
              <div className="relative">
                <img
                  src={noImg.src}
                  // onError={() => setImgSrc(noImg.src)}
                />
                {/* <InnerImageZoom src={imgSrc} zoomType="hover" width={700}/> */}
              </div>
            </CardInfo>
            <hr className="my-10" />
            <p className="font-famBold text-xl uppercase text-gray-600">Tela</p>
            <div className=" contents max-w-[15rem] sm:block">
              <ProductCard product={cloth as Collection} showPrice />
            </div>
          </div>
          <CardInfo title={'DESCRIPCIÓN DEL PRODUCTO'}>
            <p>{description}</p>
            <div className="mt-4">
              <div
                className="
                          grid 
                          grid-cols-1 
                          items-center 
                          justify-between gap-8
                          md:grid-cols-2
                "
              >
                <div className="col-span-2">
                  <ItemCounter
                    quantity={quantity}
                    currentQuantity={setQuantity}
                  />
                </div>

                <AvailableItems clothUrlName={cloth.nameUrl} isSampler />

                <div className="ml-3">
                  <p className=" m-0 text-xl font-bold">
                    <PriceLabel price={price} className="font-famBold" />
                  </p>
                </div>
                <Button
                  type="primary"
                  className="button-ctx"
                  onClick={() => {
                    dispatch(addSamplerToCart(sampler, quantity, clothName));
                    dispatch(saveCartOnStorage());
                    router.push('/cart');
                  }}
                >
                  Agregar al carrito
                </Button>
              </div>
            </div>
            <div>
              <SocialMediaDots />
            </div>
          </CardInfo>
        </div>
      </div>
      <div>
        <FeaturedProduct title={'Productos recomendados'} samplers />
      </div>
      {/* <ImageModalDetail
        imgSource={image}
        visible={showModalZoom}
        onClose={() => setShowModalZoom(false)}
      /> */}
    </MainLayout>
  );
};

export const getStaticPaths: GetStaticPaths = async () => {
  const paths = (await getOrderedCloths(40))?.content.map(i => i.nameUrl) || [
    ''
  ];
  return {
    paths: paths.map(name => ({
      params: { product: name }
    })),
    fallback: 'blocking'
  };
};

export const getStaticProps: GetStaticProps = async ctx => {
  const { params } = ctx;
  const { product } = params as { product: string };
  const cloth = await getClothBySearchUrl(product);
  // if (!cloth) {
  //   return {
  //     redirect: {
  //       destination: '/404',
  //       permanent: false
  //     }
  //   };
  // }
  if (cloth) {
    cloth.content[0].sampler.nameUrl = cloth?.content[0].nameUrl || '';
  }
  return {
    props: {
      sampler: cloth?.content[0].sampler,
      clothName: cloth?.content[0].name,
      cloth: cloth?.content[0]
    },
    revalidate: REVALIDATE_ITERATION
  };
};

export default SamplerPage;
