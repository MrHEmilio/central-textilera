import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, InputNumber } from 'antd';
import { FC, useEffect, useState } from 'react';

interface Props {
  // eslint-disable-next-line no-unused-vars
  priceLen: (len: number) => void;
  // eslint-disable-next-line no-unused-vars
  handelCheck: (index: number) => void;
  formV: any;
}

export const PricesGroup: FC<Props> = ({ priceLen, handelCheck, formV }) => {
  const [hInput, setHinput] = useState<number[]>([]);
  const [checked, setChecked] = useState(false);
  const [indx, setIndex] = useState<number>();
  useEffect(() => {
    if (!formV) return;
    if (formV) {
      priceLen(formV.length);
      formV.map((val: any, i: number) => {
        if (val === undefined) {
          return;
        }
        const hiddenInput = hInput;
        if (val?.lastAmountRange === undefined) {
          setChecked(true);
          if (hiddenInput.length > 0) {
            hiddenInput.pop();
            setHinput(hiddenInput);
          }
          hiddenInput.push(i);
          setIndex(i);
        }
      });
    }
  }, [formV?.length]);

  const handleCheckbox = (checked: boolean, i: number) => {
    setIndex(i);
    let hiddenInput = hInput;
    if (checked === true && hiddenInput.length <= 1) {
      setChecked(true);
      hiddenInput = hiddenInput.filter(item => {
        return item === i;
      });
      handelCheck(i);
      hiddenInput.push(i);
      setHinput(hiddenInput);
    } else {
      setChecked(false);
      if (hiddenInput.includes(i)) {
        hiddenInput = hiddenInput.filter(item => {
          return item !== i;
        });
        setHinput(hiddenInput);
        return;
      } else {
        setHinput([i]);
      }
    }
  };
  return (
    <>
      <Form.List
        rules={[
          {
            validator: async (_, names) => {
              if (!names || names.length < 1) {
                return Promise.reject(
                  new Error('Debe agregar al menos un precio')
                );
              }
            }
          }
        ]}
        name="prices"
      >
        {(fields, { add, remove }, { errors }) => (
          <>
            {fields.map((field, index) => (
              <div
                key={field.key}
                className="grid items-center gap-4 md:grid-cols-7"
              >
                <Form.Item name="order" label="Orden">
                  {field.name + 1}
                </Form.Item>
                <Form.Item
                  label="Desde"
                  name={[field.name, 'firstAmountRange']}
                  rules={[
                    {
                      required: true,
                      message: 'Ingrese un valor, por favor'
                    }
                    /* ({ getFieldValue }) => (
                        {
                          validator: async () => {
                            const { firstAmountRange, lastAmountRange } = getFieldValue('prices')[field.name];
                            if (firstAmountRange < lastAmountRange) return Promise.resolve()
                            return Promise.reject(new Error('Hasta no puede ser menor a desde'))
                          }
                        }
                      ) */
                  ]}
                  required
                >
                  <InputNumber
                    min={1}
                    formatter={value =>
                      `${value?.toString().replaceAll(/\D/g, '')}`.replace(
                        /\B(?=(\d{3})+(?!\d))/g,
                        ','
                      )
                    }
                  />
                </Form.Item>
                {index + 1 < fields.length && (
                  <Form.Item
                    label="Hasta"
                    name={[field.name, 'lastAmountRange']}
                    rules={[
                      ({ getFieldValue }) => ({
                        validator: async () => {
                          const { firstAmountRange, lastAmountRange } =
                            getFieldValue('prices')[field.name];
                          if (
                            firstAmountRange < lastAmountRange ||
                            lastAmountRange === null ||
                            lastAmountRange === undefined
                          )
                            return Promise.resolve();
                          return Promise.reject(
                            new Error('Hasta no puede ser menor a desde')
                          );
                        }
                      })
                    ]}
                    required
                  >
                    <InputNumber
                      defaultValue={1}
                      min={1}
                      formatter={value =>
                        `${value?.toString().replaceAll(/\D/g, '')}`.replace(
                          /\B(?=(\d{3})+(?!\d))/g,
                          ','
                        )
                      }
                    />
                  </Form.Item>
                )}
                {index + 1 >= fields.length && (
                  <Form.Item label="A más unidades" className="text-center">
                    <Checkbox
                      disabled={field.name + 1 >= formV.length}
                      checked={index + 1 >= fields.length}
                      onChange={e => handleCheckbox(e.target.checked, index)}
                      className="mb-5"
                    ></Checkbox>
                  </Form.Item>
                )}
                <Form.Item
                  label="Precio unitario"
                  className="col-span-2"
                  name={[field.name, 'price']}
                  rules={[
                    {
                      required: true,
                      message: 'Ingrese un precio, por favor'
                    }
                  ]}
                  required
                >
                  <InputNumber
                    addonBefore={'$'}
                    min={1 as any}
                    addonAfter={'MXN'}
                    // parser={(value) => value!.replace(/\$\s?|(,*)/g, '')}
                    formatter={value =>
                      `${Number(value).toLocaleString('en-US', {
                        maximumFractionDigits: 2
                      })}`
                    }
                    step={0.1}
                  />
                </Form.Item>
                <Form.Item>
                  <MinusCircleOutlined
                    className="col-span-1"
                    onClick={() => {
                      remove(field.name);
                      setHinput(hInput.filter(x => x !== index));
                    }}
                  />
                </Form.Item>
              </div>
            ))}

            <Form.Item>
              <Button
                type="dashed"
                htmlType="button"
                onClick={() => {
                  add({});
                  priceLen(fields.length + 1);
                }}
                block
                icon={<PlusOutlined />}
              >
                Agregar precio
              </Button>
              <Form.ErrorList errors={errors} />
            </Form.Item>
          </>
        )}
      </Form.List>
    </>
  );
};
