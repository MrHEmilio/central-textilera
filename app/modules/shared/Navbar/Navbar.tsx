import { useEffect, useState } from 'react';
import { Badge, Modal } from 'antd';
import { LinkCtx } from '../Link';
import { SideMenu } from '../SideMenu';
import style from './Navbar.module.css';
import { Menu } from '../../../models';
import { ModalLogin } from '../ModalLogin';
import { TitleModalLogin } from '../TitleModalLogin';
import { SearchOutlined } from '@ant-design/icons';
import { CtxSelector } from '../../../services/redux/store';
import { AdminResponseMenu } from '../../AdminLayout/AdminResponseMenu';

import Link from 'next/link';
import { SearchBar } from '../SearchBar/SearchBar';
import { useRouter } from 'next/router';
import { ArrarWithSearchBar } from '../../../models/Enums';

export const Navbar = () => {
  const { products, samplers } = CtxSelector((state: any) => state.cart);
  const sesion = CtxSelector((state: any) => state.session);
  const [roleState, setRoleState] = useState('PUBLIC');
  const [numberNotifications, setNumberNotifications] = useState(0);
  const [admin, setAdmin] = useState(true);
  const [currentePath, setCurrentePath] = useState('');
  const [popOver, setPopOver] = useState(false);
  const navigate = useRouter();

  const changePopOver = () => {
    setPopOver(false);
  };

  useEffect(() => {
    setCurrentePath(navigate.asPath);
  }, []);

  useEffect(() => {
    const local = localStorage.getItem('info');
    if (local) {
      const info = JSON.parse(local);
      if (info.role === 'ADMIN_ROOT' || info.role === 'ADMIN') {
        localStorage.removeItem('cart');
        setAdmin(false);
      } else {
        setAdmin(true);
      }
    }
  }, []);

  useEffect(() => {
    if (products.length >= 0 || samplers.lenght >= 0) {
      setNumberNotifications(
        samplers ? samplers.length + products.length : products.length
      );
    }
  }, [products, samplers]);

  useEffect(() => {
    if (sesion.role) {
      setRoleState(sesion.role);
    } else if (!sesion.role) {
      setRoleState('PUBLIC');
    }
  }, [sesion]);

  return (
    <nav className={`${style.navBarContainer}`}>
      <div className={`${style.BodyConteiner} `}>
        <div>
          <LinkCtx href="/">
            <img
              src="/central-textilera.webp"
              alt="logo Central textilera"
              className={style.image + ' cursor-pointer'}
            />
          </LinkCtx>
        </div>
        <div className={style.menuContainer}>
          <nav>
            {Menu.map(({ href, label, roles }) => {
              return (
                <LinkCtx key={href} href={href}>
                  <a
                    className={`${style.linkNavbar} ${roles.includes(roleState) ? 'inline' : 'hidden'
                      }`}
                  >
                    {label}
                  </a>
                </LinkCtx>
              );
            })}
          </nav>
        </div>
        <div className={`${style.actionContainer} `}>
          <div className={style.iconContainer}>

            <div className="relative">
              <div className={`${style.CircleContainer} flex justify-center items-center`}>
                <LinkCtx href="/fabrics">
                  <button className="bg-main text-white text-sm md:text-base lg:text-lg px-4 py-2 rounded-md hover:bg-main-dark transition">
                    Comprar ahora
                  </button>
                </LinkCtx>
              </div>
            </div>

            {admin && (
              <Badge
                count={numberNotifications}
                offset={[-16, 1]}
                size="small"
                color={'#006eb2'}
              >
                <LinkCtx href={'/cart'}>
                  <div
                    className={
                      style.CircleIconContainer + ' bg-main text-white'
                    }
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="16"
                      height="16"
                      fill="currentColor"
                      className="bi bi-cart-fill icon-navbar"
                      viewBox="0 0 16 16"
                    >
                      <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z" />
                    </svg>
                  </div>
                </LinkCtx>
              </Badge>
            )}

            <div className="relative">
              <div
                className={`${style.CircleIconContainer} bg-main text-white`}
                onClick={() => setPopOver(true)}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  className="bi bi-person-fill icon-navbar svgUser"
                  viewBox="0 0 16 16"
                >
                  <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z" />
                </svg>
              </div>
            </div>

            <div className={`${style.CircleIconContainer} ${style.iconMenu}`}>
              <Link href={'/search'}>
                <button
                  type="button"
                  className="flex items-center text-lg"
                  style={{
                    border: 'none',
                    // marginLeft: '.5rem',
                    background: 'transparent'
                  }}
                >
                  <SearchOutlined height="50px" />
                </button>
              </Link>
            </div>

            <div className={`${style.CircleIconContainer} ${style.iconMenu}`}>
              <SideMenu />
            </div>
            {!admin && (
              <div
                className={`${style.CircleIconContainer} ${style.iconMenu} hidden md:block 2xl:hidden`}
              >
                <AdminResponseMenu />
              </div>
            )}
          </div>
          <div className={style.search}>
            {ArrarWithSearchBar.includes(currentePath) && <SearchBar />}
          </div>
          {popOver && (
            <Modal
              title={<TitleModalLogin onCloseSesion={changePopOver} />}
              visible={popOver}
              closable={false}
              mask={false}
              onCancel={changePopOver}
              style={{ top: '75px', left: '0' }}
              footer={null}
              className={'sm:left-28 md:left-44'}
              width={'20rem'}
            >
              <ModalLogin admin={admin} />
            </Modal>
          )}
        </div>
      </div>
    </nav>
  );
};
