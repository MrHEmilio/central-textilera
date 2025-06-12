import { BaseService } from '../base-service';
import { Contac } from '../../interfaces/Request/Contac/Contac';

export const contactUs = (payload: Contac) => {
  const baseService = new BaseService();
  return baseService.PostRequest<Contac, any>(
    payload,
    '/send/email/contact/us'
  );
};
