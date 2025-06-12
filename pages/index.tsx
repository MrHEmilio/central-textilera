import type { NextPage, GetStaticProps } from 'next';
import { getAllBanner } from '../app/interfaces/Response/Banner';
import { getBanners } from '../app/services/banners/bannersService';
import { FeaturedP } from '../app/interfaces/Response/Cloth/FeaturedProduc';

import {
  AboutUs,
  CarouselCtx,
  Location,
  MainLayout,
  NewLetters,
  SavePay,
  FeaturedProduct
  //Testimonials
} from '../app/modules/shared';

interface Props {
  banners: getAllBanner[];
  fProducts: FeaturedP[];
}

const Home: NextPage<Props> = ({ banners }) => {
  return (
    <MainLayout title={'Home'} pageDescription={'Pagina Principal'}>
      <div className="row ">
        <CarouselCtx banners={banners} />
      </div>
      <div className="row spacer">
        <AboutUs />
      </div>
      <div className="row spacer">
        <FeaturedProduct title="Productos Destacados" />
      </div>
      <div className="row spacer">
        <SavePay />
      </div>
      <div className="row spacer">
        <NewLetters />
      </div>
      <div className="row spacer">
        <Location height={30} />
      </div>
    </MainLayout>
  );
};

// You should use getStaticProps when:
//- The data required to render the page is available at build time ahead of a user’s request.
//- The data comes from a headless CMS.
//- The data can be publicly cached (not user-specific).
//- The page must be pre-rendered (for SEO) and be very fast — getStaticProps generates HTML and JSON files, both of which can be cached by a CDN for performance.

export const getStaticProps: GetStaticProps = async () => {
  const banners: getAllBanner[] = [];

  const response = await getBanners();

  if (response?.pagination) {
    const allBanners = await getBanners(response.pagination.totalRecords);
    allBanners?.content.map(banner => {
      if (banner.active) {
        banners.push(banner);
      }
    });
  }
  return {
    props: {
      banners
    },
    revalidate: 86400
  };
};
export default Home;
