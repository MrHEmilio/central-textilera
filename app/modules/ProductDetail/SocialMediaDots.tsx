import { FC } from 'react';
import { FacebookLogo, InstagramLogo } from '../shared';

export const SocialMediaDots: FC = () => {
  return (
    <div className="mt-4 flex justify-end px-4">
      <a href="https://www.facebook.com/centraltextilera/">
        <div className=" h-10 w-10">
          <FacebookLogo />
        </div>
      </a>
      <a
        href="https://www.instagram.com/centraltextileramx/"
        className="ml-2 "
      >
        <div className=" h-10 w-10">
          <InstagramLogo />
        </div>
      </a>
    </div>
  );
};
