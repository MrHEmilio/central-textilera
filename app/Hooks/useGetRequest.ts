import { useEffect, useState } from 'react';
import { BaseService } from '../services';

export const useGetRequest = <T>(
  url: string,
  instantCall = true,
  query?: string
) => {
  const [loading, setLoading] = useState(false);
  const [response, setResponse] = useState<T | null>();
  const { GetRequest } = new BaseService();

  const getAsync = async () => {
    setLoading(true);
    const r = await GetRequest(`${url}${query ? '?' + query : ''}`);
    setResponse(r as T);
    setLoading(false);
  };

  useEffect(() => {
    if (instantCall) getAsync();
  }, []);

  useEffect(() => {
    if (url) getAsync();
  }, [url]);

  /* useEffect(() => {
    if (instantCall) getAsync();
  }, [instantCall]); */

  useEffect(() => {
    if (instantCall) getAsync();
  }, [query]);

  return [response as T, loading] as const;
};
