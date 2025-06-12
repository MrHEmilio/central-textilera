import React, { FC } from 'react';

interface Props {
  children?: React.ReactNode | undefined;
  className?: string;
}

export const CardAdmin: FC<Props> = ({ children, className }) => {
  return <div className={`${className} card-admin p-5`}>{children}</div>;
};
