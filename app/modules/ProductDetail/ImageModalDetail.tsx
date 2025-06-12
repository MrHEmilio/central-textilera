import Image from 'next/image';
import { FC, useState } from 'react';
import noImg from '/public/img/noImage.jpg';

interface Props {
  imgSource: string;
  visible: boolean;
  onClose: () => void;
}
export const ImageModalDetail: FC<Props> = ({
  visible,
  imgSource,
  onClose
}) => {
  const [srcOk, setSrc] = useState(imgSource);
  return (
    <>
      {visible && (
        <div
          className="
            absolute 
            top-0 
            left-0 
            right-0
            z-[999999999] 
            flex min-h-screen w-full flex-col
            items-center
            justify-center
            bg-black
            bg-opacity-60
            "
        >
          <button
            type="button"
            onClick={onClose}
            className=" ml-auto mr-10 mb-4 h-12 w-12 rounded-full bg-white text-3xl"
          >
            X
          </button>
          <div
            className="
              relative
              flex aspect-square 
              min-w-[99%] max-w-[99%] 
              animate-fade-in justify-center 
              md:min-w-[75%] md:max-w-[90%]
            "
          >
            <Image
              src={srcOk}
              onError={() => setSrc(noImg.src)}
              height={300}
              width={300}
              layout="fill"
            />
          </div>
        </div>
      )}
    </>
  );
};
