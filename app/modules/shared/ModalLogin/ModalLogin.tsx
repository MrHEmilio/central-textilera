import { Button, Form, Modal } from 'antd';
import Link from 'next/link';
import { FC, useState } from 'react';
import { toast } from 'react-toastify';
import { CtxSelector } from '../../../services/redux/store';
import { changePassLoged } from '../../../services/security/security';
import { InputPassword } from '../InputPassword';

interface ResetPassModal {
  onCancels: () => void;
  visible: boolean;
}

interface ModalLoginProps {
  admin?: boolean;
}

const OpenModals: FC<ResetPassModal> = ({ onCancels, visible }: any) => {
  const [loading, setLoading] = useState(false);

  const onFinish = async (values: any) => {
    setLoading(true);
    const res = await changePassLoged(values.oldPassword, values.newPassword);
    if (res) {
      toast.success(res.message, { theme: 'colored' });
      onCancels();
    }
    setLoading(false);
  };
  return (
    <Modal
      visible={visible}
      title="Cambiar contraseña"
      onCancel={onCancels}
      footer={null}
    >
      <Form layout="vertical" onFinish={onFinish} autoComplete="off">
        <InputPassword
          label={'Contraseña actual'}
          name={'oldPassword'}
          errorLabel={'Introduzca la contraseña actual'}
        />
        <InputPassword
          label={'Nueva contraseña'}
          name={'newPassword'}
          errorLabel={'Introduzca la nueva constraseña'}
        />
        <InputPassword
          label={'Confirmar nueva contraseña'}
          name={'confirmPassword'}
          confirmPass={'newPassword'}
          errorLabel={'Introduzca confirmar contraseña'}
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
    </Modal>
  );
};

export const ModalLogin: FC<ModalLoginProps> = ({ admin }) => {
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const { auth, info = { name: '', firstLastName: '' } } = userInfo;
  const [show, setShow] = useState(false);
  const content = () => {
    const { name, firstLastname } = info;

    if (!auth) {
      return (
        <div style={{ textAlign: 'center' }}>
          <p style={{ fontWeight: '400', color: '#898989', fontSize: '17px' }}>
            Bienvenido a ¡Central Textilera!
          </p>

          <Link href="/auths/login">
            <a className="button-link-ctx">Iniciar sesión</a>
          </Link>
          <Link href="/auths/register">
            <a className="button-link-ctx mb-4">Crear cuenta</a>
          </Link>
        </div>
      );
    }

    return (
      <div style={{ marginTop: '5px', textAlign: 'center' }}>
        <p style={{ fontWeight: '400', color: '#898989', fontSize: '17px' }}>
          Bienvenido{' ¡'}
          <span
            className="font-bold"
            style={{ color: '#006eb2' }}
          >{`${name} ${firstLastname}`}</span>
          <span>!</span>
        </p>
        {admin && (
          <div>
            <Link href="/fiscal-info">
              <a className="button-link-ctx">Mis datos</a>
            </Link>
            <Link href="/orders">
              <a className="button-link-ctx">Mis Pedidos</a>
            </Link>
            <Link href="/directions">
              <a className="button-link-ctx mb-4">Libreta de direcciones</a>
            </Link>
            <button
              onClick={() => {
                setShow(true);
              }}
              className="button-link-ctx mb-4"
            >
              Cambiar contraseña
            </button>
            <OpenModals
              onCancels={() => {
                setShow(false);
              }}
              visible={show}
            />
          </div>
        )}
      </div>
    );
  };
  return <>{content()}</>;
};
