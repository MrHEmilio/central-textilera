import { toast } from 'react-toastify';
import { changePassResponse } from '../../interfaces/Response/email/emailResponse';
import { BaseService } from '../base-service';

export const changePassword = (
  email: string
): Promise<changePassResponse | null> => {
  const baseService = new BaseService();
  return baseService
    .PostRequest<any, any>({}, `/send/email/change/password/${email}`)
    .catch(res => {
      toast.error(res.message, { theme: 'colored' });
      return null;
    });
};

export const verifyEmailAdmin = (
  id: string
): Promise<changePassResponse | null> => {
  const baseService = new BaseService();
  return baseService
    .PostRequest<any, any>({}, `/send/email/verify/email/${id}`)
    .catch(res => {
      toast.error(res.message, { theme: 'colored' });
      return null;
    });
};

export const verifyTrackingOrder = (
  order: string
): Promise<{ message: string; id?: string } | null> => {
  const baseService = new BaseService();
  return baseService.PostRequest<any, any>({}, `/send/email/ticket/${order}`);
};
