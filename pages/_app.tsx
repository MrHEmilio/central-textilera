import 'antd/dist/antd.css';
import '../styles/globals.scss';
import type { AppProps } from 'next/app';
import { Provider } from 'react-redux';
import { ReduxStore } from '../app/services/redux/store';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';
import { inicialConfigurations } from '../app/services/utils';
import { RouterLoader } from '../app/modules/shared/RouterLoader';

//setSesion
inicialConfigurations();

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <Provider store={ReduxStore}>
      <RouterLoader />
      <Component {...pageProps} />

      <ToastContainer limit={1} />
    </Provider>
  );
}

export default MyApp;
