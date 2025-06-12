import { Form, Input } from 'antd';
import { useForm } from 'antd/lib/form/Form';
import { useRouter } from 'next/router';
import { Dispatch, FC, SetStateAction, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import {
  LASTNAME_REGEX,
  NAME_REGEX,
  NUM_REGEX_LONG10
} from '../../../models/RegularExpression';
import { getCountryCode, verifyClientExists } from '../../../services/Client';
import {
  SetNotRegisterUserContact,
  SetNotRegisterUserContactValid,
  SetNotRegisterUserValidateStatus,
  setValidatedUser
} from '../../../services/redux/actions/NotRegisterUserActions';
import { CtxDispatch, CtxSelector } from '../../../services/redux/store';
import { PhonePrefix } from '../../shared/PhonePrefix/PhonePrefix';

interface Props {
  setEmail: Dispatch<SetStateAction<string>>;
}

export const PaymentFormUserNotRegistered: FC<Props> = ({ setEmail }) => {
  const notRegisteredUserState = CtxSelector(s => s.notRegisterUser);
  const { validate, userContact } = notRegisteredUserState;
  const [nestedProp] = useState('user');
  const [form] = useForm();
  const router = useRouter();
  const { isReady } = router;
  const dispatch = CtxDispatch();

  const onFinish = async (val: any) => {
    const userContact = { ...val.user, countryCode: val.countryCode };
    dispatch(SetNotRegisterUserContact(userContact));
    dispatch(SetNotRegisterUserContactValid(true));
    const r = await verifyClientExists(
      userContact.email,
      userContact.countryCode,
      userContact.phone
    );
    if (!r) return;
    dispatch(setValidatedUser(r.ok));
    if (!r.ok) {
      toast.error(
        `No podrá continuar el proceso porque los siguientes datos se encuentran registrados en otra cuenta: ${r.dataWrong.join(
          ', '
        )}`,
        {
          theme: 'colored',
          autoClose: false,
          closeButton: true
        }
      );
      router.replace('/cart');
    }
  };

  useEffect(() => {
    if (userContact) return;
    getMx();
  }, [userContact]);

  useEffect(() => {
    if (!isReady) return;
    if (!userContact) return;
    form.setFieldsValue({
      user: {
        name: userContact.name,
        lastName: userContact.lastName,
        secondLastName: userContact.secondLastName,
        email: userContact.email,
        phone: userContact.phone
      },
      countryCode: userContact.countryCode
    });
  }, [isReady, userContact]);

  useEffect(() => {
    if (!validate) return;
    form
      .validateFields()
      .then(() => {
        // dispatch(SetNotRegisterUserValidateStatus(true));
        form.submit();
      })
      .catch(() => {
        // dispatch(SetNotRegisterUserAddressValid(false));
        dispatch(SetNotRegisterUserValidateStatus(false));
      });
  }, [validate]);

  const getMx = async () => {
    const resp = await getCountryCode('México');
    if (resp) form.setFieldsValue({ countryCode: resp.content[0].id });
  };
  return (
    <>
      <Form
        layout="vertical"
        className='md:grid-cols-3 grid-cols-1 md:grid gap-2 w-full'
        form={form}
        onFinish={onFinish}
        onChange={() => {
          const user = form.getFieldValue('user');
          const regex =
            /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;
          setEmail('');
          if (regex.test(user?.email)) {
            setEmail(user.email);
          }
        }}
      >
        {/* names */}
        {/* <div className="grid grid-cols-1 gap-2 sm:grid-cols-2 md:grid-cols-3"> */}
        <Form.Item
          label="Nombre"
          required
          name={[nestedProp, 'name']}
          rules={[
            { required: true, message: 'Este campo es requerido' },
            {
              pattern: new RegExp(NAME_REGEX),
              message: 'Escriba un nombre válido'
            }
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Primer apellido"
          name={[nestedProp, 'lastName']}
          required
          rules={[
            { required: true, message: 'Este campo es requerido' },
            {
              pattern: new RegExp(LASTNAME_REGEX),
              message: 'No se admiten caracteres especiales'
            }
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Segundo apellido"
          name={[nestedProp, 'secondLastName']}
          rules={[
            {
              pattern: new RegExp(LASTNAME_REGEX),
              message: 'No se admiten caracteres especiales'
            }
          ]}
        >
          <Input />
        </Form.Item>
        {/* </div> */}
        {/* names */}
        <Form.Item
          className="md:col-span-full"
          label="Correo"
          name={[nestedProp, 'email']}
          rules={[
            { required: true, message: 'Este campo es requerido' },
            {
              type: 'email',
              message: 'El correo debe ser parecido a ejemplo@dominio.com'
            }
          ]}
          required
        >
          <Input type="email" />
        </Form.Item>
        <Form.Item
          className="md:col-span-full"
          name={[nestedProp, 'phone']}
          rules={[
            { required: true, message: 'Este campo es requerido' },
            { pattern: new RegExp(NUM_REGEX_LONG10), message: 'Solo números' }
          ]}
          label="Teléfono"
          required
        >
          <Input maxLength={10} addonBefore={<PhonePrefix />} type="tel" />
        </Form.Item>
      </Form>
    </>
  );
};
