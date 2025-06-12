import { BaseService } from '../base-service';
import { Login } from '../../interfaces/Request/auth/Login';
import { SessionReducerT } from '../redux/reducers';

export const authService = (payload: Login): Promise<any | null> => {
  const formData = new FormData();
  formData.append('username', payload.username);
  formData.append('password', payload.password);
  const baseService = new BaseService();
  return baseService.PostRequest<any, any>(formData, 'login').catch(e => e);
};

export const authRedirect = (url: string): Promise<SessionReducerT | null> => {
  const baseService = new BaseService();
  return baseService.GetRequest<SessionReducerT>(url);
};

export const closeSesionService = () => {
  const baseService = new BaseService();
  return baseService.GetRequest('logout');
};
