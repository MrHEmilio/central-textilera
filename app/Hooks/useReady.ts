import { useEffect, useState } from 'react';

export const useReady = () => {
  const [ready, setready] = useState(false);
  useEffect(() => {
    setready(true);
  }, []);
  return ready;
};
