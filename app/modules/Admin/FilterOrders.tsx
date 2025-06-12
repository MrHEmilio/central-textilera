import { Select } from 'antd';
import React, { FC } from 'react';
import { CatalogStatus } from '../../interfaces/Response/Catalog/Catalog';

const { Option } = Select;

interface Props {
  options: CatalogStatus[];
  labelStr: string;
  defaultValue?: string;
  value?: string;
  className?: string;
  optionSelect?: boolean;
  labelDefault?: string;
  // eslint-disable-next-line no-unused-vars, @typescript-eslint/no-explicit-any
  onChange: (arg0: any) => void;
}
export const FilterOrders: FC<Props> = ({
  options,
  onChange,
  labelStr,
  defaultValue = '',
  value = '',
  className = 'mr-6',
  optionSelect = true,
  labelDefault = 'Selecciona'
}) => {
  return (
    <div className={`${className} flex flex-col`}>
      <label className="mr-4 font-bold text-black">{labelStr}</label>
      <Select
        onChange={onChange}
        className="min-w-[10rem]"
        defaultValue={defaultValue}
        value={value}
      >
        {optionSelect && (
          <Option value={defaultValue ? 'ALWAYS' : ''}>{labelDefault}</Option>
        )}

        {options.map(status => (
          <Option value={status.key} key={status.key}>
            {status.label}
          </Option>
        ))}
      </Select>
    </div>
  );
};
