import { FC, HTMLAttributes } from 'react';

interface Props {
  price?: number;
  coin?: boolean;
}
export const PriceLabel: FC<Props & HTMLAttributes<HTMLDivElement>> = ({
  price,
  className,
  coin
}) => {
  return (
    <span className={className}>
      {`$ ${price?.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')} ${
        coin ? 'MXN' : ''
      }`}
    </span>
  );
};
