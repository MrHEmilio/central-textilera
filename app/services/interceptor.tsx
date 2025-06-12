import axios from 'axios';
import { toast } from 'react-toastify';
import { EndpointWith409 } from '../models/Enums';
import { LogOut } from './redux/actions/SessionActions';
import { ReduxStore } from './redux/store';

export const CtxAxios = axios.create({
  baseURL: process.env.NEXT_PUBLIC_BASE_URL
});

CtxAxios.interceptors.request.use(
  function (config) {
    let cookieSession = '';

    if (typeof window !== 'undefined') {
      cookieSession = localStorage.getItem('session') || ' ';
    }
    config.headers = {
      // 'Content-type': 'application/json',
      'x-auth-token': cookieSession
    };
    // config.withCredentials = true;
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add a response interceptor
CtxAxios.interceptors.response.use(
  function (response) {
    const cookieSession = response.headers['x-auth-token'];
    if (cookieSession) {
      if (typeof window !== 'undefined') {
        localStorage.setItem('session', cookieSession);
      }
    }

    return response;
  },
  function (error) {
    /* if (error.response.status === 500) {
      if (typeof window === 'undefined') {
        return Promise.reject(error);
      } else {
        window.location.href = '/sorry';
      }
    } */
    if (
      error.response.status === 409 &&
      !EndpointWith409.includes(error.response.request.responseURL)
    ) {
      return Promise.reject(error);
    }
    let message = error.response?.data?.message || 'Hubo un error inesperado';

    if (error.response.status === 401) {
      if (typeof window !== 'undefined') {
        const array = window.location.pathname.split('/');
        if (array.includes('admin')) {
          window.location.href = '/';
        }
      }
      message = 'Es necesario que inicie sesión para acceder a esta sección.';
      ReduxStore.dispatch(LogOut());
      localStorage.clear();
    }
    toast.error(`${message}`, { theme: 'colored' });
    return Promise.reject(error);
  }
);

export default CtxAxios;
