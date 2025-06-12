import { Button, Form } from 'antd';
import { useRouter } from 'next/router';
import { useState } from 'react';
import { toast } from 'react-toastify';
import { changePassword } from '../../../services/email/email';
import { CardInfo, InputEmail } from '../../shared';
import { LinkCtx } from '../../shared/Link';

export const FormForgotPassword = () => {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const onFinish = async (values: any) => {
    setLoading(true);
    const resp = await changePassword(values.email);
    if (resp) {
      toast.success(resp.message, { theme: 'colored' });
      router.push('/auths/login');
    }
    setLoading(false);
  };

  return (
    <CardInfo title="">
      <Form
        layout="vertical"
        onFinish={onFinish}
        // onFinishFailed={onFinishFailed}
        autoComplete="off"
        requiredMark={false}
      >
        <p className="mb-6">
          Le enviaremos un correo electrónico para restablecer su contraseña.
        </p>

        <InputEmail
          label={'Correo electrónico'}
          name={'email'}
          required={true}
        />

        <Form.Item>
          <div className="container-actions">
            <Button
              type="primary"
              htmlType="submit"
              className="button-ctx response-forgot mt-4 w-36"
              loading={loading}
            >
              Enviar
            </Button>
            <LinkCtx href={`/auths/login`}>
              <a className="button-link-ctx link-forgotpassword mt-4 w-36">
                Cancelar
              </a>
            </LinkCtx>
          </div>
        </Form.Item>
      </Form>
    </CardInfo>
  );
};
