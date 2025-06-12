import { Col, Form, Input, InputNumber } from 'antd';
import { FC } from 'react';
import { FormBillingSection } from './FormClothBillingSection';

export const FormClothSectionSampler: FC = () => {
  return (
    <>
      <Col>
        <Form.Item
          name="samplerDescription"
          label="Descripción"
          rules={[
            {
              required: true,
              message: 'Ingrese la descripción, por favor'
            }
          ]}
        >
          <Input.TextArea />
        </Form.Item>
      </Col>
      <Col className="grid grid-cols-2 gap-4">
        <Col>
          <Form.Item
            name="samplerPrice"
            label="Precio"
            rules={[
              { required: true, message: 'Ingrese el precio, por favor' }
            ]}
          >
            <InputNumber
              addonBefore={'$'}
              min={1}
              addonAfter={'MXN'}
              formatter={value =>
                `${value?.toString().replaceAll(/\D/g, '')}`.replace(
                  /\B(?=(\d{3})+(?!\d))/g,
                  ','
                )
              }
            />
          </Form.Item>
        </Col>
        <Col>
          <Form.Item
            name="samplerAmount"
            rules={[
              {
                required: true,
                message: 'Ingrese una cantidad, por favor'
              }
            ]}
            label="Cantidad"
          >
            <InputNumber
              min={1}
              className="w-full"
              formatter={value =>
                `${value?.toString().replaceAll(/\D/g, '')}`.replace(
                  /\B(?=(\d{3})+(?!\d))/g,
                  ','
                )
              }
            />
          </Form.Item>
        </Col>
      </Col>

      <FormBillingSection
        label={'Código de Producto'}
        nameSearch={['billingSampler', 'search']}
        nameSelect={['billingSampler', 'productCode']}
        urlEndpoint="/billing/product/code"
      />
      <FormBillingSection
        label={'Código de Unidad'}
        nameSearch={['billingSampler', 'searchUnit']}
        nameSelect={['billingSampler', 'unitCode']}
        urlEndpoint="/billing/unit/code"
      />
    </>
  );
};
