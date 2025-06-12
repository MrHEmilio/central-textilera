import { Form, Select } from 'antd';
import { FC, useEffect, useState } from 'react';
import { CountryCode } from '../../../interfaces/Response/Admin/Client';
import { getCountryCode } from '../../../services/Client';
import { removeAccents } from '../../../services/utils';

export const PhonePrefix: FC = () => {
  const { Option } = Select;
  const [codes, setCodes] = useState<CountryCode[]>();
  const getCodes = async () => {
    const resp = await getCountryCode();
    if (resp) setCodes(resp.content);
  };

  useEffect(() => {
    getCodes();
  }, []);

  return (
    <Form.Item
      name="countryCode"
      noStyle
      rules={[{ required: true, message: 'Debe seleccionar una opcion' }]}
    >
      <Select
        className="min-w-[5rem]"
        showSearch
        filterOption={(input, option) => {
          const opt = removeAccents(
            (option?.children ?? '').toString().toLocaleLowerCase()
          );
          return opt.includes(removeAccents(input.toString().toLowerCase()));
        }}
      >
        {codes?.map(code => (
          <Option key={code.id} value={code.id} className="">
            ({code.code}) {code.name}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );
};
