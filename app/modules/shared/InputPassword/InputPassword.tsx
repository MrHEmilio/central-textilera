import { Form, Input } from 'antd';
import React, { FC } from 'react';

interface Props {
  label: string;
  name: string;
  required?: boolean;
  message?: string;
  confirmPass?: string;
  errorLabel?: string;
}

export const InputPassword: FC<Props> = ({
  label,
  name,
  required = true,
  message = 'Ingrese un password con una mayúscula un número y un símbolo especial',
  confirmPass,
  errorLabel
}) => {
  return confirmPass ? (
    <Form.Item
      label={label}
      name={name}
      dependencies={[confirmPass]}
      rules={[
        {
          required,
          message: `${errorLabel || 'Intrduzca una contraseña'}`
        },
        {
          pattern: new RegExp(
            '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
          ),
          message
        },
        ({ getFieldValue }) => ({
          validator(_, value) {
            if (!value || getFieldValue(confirmPass) === value) {
              return Promise.resolve();
            }
            return Promise.reject(
              new Error('Ambas contraseñas deben coincidir')
            );
          }
        })
      ]}
    >
      <Input.Password className="custom-input" size="large" />
    </Form.Item>
  ) : (
    <Form.Item
      label={label}
      name={name}
      rules={[
        {
          required,
          message: `${errorLabel || 'Intrduzca una contraseña'}`
        },
        {
          pattern: new RegExp(
            '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'
          ),
          message
        }
      ]}
    >
      <Input.Password className="custom-input" size="large" />
    </Form.Item>
  );
};
