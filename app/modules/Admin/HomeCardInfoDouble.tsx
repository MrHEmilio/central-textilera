import Link from 'next/link';
import React, { FC } from 'react';
import { GoogleIcon } from '../shared';
import Arrow from '/public/img/arrow_forward.svg';

interface Props {
  titleLeft: string;
  bodyLeft: string;
  iconLeft: string;
  titleRight: string;
  bodyRight: string;
  iconRight: string;
  colorFooter?: string;
  footerLink?: boolean;
  url?: string;
  childrenBody?: React.ReactNode;
}

export const HomeCardInfoDouble: FC<Props> = ({
  titleLeft,
  bodyLeft,
  iconLeft,
  colorFooter,
  footerLink,
  url,
  childrenBody,
  bodyRight,
  iconRight,
  titleRight
}) => {
  return (
    <>
      <div className="body-adminCard ">
        <div className="flex py-4 px-4 md:flex-col md:items-center 2xl:flex-row 2xl:justify-between">
          <div className="md:mb-4 2xl:mb-0">
            <div className="flex md:justify-center 2xl:justify-start">
              <GoogleIcon className="text-[44px]" icon={iconLeft} />
              <p className=" ml-5 text-[30px] font-bold">{bodyLeft}</p>
            </div>
            <p className="ml-[64px] text-[#797979]">{titleLeft}</p>
          </div>
          <div>
            <div className="flex md:justify-center">
              <GoogleIcon className="text-[44px]" icon={iconRight} />
              <p className=" ml-5 text-[30px] font-bold">{bodyRight}</p>
            </div>
            <p className="ml-[64px] text-[#797979]">{titleRight}</p>
          </div>
        </div>
        {childrenBody && <div className="p-4">{childrenBody}</div>}
      </div>
      {/* <div className={`footer-adminCard h-12 bg-[${colorFooter}]`}></div> */}
      <div
        className={`footer-adminCard  flex h-12 items-center justify-end`}
        style={{ backgroundColor: colorFooter }}
      >
        {footerLink && (
          <Link href={url || ''}>
            <a className="mr-4 flex font-semibold text-[#006EB2]">
              Ver más <img className="ml-2" src={Arrow.src} />
            </a>
          </Link>
        )}
      </div>
    </>
  );
};
