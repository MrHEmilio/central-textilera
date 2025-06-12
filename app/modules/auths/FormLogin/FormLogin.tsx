import { useEffect, useState } from 'react';
import { Button, Form } from 'antd';
import { CardInfo, InputEmail, InputPassword } from '../../shared';
import { LinkCtx } from '../../shared/Link/Link';
import { authRedirect, authService } from '../../../services/auth/authService';
import { CtxDispatch } from '../../../services/redux/store';
import { SessionActions } from '../../../services/redux/actions/SessionActions';
import { toast } from 'react-toastify';

export const FormLogin = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const dispatch = CtxDispatch();

  const onFinish = async (values: any) => {
    setLoading(true);
    const response = await authService({
      username: values.email,
      password: values.password
    });
    if (response.redirect) {
      const res = await authRedirect(response.redirect);
      if (res) {
        dispatch(SessionActions(res));
        localStorage.removeItem('notRegisterUser');
        return;
      }
    } else {
      toast.error(response.message, {
        theme: 'colored'
      });
    }
    setTimeout(() => {
      setLoading(false);
    }, 600);
  };

  useEffect(() => {
    return () => {
      setLoading(false);
    };
  }, []);

  return (
    <CardInfo title="">
      <Form
        layout="vertical"
        onFinish={onFinish}
        // onFinishFailed={onFinishFailed}
        autoComplete="off"
        requiredMark={false}
      >
        <div className="conatiner-forgot">
          <LinkCtx href={'/auths/register'}>
            <p style={{ marginTop: '1rem' }}>
              ¿Cliente nuevo?
              <span className="forgot-password">{`  Regístrate`}</span>
            </p>
          </LinkCtx>
        </div>
        <InputEmail
          label={'Correo electrónico'}
          name={'email'}
          required={true}
        />

        <InputPassword label={'Contraseña'} name={'password'} />

        <div className="conatiner-forgot">
          <LinkCtx href={'/auths/forgot-password'}>
            <p className="forgot-password">¿Olvidaste tu contraseña?</p>
          </LinkCtx>
        </div>
        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            className="button-ctx w-full"
            loading={loading}
          >
            Iniciar sesión
          </Button>
        </Form.Item>
      </Form>
    </CardInfo>
  );
};
