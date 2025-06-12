import { useEffect, useState } from 'react';
import { SessionReducerT } from '../services/redux/reducers';
import { getUserInfo } from '../services/utils';

export const useUserInfo = () => {
  const [userinfo, setUserInfo] = useState<SessionReducerT | null>(null);
  useEffect(() => {
    setUserInfo(getUserInfo() || null);
  }, []);

  return userinfo;
};
