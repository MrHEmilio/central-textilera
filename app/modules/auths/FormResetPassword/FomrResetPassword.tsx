import { Button, Form } from 'antd';
import { useRouter } from 'next/router';
import { FC, useState } from 'react';
import { toast } from 'react-toastify';
import { resetPassByToken } from '../../../services/security/security';
import { CardInfo, InputPassword } from '../../shared';

interface Props {
  token: string;
}

export const FomrResetPassword: FC<Props> = ({ token }) => {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const onFinish = async (values: any) => {
    if (values.newPassword === values.confirmPassword) {
      setLoading(true);
      const res = await resetPassByToken(token, values.newPassword);
      if (res) {
        toast.success(res.message, { theme: 'colored' });
        router.push('/auths/login');
      }
      setLoading(false);
    }
  };

  return (
    <CardInfo title="">
      <Form layout="vertical" onFinish={onFinish} autoComplete="off">
        <InputPassword label={'Nueva contraseña'} name={'newPassword'} />
        <InputPassword
          label={'Confirmar nueva contraseña'}
          name={'confirmPassword'}
          confirmPass={'newPassword'}
        />
        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            className="button-ctx mt-3 w-full"
            loading={loading}
          >
            Cambiar contraseña
          </Button>
        </Form.Item>
      </Form>
    </CardInfo>
  );
};
