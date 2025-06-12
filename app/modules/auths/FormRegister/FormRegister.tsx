import { Button, Form, Input, Select } from 'antd';
import { useForm } from 'antd/lib/form/Form';
import { FC, useEffect, useRef, useState } from 'react';
import ReCAPTCHA from 'react-google-recaptcha';
import { useGetRequest } from '../../../Hooks/useGetRequest';
import { useReady } from '../../../Hooks/useReady';
import { useUserInfo } from '../../../Hooks/useUserInfo';
import { PaginationResponse } from '../../../interfaces/paginationResponse';
import { CreateUser } from '../../../interfaces/Request/Client/CreateUser';
import { CountryCode } from '../../../interfaces/Response/Admin/Client';
import { OrderByEnum } from '../../../models/Enums';
import {
  LASTNAME_REGEX,
  NAME_REGEX,
  RFC_REGEX
} from '../../../models/RegularExpression';
import { BillingRegimenFisalResponse } from '../../../services/Billing/billing-responses';
import { getCountryCode } from '../../../services/Client';
import { removeAccents } from '../../../services/utils';
import { CardInfo, InputPassword } from '../../shared';
import { InputEmail } from '../../shared/InputEmail/InputEmail';
import { LinkCtx } from '../../shared/Link/Link';
import PhoneInputMask from '../../shared/PhoneInputMask/PhoneInputMask';

interface Props {
  // eslint-disable-next-line no-unused-vars, @typescript-eslint/no-explicit-any
  onRegister: (arg0: CreateUser, arg1: any) => void;
  loading: boolean;
  fill?: boolean;
}

export interface RegisterUserForm {
  name: string;
  firstLastname: string;
  secondLastname?: string;
  rfc?: string;
  fiscalRegimen?: string;
  companyName?: string;
  phone: string;
  email: string;
  password?: string;
  countryCode?: string;
}

export const FormRegister: FC<Props> = ({ onRegister, loading, fill }) => {
  const key = process.env.NEXT_PUBLIC_RECAPTCHA_KEY;
  
  const userinfo = useUserInfo();
  const [msgCaptcha, setMsgCaptcha] = useState(false);
  const [fiscalRegimens, fiscalRegimensLoading] = useGetRequest<
    BillingRegimenFisalResponse[]
  >('billing/fiscal/regimen');
  const isready = useReady();
  const [codes, loadingCodes] = useGetRequest<
    PaginationResponse<CountryCode[]>
  >(
    `/catalog/country/code?&active=true&page=1&size=1000&columSort=name&typeSort=${OrderByEnum.asc}`
  );

  const [form] = useForm();
  const captcha = useRef<ReCAPTCHA>(null);
  const onChange = () => {
    if (captcha?.current?.getValue()) {
      setMsgCaptcha(false);
    }
  };

  useEffect(() => {
    if (!isready) return;
    getMx();
  }, [isready]);

  useEffect(() => {
    if (!isready) return;
    if (!userinfo || !fill) return;
    const {
      name,
      firstLastname,
      secondLastname,
      rfc,
      fiscalRegimen,
      companyName,
      phone,
      email,
      countryCode
    } = userinfo.info!;
    const frm: RegisterUserForm = {
      name,
      firstLastname,
      secondLastname: secondLastname || '',
      rfc,
      fiscalRegimen,
      companyName,
      phone,
      email,
      countryCode: countryCode?.id || ''
    };
    form.setFieldsValue(frm);
  }, [userinfo, isready]);

  const onFinish = (values: RegisterUserForm) => {
    values.name = values.name.trim();
    values.secondLastname = values.secondLastname?.trim();
    values.firstLastname = values.firstLastname.trim();
    values.companyName = values.companyName?.trim();
    values.phone = values.phone?.trim().replaceAll(' ', '');

    if (!fill && captcha?.current?.getValue()) {
      setMsgCaptcha(false);
      onRegister(values as CreateUser, captcha);
    } else {
      setMsgCaptcha(true);
    }

    if (fill) {
      onRegister(values as CreateUser, captcha);
    }
  };

  const getMx = async () => {
    const resp = await getCountryCode('México');
    if (!resp) return;
    form.setFieldsValue({ countryCode: resp.content[0].id });
  };

  const prefixSelector = (
    <Form.Item
      name="countryCode"
      noStyle
      rules={[{ required: true, message: 'Debe seleccionar una opcion' }]}
    >
      {!codes ? (
        <p>Cargando...</p>
      ) : (
        <Select
          className="min-w-[5rem]"
          showSearch
          disabled={loadingCodes}
          loading={loadingCodes}
          filterOption={(input, option) => {
            const opt = removeAccents(
              (option?.children ?? '').toString().toLocaleLowerCase()
            );
            return opt.includes(removeAccents(input.toString().toLowerCase()));
          }}
        >
          {loadingCodes ? (
            <Select.Option>Cargando...</Select.Option>
          ) : (
            codes?.content?.map(code => (
              <Select.Option key={code.id} value={code.id} className="">
                ({code.code}) {code.name}
              </Select.Option>
            ))
          )}
        </Select>
      )}
    </Form.Item>
  );

  return (
    <CardInfo title="">
      <Form
        layout="vertical"
        form={form}
        onFinish={onFinish}
        autoComplete="off"
      >
        {!userinfo && (
          <div className="conatiner-forgot">
            <LinkCtx href={'/auths/login'}>
              <p style={{ marginTop: '1rem' }}>
                ¿Eres cliente?
                <span className="forgot-password">{`  Inicia sesión →`}</span>
              </p>
            </LinkCtx>
          </div>
        )}
        <Form.Item
          label="Nombre"
          name="name"
          rules={[
            {
              required: true,
              message: 'Escriba un nombre por favor'
            },
            {
              pattern: new RegExp(NAME_REGEX),
              message: 'Escriba un nombre válido'
            }
          ]}
        >
          <Input className="custom-input" size="large" />
        </Form.Item>

        <Form.Item
          label="Primer apellido"
          name="firstLastname"
          rules={[
            { required: true, message: 'Escriba un apellido por favor' },
            {
              pattern: new RegExp(NAME_REGEX),
              message: 'Escriba un apellido válido'
            }
          ]}
        >
          <Input className="custom-input" size="large" />
        </Form.Item>

        <Form.Item
          label="Segundo apellido"
          name="secondLastname"
          rules={[
            {
              pattern: new RegExp(LASTNAME_REGEX),
              message: 'Escriba un apellido válido'
            }
          ]}
        >
          <Input className="custom-input" size="large" />
        </Form.Item>

        <Form.Item
          label="RFC"
          name="rfc"
          rules={[
            {
              pattern: new RegExp(RFC_REGEX),
              message: 'Este RFC no es válido'
            }
          ]}
        >
          <Input className="custom-input uppercase" size="large" />
        </Form.Item>

        <Form.Item label="Denominación/razón social" name="companyName">
          <Input className="custom-input " size="large" />
        </Form.Item>

        <Form.Item label="Régimen fiscal" name="fiscalRegimen">
          <Select
            disabled={fiscalRegimensLoading}
            showSearch
            filterOption={(input, option) => {
              const opt = removeAccents(
                (option?.children ?? '').toString().toLocaleLowerCase()
              );
              return opt.includes(
                removeAccents(input.toString().toLowerCase())
              );
            }}
          >
            {fiscalRegimens?.map(reg => (
              <Select.Option key={reg.Name} value={reg.Value}>
                {reg.Name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        {
          <PhoneInputMask
            initialValue={userinfo?.info?.phone}
            addonBefore={prefixSelector}
          />
        }

        <InputEmail
          disable={!!userinfo}
          label={'Correo electrónico'}
          name={'email'}
          required={true}
        />
        {!fill && <InputPassword label={'Contraseña'} name={'password'} />}

        {!fill && (
          <Form.Item
            extra={
              msgCaptcha ? (
                <p className="mt-2 text-center text-red-600">
                  Seleccione el captcha
                </p>
              ) : (
                <></>
              )
            }
          >
            <div className="mt-4 flex justify-center">
              <ReCAPTCHA ref={captcha} sitekey={key!} onChange={onChange} />
            </div>
          </Form.Item>
        )}

        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            className="button-ctx mt-3 w-full"
            loading={loading}
          >
            {!fill ? 'Crear Cuenta' : 'Guardar'}
          </Button>
        </Form.Item>
      </Form>
    </CardInfo>
  );
};
