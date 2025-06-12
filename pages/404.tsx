import { Result } from 'antd';
import { NextPage } from 'next';
import { MainLayout } from '../app/modules/shared';

const NotFoundPage: NextPage = () => {
  return (
    <MainLayout
      title={'Página no encontrada'}
      pageDescription={'Esta pagina no existe'}
    >
      <div
        style={{
          height: 'calc( 100vh - 12rem )',
          display: 'grid',
          placeContent: 'center'
        }}
      >
        <Result
          status={404}
          title={404}
          subTitle="No se encontró la página que está buscando"
        />
      </div>
    </MainLayout>
  );
};
export default NotFoundPage;
