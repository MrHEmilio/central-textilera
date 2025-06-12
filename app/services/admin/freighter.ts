import { toast } from 'react-toastify';
import { RequestCreateFreighter, RequestUpdateFreighter } from '../../interfaces/Request/Admin/Freighter';
import { ResponseNewFreighter } from '../../interfaces/Response/Admin/Freighter';
import { BaseService } from '../base-service';

const baseService = new BaseService();
export const newFreighter = (
  payload: RequestCreateFreighter
): Promise<ResponseNewFreighter | null> => {
  return baseService
    .PostRequest<RequestCreateFreighter, ResponseNewFreighter>(payload, 'freighter')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const editFreighter = (
  payload: RequestUpdateFreighter
): Promise<ResponseNewFreighter | null> => {
  return baseService
    .PutRequest<RequestUpdateFreighter, ResponseNewFreighter>(payload, 'freighter')
    .catch(e => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const deleteFreighter = (id: string): Promise<ResponseNewFreighter | null> => {
  return baseService.DeleteRequest<ResponseNewFreighter>(`freighter/${id}`);
};

export const reactivateFreighter = (id: string): Promise<void | null> => {
  return baseService
    .PutRequest<null, ResponseNewFreighter>(
      null, `freighter/${id}`
    )
    .then((r: any) => {
      toast.success(
        r?.message,
        { theme: 'colored' }
      );
    })
    .catch((r: any) => {
      toast.error(r.message, { theme: 'colored' });
    });
  }