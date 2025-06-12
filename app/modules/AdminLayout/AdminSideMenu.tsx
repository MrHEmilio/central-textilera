import { Menu, MenuProps } from 'antd';

import { useRouter } from 'next/router';
// import { ItemType } from 'antd/lib/menu/hooks/useItems';
import { useEffect, useState } from 'react';

import { SessionStore } from '../../interfaces/State/session';
import { CtxSelector } from '../../services/redux/store';
import { getItem } from '../../services/utils';
// import { GoogleIcon } from '../shared';

type MenuItem = Required<MenuProps>['items'][number];

export const AdminSideMenu = () => {
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const { menus } = userInfo as SessionStore;
  const [menuArrayState, setMenuArrayState] = useState<MenuItem[]>([]);

  const navigate = useRouter();

  const onClickMenu = (e: any) => {
    if (e.key) {
      navigate.push(e.key);
    }
  };

  useEffect(() => {
    if (menus) {
      const a: MenuItem[] = [];
      menus.forEach(({ icon, name, path }) => {
        const arrayPath = path.split('/');
        if (arrayPath.includes('admin')) {
          const iconNode = (
            // <GoogleIcon icon={icon || ''} className=" text-slate-500" />
            <img src={`/icons/${icon}.svg`} className={'w-[20px]'} />
          );
          a.push(getItem(name, path, iconNode, onClickMenu));
        }
      });
      setMenuArrayState(a);
    }
  }, [menus]);

  return (
    <div style={{ width: 256 }}>
      <div className="admin-side-menu hidden 2xl:block">
        <Menu
          defaultSelectedKeys={['1']}
          defaultOpenKeys={['sub1']}
          mode="inline"
          // inlineCollapsed={collapsed}
          items={menuArrayState}
        />
      </div>
    </div>
  );
};
