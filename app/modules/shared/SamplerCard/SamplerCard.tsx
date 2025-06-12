import Image from 'next/image';
import Link from 'next/link';
import { FC } from 'react';
import { Sampler } from '../../../interfaces/Response/Cloth/Cloth';
import { CardInfo } from '../CardInfo';
import { PriceLabel } from '../PriceLabel';
import noImg from '/public/img/noSampler1.jpg';

interface Props {
  sampler: Sampler;
  clothName: string;
}
export const SampleCard: FC<Props> = ({ sampler, clothName }) => {
  const { price } = sampler;

  return (
    <CardInfo>
      <div className="text-center">
        <p
          className="
      mb-5 
      text-center
      font-famBold
      text-xl
      font-bold
      uppercase
      text-gray-500
      "
        >
          Muestrario de tela
        </p>

        <Link
          href={`/products/${clothName}/sampler`}
          className="aspect-square max-w-full cursor-pointer"
        >
          <a>
            <Image
              className="hover:scale-150"
              src={noImg.src}
              // onError={() => setImgSrc(noImg.src)}
              width={300}
              height={330}
              alt="img"
            />
          </a>
        </Link>

        <Link href={`/products/${clothName}/sampler`}>
          <h4
            className="
            text-uppercase
            cursor-pointer
            text-center
            font-famBold
            text-xl
            font-bold
            text-main
          "
          >
            Muestrario {clothName}
          </h4>
        </Link>

        <p className="mt-5">
          {' '}
          <PriceLabel price={price} className="font-famBold text-xl" />
        </p>
      </div>
    </CardInfo>
  );
};
