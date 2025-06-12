import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { Button, Form, Input } from 'antd';
import { FC } from 'react';

interface Props {
  // eslint-disable-next-line no-unused-vars
  itemsLen: (v: number) => void;
}
export const DetailsCloth: FC<Props> = ({ itemsLen }) => {
  return (
    <>
      <Form.List
        name="descriptions"
        rules={[
          {
            validator: async (_, names) => {
              if (!names || names.length < 1) {
                return Promise.reject(
                  new Error('Debe agregar al menos una característica')
                );
              }
            }
          }
        ]}
      >
        {(fields, { add, remove }, { errors }) => (
          <>
            {fields.map(field => (
              <div key={field.key} className="grid grid-cols-6 items-center">
                <Form.Item
                  {...field}
                  className="col-span-5"
                  name={[field.name, 'description']}
                  rules={[
                    {
                      required: true,
                      message: 'Agrege una característica'
                    }
                  ]}
                  required
                >
                  <Input autoFocus />
                </Form.Item>

                <MinusCircleOutlined
                  className="col-span-1"
                  onClick={() => remove(field.name)}
                />
              </div>
            ))}
            <Form.Item>
              <Button
                type="dashed"
                htmlType="button"
                onClick={() => {
                  add();
                  itemsLen(fields.length + 1);
                }}
                block
                icon={<PlusOutlined />}
              >
                Agregar Característica
              </Button>
              <Form.ErrorList errors={errors} />
            </Form.Item>
          </>
        )}
      </Form.List>
    </>
  );
};
