import { Skeleton } from 'antd';
import { FC, useEffect, useState } from 'react';
import { useGetRequest } from '../../../Hooks/useGetRequest';
import { useReady } from '../../../Hooks/useReady';
import { PaginationResponse } from '../../../interfaces/paginationResponse';
import { Cloth, Variant } from '../../../interfaces/Response/Cloth/Cloth';
import { GoogleIcon } from '../GoogleIcon';

type AvailableItems = {
  clothUrlName: string;
  variantId?: string;
  // eslint-disable-next-line no-unused-vars
  getVariant?: (variant: Variant) => void;
  isSampler?: boolean;
};

const AvailableItems: FC<AvailableItems> = ({
  clothUrlName,
  variantId,
  getVariant,
  isSampler
}) => {
  const ready = useReady();
  const buildUrl = (clothname: string) =>
    `TypeSort=ASC&columnSort=name&uses=&fibers=` +
    `&sales=&page=1&size=1&collections=` +
    `&responseStructure=VARIANTS,SALE,SAMPLER` +
    `&searchUrl=${clothname}`;
  const [selectedVariant, setselectedVariant] = useState<Variant | null>(null);
  const [urlquery, seturlquery] = useState<string>();
  const [cloth, loadingCloth] = useGetRequest<PaginationResponse<Cloth[]>>(
    `/cloth`,
    ready,
    urlquery
  );

  useEffect(() => {
    if (!ready) return;
    seturlquery(buildUrl(clothUrlName + '&' + new Date().toJSON()));
  }, [variantId]);

  useEffect(() => {
    if (!ready) return;
    seturlquery(buildUrl(clothUrlName));
  }, [ready]);

  useEffect(() => {
    if (!ready || !cloth) return;
    setselectedVariant(
      cloth.content[0].variants.find(x => x.id === variantId) || null
    );

    if (getVariant) {
      getVariant(
        cloth.content[0].variants.find(x => x.id === variantId) ||
          cloth.content[0].variants[0]
      );
    }
  }, [cloth, ready]);

  return loadingCloth ? (
    <Skeleton.Input />
  ) : !cloth || (!selectedVariant && !isSampler) ? (
    <div>
      <p className="text-redalert">
        Tuvimos un problema para obtener el inventario
      </p>
      <button
        className="flex animate-pulse items-center text-main"
        onClick={() => {
          window?.location.reload();
        }}
      >
        Recargar <GoogleIcon className="text-lg" icon={'refresh'} />
      </button>
    </div>
  ) : (
    <p className="animate-fade-in py-3 text-gray-400">
      <span className="font-famBold text-main">
        {!isSampler
          ? `${selectedVariant?.amount} ${cloth.content[0].sale.abbreviation}`
          : `${cloth.content[0].sampler.amount}`}
      </span>{' '}
      disponibles
    </p>
  );
};

export default AvailableItems;
