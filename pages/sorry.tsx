import { NextPage } from 'next';
import { MainLayout } from '../app/modules/shared';

const SorryPage: NextPage = () => {
  return (
    <MainLayout title="Sorry" pageDescription={'Servidores en Mantenimiento'}>
      <div
        style={{
          height: 'calc( 100vh - 12rem )',
          display: 'grid',
          placeContent: 'center',
          marginBottom: '2rem'
        }}
      >
        <img
          src="/img/iMantenimientoSitioWeb.png"
          className="flex justify-self-center"
        />
        <h1 className="text-center text-2xl font-black text-redalert md:text-6xl ">
          Servidores en Mantenimiento
        </h1>
        <p className="mt-6 flex justify-self-center text-center text-base md:w-3/5 md:text-2xl">
          Lo sentimos, por el momento nuestros servidores experimentan más
          tráfico de lo normal. Te agradecemos regresar más tarde.
        </p>
      </div>
    </MainLayout>
  );
};

export default SorryPage;
