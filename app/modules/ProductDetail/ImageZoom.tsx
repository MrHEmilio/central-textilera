import React, { FC, useEffect, useState } from 'react';
import noImg from '/public/img/noImage.jpg';

interface Props {
  src: string;
  description: string;
}

export const ImageZoom: FC<Props> = ({ src, description }) => {
  const [srcfinal, setSrc] = useState(src);
  useEffect(() => {
    setSrc(src);
  }, [src]);
  return (
    <div className="container-img">
      <img
        className="product-img hover-zoom"
        onError={() => setSrc(noImg.src)}
        src={srcfinal}
        alt={description}
      />
    </div>
  );
};
