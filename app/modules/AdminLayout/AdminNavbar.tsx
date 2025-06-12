import { SearchOutlined, MenuOutlined } from '@ant-design/icons';

import { Drawer, Input, Select } from 'antd';
import React, { useState } from 'react';
import { GoogleIcon } from '../shared';
import { LinkCtx } from '../shared/Link';

const { Option } = Select;

export const AdminNavbar = () => {
  const [visible, setVisible] = useState<boolean>(false);
  const close = () => {
    setVisible(false);
  };
  const toogleMenu = () => {
    setVisible(!visible);
  };
  return (
    <nav className="admin-navbar ">
      <div className="admin-navbar-bodycontanier">
        <div>
          <LinkCtx href="/">
            <img
              src="/central-textilera.webp"
              alt="logo Central textilera"
              className={'admin-navbar-img cursor-pointer'}
            />
          </LinkCtx>
        </div>

        <div className="admin-navbar-search">
          <Input
            placeholder="Buscar"
            // onSearch={onSearch}
            className={`custom-input custom-searchinput w-3/5`}
            suffix={<SearchOutlined />}
          />
        </div>
        <div className="admin-navbar-select">
          <GoogleIcon icon={'person'} />
          <Select
            bordered={false}
            // className="w-full"
            defaultValue="Jose Luis Zavala"
          >
            <Option value="Option1-1">Option1-1</Option>
            <Option value="Option1-2">Option1-2</Option>
          </Select>
        </div>
        <div className="sidebar-menu-response">
          <button
            type="button"
            className="flex items-center text-lg"
            style={{
              border: 'none',
              // marginLeft: '.5rem',
              background: 'transparent'
            }}
            onClick={toogleMenu}
          >
            <MenuOutlined />
          </button>
          <Drawer placement="left" visible={visible} onClose={close}>
            <div className="mb-4">
              <Input
                placeholder="Buscar"
                // onSearch={onSearch}
                className="custom-input  custom-searchinput w-full"
                suffix={<SearchOutlined />}
              />
            </div>
            <div className="admin-navbar-selecresponse">
              <GoogleIcon icon={'person'} />
              <Select
                bordered={false}
                // className="w-full"
                defaultValue="Jose Luis Zavala"
              >
                <Option value="Option1-1">Option1-1</Option>
                <Option value="Option1-2">Option1-2</Option>
              </Select>
            </div>
          </Drawer>
        </div>
      </div>
    </nav>
  );
};
