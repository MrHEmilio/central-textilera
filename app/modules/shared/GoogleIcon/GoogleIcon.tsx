import { FC, HTMLAttributes } from 'react';

interface Props {
  icon: 'local_shipping' | string;
}
export const GoogleIcon: FC<Props & HTMLAttributes<HTMLDivElement>> = ({
  icon,
  className = 'text-5xl'
}) => <span className={`${className} material-symbols-outlined `}>{icon}</span>;
