import { Form, Input } from 'antd';
import { CSSProperties, FC } from 'react';

interface Props {
  label?: string;
  name: string;
  disable?: boolean;
  required: boolean;
  message?: string;
  placeholder?: string;
  styles?: CSSProperties;
}

export const InputEmail: FC<Props> = ({
  label,
  name,
  required,
  message = 'Por favor, ingresa un correo válido',
  placeholder,
  styles,
  disable = false
}) => {
  return (
    <Form.Item
      label={label}
      name={name}
      rules={[
        {
          type: 'email',
          required,
          message
        }
      ]}
      style={styles}
    >
      <Input
        className="custom-input"
        size="large"
        placeholder={placeholder}
        disabled={disable}
      />
    </Form.Item>
  );
};
