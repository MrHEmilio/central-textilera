import { Row, Col, Form, Input, Button } from 'antd';
import style from './ContactForm.module.css';
import { contactUs } from '../../../services/contact/contact';
import { toast } from 'react-toastify';
import { useState } from 'react';
import {
  NAME_REGEX,
  NUM_REGEX_LONG10
} from '../../../models/RegularExpression';

const { TextArea } = Input;

const CleanForm = () => {
  const form = Form.useFormInstance();
  return (
    <Button
      type="primary"
      className={`button-ctx ${style['clean-button']}`}
      onClick={() => form.resetFields()}
    >
      Limpiar
    </Button>
  );
};

export const ContactForm = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const onFinish = async (values: any) => {
    setLoading(true);
    const response = await contactUs({ ...values });

    if (response) {
      form.resetFields();
      toast.success(`${response.message}`, { theme: 'colored' });
    }

    setLoading(false);
  };

  return (
    <Form form={form} layout="vertical" onFinish={onFinish}>
      <div style={{ widows: '100%' }}>
        <Row gutter={[24, { xs: 0, md: 24 }]}>
          <Col span={12} xs={{ span: 24 }} md={{ span: 12 }}>
            <Form.Item
              name={'name'}
              label={<p className={style.TextBody}>Nombre y Apellido:</p>}
              rules={[
                { required: true, message: 'Por Favor ingresa tu nombre' },
                {
                  pattern: new RegExp(NAME_REGEX),
                  message: 'Escriba un nombre válido'
                }
              ]}
              style={{ marginBottom: '.5rem' }}
            >
              <Input className="custom-input" />
            </Form.Item>
            <Form.Item
              name={'phone'}
              label={<p className={style.TextBody}>Número de Teléfono:</p>}
              rules={[
                {
                  pattern: new RegExp(NUM_REGEX_LONG10),
                  message: 'Escriba un número de teléfono válido'
                }
              ]}
            >
              <Input className="custom-input" />
            </Form.Item>
          </Col>
          <Col span={12} xs={{ span: 24 }} md={{ span: 12 }}>
            <Form.Item
              name={'email'}
              label={<p className={style.TextBody}>Correo electrónico:</p>}
              rules={[
                {
                  type: 'email',
                  required: true,
                  message: 'Por Favor ingresa un correo válido'
                }
              ]}
            >
              <Input className="custom-input" />
            </Form.Item>
          </Col>
        </Row>
      </div>

      <div style={{ marginBottom: '1.5rem' }}>
        <Form.Item
          name={'message'}
          label={<p className={style.TextBody}>Mensaje</p>}
          rules={[{ required: true, message: 'Por Favor ingresa un mensaje' }]}
        >
          <TextArea rows={5} />
        </Form.Item>
      </div>

      <div>
        <div className={style.FormActions}>
          <div className={style.ButtonContainer}>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                className="button-ctx"
                loading={loading}
              >
                Enviar
              </Button>
            </Form.Item>
            <Form.Item>
              <CleanForm />
            </Form.Item>
          </div>
          <div>
            <span className={style.reqired}>* Datos obligatorios</span>
          </div>
        </div>
      </div>
    </Form>
  );
};
