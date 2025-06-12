import { Modal } from 'antd';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { Cloth } from '../../../interfaces/Response/Cloth/Cloth';
import {
  addProductDetail,
  cleanProductDetail
} from '../../../services/redux/actions/ProductDetailActions';
import { CtxDispatch } from '../../../services/redux/store';
import { ModalProduct } from '../../fabrics';
import noImg from '/public/img/noSampler1.jpg';

interface Props {
  product: Cloth;
  withoutPreview?: boolean;
  samplershow?: boolean;
}

export const CardFeaturedProduct: FC<Props> = ({
  product,
  withoutPreview = false,
  samplershow = false
}) => {
  const dispatch = CtxDispatch();
  const { image, name, mainDescription, prices, sampler, nameUrl } = product;
  const navigate = useRouter();
  const [showModal, setShowModal] = useState<boolean>(false);
  const [srcImage, setSrcImage] = useState(samplershow ? sampler.image : image);

  const onClick = () => {
    dispatch(cleanProductDetail());
    dispatch(addProductDetail(product as Cloth));

    if (samplershow) {
      navigate.push(`/products/${nameUrl}/sampler`);
    } else {
      navigate.push(`/products/${nameUrl}`);
    }
  };

  useEffect(() => {
    setSrcImage(samplershow ? noImg.src : image);
  }, [image, samplershow]);

  return (
    <div className="featured-container">
      <div>
        <div className="featured-image-container">
          <div className="relative">
            {!withoutPreview && (
              <div
                className="
                hidde-component
              absolute
              z-10
              h-full
              w-full
              cursor-pointer
              place-content-center opacity-100
              transition-all
              duration-500
              hover:opacity-100
              md:opacity-0
              "
                onClick={e => {
                  dispatch(cleanProductDetail());
                  dispatch(addProductDetail(product as Cloth));
                  if (e.target !== e.currentTarget) return;
                  if (samplershow) {
                    navigate.push(`/products/${nameUrl}/sampler`);
                  } else {
                    navigate.push(`/products/${nameUrl}`);
                  }
                }}
              >
                {!samplershow && (
                  <a
                    onClick={() => setShowModal(!showModal)}
                    className="z-50 rounded-3xl bg-main px-3 py-3 text-white"
                  >
                    Vista previa
                  </a>
                )}
              </div>
            )}
            <div onClick={onClick} className="p-2">
              <a>
                <img
                  className="featured-img"
                  onError={() => setSrcImage(noImg.src)}
                  alt={mainDescription}
                  src={srcImage}
                />
              </a>
            </div>
          </div>
          <p
            className="cursor-pointer text-base"
            onClick={onClick}
            style={{ minHeight: '50px' }}
          >
            {samplershow ? `Muestrario ${name}` : name}
          </p>
          <Modal
            visible={showModal}
            footer={null}
            onCancel={() => setShowModal(false)}
            width={767}
          >
            <ModalProduct productNameUrl={product.nameUrl} />
          </Modal>
          <span className="text-lg">
            $ {samplershow ? sampler.price : prices[0].price}.00
          </span>
        </div>
      </div>
    </div>
  );
};
