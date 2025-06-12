import { Col, Form, InputNumber } from 'antd';
import { FC } from 'react';
import { DataTypeSale } from '../../interfaces/Response/Admin/typeSale';

type Props = {
  saleSelected: DataTypeSale | undefined;
};
export const FormClothSectionDimensions: FC<Props> = ({ saleSelected }) => {
  return (
    <>
      <div className="grid grid-cols-1 gap-4 lg:grid-cols-2 xl:grid-cols-3">
        <Col>
          <Form.Item
            name="dimension"
            rules={[
              {
                required: true,
                message: 'Ingrese la dimensión, por favor'
              }
            ]}
            required
            label="Dimensión (cilindro)"
          >
            <InputNumber className="w-full" addonAfter="cm" />
          </Form.Item>
        </Col>
        <Col>
          <Form.Item
            name="width"
            rules={[{ required: true, message: 'Ingrese el ancho, por favor' }]}
            label="Ancho"
          >
            <InputNumber className="w-full" addonAfter="m" />
          </Form.Item>
        </Col>
        <Col>
          <Form.Item
            name="weight"
            rules={[{ required: true, message: 'Ingrese el peso, por favor' }]}
            label="Peso"
          >
            <InputNumber
              addonAfter={
                <p>
                  gr/m<sup>2</sup>
                </p>
              }
              className="w-full"
            />
          </Form.Item>
        </Col>
        {saleSelected?.name === 'Kilo' && (
          <Col>
            <Form.Item
              name="yieldPerKilo"
              rules={[
                {
                  required: true,
                  message: 'Ingrese el rendimiento por kilo, por favor'
                }
              ]}
              label="Rendimiento Por Kilo"
            >
              <InputNumber min={1} addonAfter={<p>m</p>} className="w-full" />
            </Form.Item>
          </Col>
        )}
        {}
        <Col>
          <Form.Item
            name="averagePerRoll"
            rules={[
              {
                required: true,
                message: 'Ingrese el promedio por unidad, por favor'
              }
            ]}
            label={`Cuantos ${
              saleSelected?.name.toLowerCase() || 'Unidad'
            } trae el rollo aprox`}
          >
            <InputNumber
              min={1}
              addonAfter={<p>{saleSelected?.abbreviation}</p>}
              className="w-full"
            />
          </Form.Item>
        </Col>
        <Col>
          <Form.Item
            name={'wxw'}
            label="Peso por metro lineal"
          >
            <InputNumber disabled addonAfter={<p>gr/m</p>} className="w-full" />
          </Form.Item>
        </Col>
      </div>
    </>
  );
};
