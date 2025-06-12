import { toast } from 'react-toastify';
import { changePassResponse } from '../../interfaces/Response/email/emailResponse';
import { BaseService } from '../base-service';

export const resetPassByToken = (
  token: string,
  newPassword: string
): Promise<changePassResponse | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<{ newPassword: string }, any>(
      { newPassword },
      `/security/password/${token}`
    )
    .catch(res => {
      toast.error(res.message, { theme: 'colored' });
      return null;
    });
};

export const changePassLoged = (
  oldPassword: string,
  newPassword: string
): Promise<changePassResponse | null> => {
  const baseService = new BaseService();
  return baseService
    .PutRequest<{ oldPassword: string; newPassword: string }, any>(
      { oldPassword, newPassword },
      '/security/password'
    )
    .catch(res => {
      toast.error(res.message, { theme: 'colored' });
      return null;
    });
};
