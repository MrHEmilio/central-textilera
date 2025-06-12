/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { FC } from 'react';
import { ScrollMenu } from 'react-horizontal-scrolling-menu';

import { LeftArrow, RightArrow } from './Arrows';
// import { CardFeaturedProduct } from '../CardFeaturedProduct/CardFeaturedProduct';
// import { ItemType } from 'react-horizontal-scrolling-menu/dist/types/types';

interface Props {
  children: React.ReactNode[];
  title: string;
}

const Arrows = (title: string) => (
  <div
    style={{
      width: '100%',
      display: 'flex',
      justifyContent: 'space-between',
      padding: '.5rem'
    }}
  >
    <div>
      <p className="header-carrusel">{title}</p>
    </div>
    <div
      style={{
        marginLeft: '10px',
        display: 'flex',
        marginBottom: '-50px',
        zIndex: '10'
      }}
    >
      <LeftArrow /> <RightArrow />
    </div>
  </div>
);

export const CarruselCard: FC<Props> = ({ children, title }) => {
  return <ScrollMenu Header={Arrows(title)}>{children as any}</ScrollMenu>;
};
