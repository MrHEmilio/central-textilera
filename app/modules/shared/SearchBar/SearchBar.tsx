import { SearchOutlined } from '@ant-design/icons';
import { Input, Popover } from 'antd';
import React, { ChangeEvent, useEffect, useState } from 'react';
import useDebounce from '../../../Hooks/useDebouncer';
import { SearchContainer } from '../SearchContainer';

export const SearchBar = () => {
  const [loading, setLoading] = useState(true);
  const [modalSearh, setModalSearch] = useState(false);
  const [searchRender, setSearchRender] = useState(true);
  const [value, setValue] = useState<string>('');
  const debouncedValue = useDebounce<string>(value, 1000);

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    setLoading(true);
    setValue(event.target.value);
    setModalSearch(true);
  };

  useEffect(() => {
    if (!debouncedValue) {
      setModalSearch(false);
      setLoading(true);
      return;
    }
    setSearchRender(false);
    setTimeout(() => {
      setSearchRender(true);
    }, 1);
  }, [debouncedValue]);

  return (
    <div>
      <Popover
        placement="bottom"
        visible={modalSearh}
        content={
          <SearchContainer
            loading={loading}
            endPointCloth={{
              value: debouncedValue
            }}
            setLoading={setLoading}
            modalSearch={setModalSearch}
            renderAll={searchRender}
          />
        }
        overlayClassName="dropdownSearch"
        arrowPointAtCenter={false}
        style={{ width: '400px' }}
      >
        <Input
          placeholder="Buscar"
          onBlur={e => {
            if (e.relatedTarget?.className === 'ant-collapse-header') {
              return;
            }
            setTimeout(() => {
              setModalSearch(false);
            }, 150);
          }}
          onFocus={() => {
            if (value === '') {
              setModalSearch(false);
              return;
            }
            setModalSearch(true);
          }}
          onChange={handleChange}
          className={`custom-input  custom-searchinput`}
          suffix={<SearchOutlined />}
        />
      </Popover>
    </div>
  );
};
