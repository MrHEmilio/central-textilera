import { Form, Radio } from 'antd';
import FormItem from 'antd/lib/form/FormItem';
import { FC, useState } from 'react';
import { GoogleIcon } from '../../shared';

export const PaymentFormDeliveryWay: FC = () => {
  const [value, setValue] = useState('shipping');

  return (
    <div>
      <p className="mb-3 text-lg">Forma de entrega</p>
      <Form className="text-graySeparation">
        <FormItem className="m-0 rounded-lg border-[1px] border-graySeparation">
          <Radio.Group
            value={value}
            className="w-full p-0"
            onChange={d => setValue(d.target.value)}
          >
            <Radio value={'shipping'} className="flex items-stretch p-3">
              <div className="flex items-end gap-3">
                <GoogleIcon
                  icon={'local_shipping'}
                  className="text-slate-500"
                />
                <span> Envía</span>
              </div>
            </Radio>
            <hr className="my-3 w-full" />
            <Radio
              value={'personal'}
              className="flex items-stretch border-t-graySeparation p-3"
            >
              <div className="flex items-end gap-3">
                <GoogleIcon icon="storefront" className="text-slate-500" />
                <span> Retiro</span>
              </div>
            </Radio>
          </Radio.Group>
        </FormItem>
      </Form>
    </div>
  );
};
