// import { SubscribeNewsLetter } from '../../interfaces/Request/NewsLetter/SubscribeNewsletter';
import { BaseService } from '../base-service/BaseService';
export const addNewLetters = (
  email: string,
  response = false
): Promise<{
  data?: any;
  message: string;
  status?: number;
} | null> => {
  const baseService = new BaseService();
  return baseService.PostRequestQ<{ message: string; status?: number }>(
    `newsletter/${email}`,
    response
  );
};

export const verifyNewsLetter = (email: string) => {
  const baseService = new BaseService();
  return baseService.GetRequest(`newsletter/${email}`);
};

export const unSubscribeNews = (email: string) => {
  const baseService = new BaseService();
  return baseService.DeleteRequest(`newsletter/${email}`);
};
