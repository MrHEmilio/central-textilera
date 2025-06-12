import { Col, Form, Input, Select } from 'antd';
import { FC, useState } from 'react';
import { useGetRequest } from '../../Hooks/useGetRequest';
import { REQUIRED_FIELD } from '../../models/constants';
import { BillingProductCode } from '../../services/Billing/billing-responses';
import { GoogleIcon } from '../shared';

type Props = {
  label: string;
  nameSearch: string[];
  nameSelect: string[];
  urlEndpoint: string;
};
export const FormBillingSection: FC<Props> = ({
  label,
  nameSearch,
  nameSelect,
  urlEndpoint
}) => {
  const [search, setSearch] = useState(`search=textil`);
  const [callInitial, setcallInitial] = useState(false);
  const [codes, loadingCodes] = useGetRequest<BillingProductCode[]>(
    urlEndpoint,
    callInitial,
    search
  );
  const placeHolder=()=>{
    if(label.includes('nidad')){
      return 'Ejemplo: Metro'
    }
    return 'Ejemplo: Fibra o Poliéster'
  }
  return (
    <>
      <Col className="grid grid-cols-3 gap-2">
        <Form.Item
          rules={[
            {
              required: true,
              message: 'No aparecerán códigos'
            },
            {
              min: 3,
              message: 'Al menos tres caracteres'
            }
          ]}
          label={label.includes('nidad') ? 'Se vende por' : 'Composición'}
          required
          name={nameSearch}
        >
          <Input
            minLength={3}
            prefix={<GoogleIcon className="text-sm font-bold" icon="search" />}
            value={search}
            onChange={({ target }) => {
              const { value } = target as { value: string };
              const tm = setTimeout(() => {
                if (value.length <= 3) return;
                setcallInitial(true);
                setSearch(`search=${value}`);
              }, 1000);
              return () => clearTimeout(tm);
            }}
            placeholder={placeHolder()}
          />
        </Form.Item>
        <Form.Item
          className="col-span-2"
          name={nameSelect}
          label={label}
          rules={[{ required: true, message: REQUIRED_FIELD(label) }]}
          required
        >
          <Select disabled={loadingCodes} loading={loadingCodes}>
            {codes?.map(pcode => (
              <Select.Option key={pcode.Value} value={pcode.Value}>
                {pcode.Value} = {pcode.Name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </Col>
    </>
  );
};
