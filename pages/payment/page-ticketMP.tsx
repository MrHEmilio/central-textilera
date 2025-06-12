import { NextPage } from 'next';
import { MainLayout } from '../../app/modules/shared';
import { StatusMP } from '../../app/modules/payment';


const ticketMP: NextPage = () => {
  
  return (
    <MainLayout title={'Compra'} pageDescription={'Procesando pago'} >
        <StatusMP />
    </MainLayout>
  );
};
export default ticketMP;
