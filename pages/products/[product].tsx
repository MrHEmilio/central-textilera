import { Breadcrumb, Col, Row } from 'antd';
import { GetStaticPaths, GetStaticProps, NextPage } from 'next';
import { Cloth } from '../../app/interfaces/Response/Cloth/Cloth';
import { Detail } from '../../app/modules/ProductDetail';
import { CardInfo, MainLayout } from '../../app/modules/shared';
import {
  getClothBySearchUrl,
  getOrderedCloths
} from '../../app/services/cloth';
import { FeaturedProduct } from '../../app/modules/shared/FeaturedProduct/FeaturedProduct';
import Link from 'next/link';
import { SampleCard } from '../../app/modules/shared/SamplerCard';
import InnerImageZoom from 'react-inner-image-zoom';

interface Props {
  product: Cloth;
}

const DetailProduct: NextPage<Props> = ({ product }) => {
  return (
    <MainLayout
      title={product.name}
      pageDescription={product.name}
      imageFullUrl={product.image}
    >
      <div className="color-main container mx-auto" style={{}}>
        <div className="mt-8">
          <Breadcrumb>
            <Breadcrumb.Item>
              <Link href="/">Inicio</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>
              <Link href="/fabrics">All</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>
              <span className="font-extrabold">{product.name}</span>
            </Breadcrumb.Item>
          </Breadcrumb>
        </div>
        <h1 className="color-main pb-2 pt-4 text-4xl">{product.name}</h1>
        <div className="container-detailProduct">
          <Row
            gutter={[32, 16]}
            style={{ display: 'flex', justifyContent: 'center' }}
          >
            <Col
              span={12}
              xs={{ span: 24 }}
              md={{ span: 12 }}
              lg={{ span: 12 }}
            >
              <Row>
                <CardInfo>
                  <InnerImageZoom
                    src={product.image}
                    zoomType="hover"
                    width={530}
                  />
                </CardInfo>
              </Row>
              <Row className="justify-center">
                <div className="hidden max-w-[16rem] sm:block">
                  <SampleCard
                    clothName={product.nameUrl || ''}
                    sampler={product.sampler}
                  />
                </div>
              </Row>
            </Col>
            <Col
              span={12}
              xs={{ span: 24 }}
              md={{ span: 12 }}
              lg={{ span: 12, offset: 0 }}
            >
              <div>
                <CardInfo title="DESCRIPCIÓN DEL PRODUCTO ">
                  <Detail cloth={product} />
                </CardInfo>
              </div>
              <Row className="justify-center">
                <div className="block max-w-[16rem] sm:hidden">
                  <SampleCard
                    clothName={product.nameUrl || ''}
                    sampler={product.sampler}
                  />
                </div>
              </Row>
            </Col>
            <Col span={24}>
              <FeaturedProduct
                styleContainer={{
                  paddingLeft: '5px',
                  paddingRight: '0px'
                }}
                title="Productos Recomendados"
                withoutPreview={true}
                samplers={false}
              />
            </Col>
          </Row>
        </div>
      </div>
    </MainLayout>
  );
};

export const getStaticPaths: GetStaticPaths = async () => {
  const paths = (await getOrderedCloths(40))?.content.map(i => i.nameUrl) || [
    ''
  ];

  return {
    paths: paths.map(name => ({
      params: {
        product: name
      }
    })),
    fallback: 'blocking'
  };
};

export const getStaticProps: GetStaticProps = async ctx => {
  const { params } = ctx;
  const { product } = params as { product: string };

  const cloth = (await getClothBySearchUrl(product))?.content[0] || null;

  if (!cloth || cloth === null) {
    return {
      redirect: {
        destination: '/404',
        permanent: false
      }
    };
  }

  return {
    props: {
      product: cloth
    },
    revalidate: 1
  };
};

export default DetailProduct;
