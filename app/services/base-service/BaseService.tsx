import CtxAxios from '../interceptor';

export class BaseService {
  PostRequest<T, R>(req: T, url: string): Promise<R | null> {
    return CtxAxios.post<R>(url, req)
      .then(r => r.data)
      .catch(r => {
        return r.response ? Promise.reject(r.response.data) : null;
      });
  }

  GetRequest<R>(url: string, response = false): Promise<R | null> {
    return CtxAxios.get<R>(url)
      .then(r => r.data)
      .catch(r => {
        return response ? Promise.reject(r.response) : null;
      });
  }

  PutRequest<T, R>(req: T, url: string): Promise<R | null> {
    return CtxAxios.put<R>(url, req)
      .then(r => r.data)
      .catch(r => (r.response ? Promise.reject(r.response.data) : null));
  }

  DeleteRequest<R>(url: string): Promise<R | null> {
    return CtxAxios.delete<R>(url)
      .then(r => r.data)
      .catch(r => (r.response ? Promise.reject(r.response.data) : null));
  }

  PostRequestQ<R>(url: string, response: boolean): Promise<R | null> {
    return CtxAxios.post<R>(url)
      .then(r => r.data)
      .catch(r => {
        return response ? r.response : null;
      });
  }
}
