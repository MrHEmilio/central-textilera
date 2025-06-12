import Image from 'next/image';
import React, { FC } from 'react';
import noImg from '/public/img/noSampler1.jpg';

interface Props {
  img: string;
  nameColor: string;
  status: string;
  nameCloth: string;
}

export const OrderClothInfo: FC<Props> = ({
  img,
  nameColor,
  status,
  nameCloth
}) => {
  return (
    <div className="my-4 flex flex-row">
      <Image
        src={img || ''}
        alt="producto"
        width={100}
        height={100}
        className="aspect-square rounded-lg"
        onError={({ currentTarget }) => {
          currentTarget.src = noImg.src;
        }}
      />
      <div className="ml-2 flex flex-1 flex-col justify-between">
        <span className="text-lg font-bold text-main">{status}</span>
        <div>
          <p className=" uppercase">{nameCloth} </p>
          <p>{nameColor}</p>
        </div>
      </div>
    </div>
  );
};
