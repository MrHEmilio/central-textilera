import { useRouter } from 'next/router';
import React, { FC } from 'react';
import { Collection } from '../../../interfaces/Response/Collections/CollectionResponses';

interface Props {
  cloth: Collection;
  closeModal?: () => void;
}

export const CardSearchCollection: FC<Props> = ({ cloth, closeModal }) => {
  const navigate = useRouter();

  const onClickSearch = () => {
    navigate.push(`/fabrics?collections=${cloth.id}&name=${cloth.name}`);
    if (closeModal) {
      closeModal();
    }
  };

  return (
    <div
      className=" cs-container mt-4 flex cursor-pointer flex-col"
      onClick={onClickSearch}
    >
      <div className="mb-3 flex flex-col items-center  xl:mb-0 xl:flex-row">
        <div className="cs-img mb-3 w-24">
          <img
            src={cloth.image}
            alt={cloth.mainDescription}
            style={{ borderRadius: '9px' }}
          />
        </div>
        <div className="cs-info ">
          <a>{cloth.name}</a>
        </div>
      </div>
    </div>
  );
};
