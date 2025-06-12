import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { Button, Form, InputNumber, Select, Space, Typography } from 'antd';
import { FC, useEffect, useState } from 'react';
import { DataColors } from '../../../interfaces/Response/Admin/Colors';
import { getColorAdmin } from '../../../services/admin/colors';
import { removeAccents } from '../../../services/utils';

const { Option } = Select;

interface Props {
  // eslint-disable-next-line no-unused-vars
  colorsTotal?: (colors: DataColors[]) => void;
  // eslint-disable-next-line no-unused-vars
  colorLen?: (len: number) => void;
  colorsFilter?: string[];
}
const ColorPickerForCloth: FC<Props> = ({
  colorsTotal,
  colorLen,
  colorsFilter
}) => {
  useEffect(() => {
    if (!colorOrg || colorOrg.length === 0) return;
    if (colorsFilter && colorsFilter.length > 0) {
      setColor(colorOrg.filter(v => !colorsFilter.includes(v.id)));
    } else {
      setColor(colorOrg);
    }
  }, [colorsFilter]);
  const [color, setColor] = useState<DataColors[]>([]);
  const [colorOrg, setColorOrg] = useState<DataColors[]>([]);

  const getColor = async () => {
    const response = await getColorAdmin(true);
    if (response) {
      setColor(response.content);
      setColorOrg(response.content);
      if (!colorsTotal) return;
      colorsTotal(response.content);
    }
  };

  useEffect(() => {
    getColor();
  }, []);

  return (
    <>
      <Typography.Text className="pb-4">Color</Typography.Text>
      <div className="mt-2">
        <Form.List
          name="variants"
          rules={[
            {
              validator: async (_, variants) => {
                if (!variants || variants.length < 1) {
                  return Promise.reject(new Error('Agregue al menos un color'));
                }
              }
            }
          ]}
        >
          {(fields, { add, remove }, { errors }) => (
            <>
              {fields.map(({ key, name, ...restField }) => (
                <Space key={key} style={{ display: 'flex' }} align="baseline">
                  <Form.Item
                    {...restField}
                    name={[name, 'color']}
                    rules={[{ required: true, message: 'Seleccione un color' }]}
                  >
                    <Select
                      style={{ width: '300px' }}
                      placeholder="Seleccione un color"
                      showSearch
                      filterOption={(input, option) => {
                        const opt = removeAccents((option?.key ?? '').toString().toLocaleLowerCase());
                        return opt.includes(removeAccents(input.toString().toLowerCase()))
                      }}
                    >
                      {color.map(col => (
                        <Option value={col.id} key={col.name}>
                          <div className="flex items-center justify-between">
                            <p>{col.name}</p>
                            <div
                              className="h-5 w-12"
                              style={{ backgroundColor: col.code }}
                            ></div>
                          </div>
                        </Option>
                      ))}
                    </Select>
                  </Form.Item>
                  <Form.Item
                    {...restField}
                    name={[name, 'amount']}
                    rules={[
                      { required: true, message: 'Escriba una cantidad' }
                    ]}
                  >
                    <InputNumber min={1} placeholder="Agregue una cantidad" />
                  </Form.Item>

                  <MinusCircleOutlined onClick={() => remove(name)} />
                </Space>
              ))}
              <Form.Item>
                <Button
                  type="dashed"
                  onClick={() => {
                    add({ amount: 1, color: '' });
                    if (colorLen) colorLen(fields.length + 1);
                  }}
                  block
                  icon={<PlusOutlined />}
                >
                  Agregar Color
                </Button>
                <Form.ErrorList errors={errors} />
              </Form.Item>
            </>
          )}
        </Form.List>
      </div>
    </>
  );
};

export default ColorPickerForCloth;
