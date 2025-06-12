import React, { FC, CSSProperties } from 'react';
import Link from 'next/link';

interface Props {
  label?: string;
  href: string;
  classNameLink?: string;
  styleLink?: CSSProperties;
  children?: React.ReactNode;
}
export const LinkCtx: FC<Props> = ({ href, children, classNameLink }) => {
  return (
    <Link className={classNameLink} href={href}>
      {children}
    </Link>
  );
};
