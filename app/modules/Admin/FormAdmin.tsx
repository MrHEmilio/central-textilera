import { Button, Form, Input } from 'antd';
import React, { FC, useState, useEffect } from 'react';
import { NAME_REGEX, LASTNAME_REGEX } from '../../models/RegularExpression';
import { CardInfo, InputEmail } from '../shared';
import { CreateRequestAdmin } from '../../interfaces/Request/Admin/AdminResponseAdmin';
import { InfoAdmin } from '../../interfaces/Response/Admin/AdminResposeAdmin';
import { useRouter } from 'next/router';

interface Props {
  // eslint-disable-next-line no-unused-vars, @typescript-eslint/no-explicit-any
  onSubmit: (arg0: CreateRequestAdmin, setLoading: any) => void;
  onDelete?: () => void;
  admin?: InfoAdmin | null | undefined;
  back?: boolean;
  deleteButton?: boolean;
}

export const FormAdmin: FC<Props> = ({
  onSubmit,
  admin,
  onDelete,
  back = true,
  deleteButton = true
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const navigate = useRouter();
  const [edit, setEdit] = useState(false);
  const [disableDelete, setDisableDelete] = useState(false);

  const onFinish = (values: CreateRequestAdmin) => {
    setLoading(true);
    onSubmit(values, setLoading);
  };

  const Delete = async () => {
    if (onDelete) onDelete();
  };

  useEffect(() => {
    if (admin) {
      setEdit(true);
      form.setFieldsValue(admin);
      setDisableDelete(admin.active ? false : true);
    }
  }, [admin]);

  return (
    <CardInfo title="">
      <Form
        layout="vertical"
        onFinish={onFinish}
        autoComplete="off"
        form={form}
      >
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

        <InputEmail
          label={'Correo electrónico'}
          name={'email'}
          required={true}
          disable={edit}
        />
        {/* <InputPassword label={'Contraseña'} name={'password'} /> */}

        <Form.Item>
          {!edit && (
            <div className="flex justify-end">
              <Button
                type="primary"
                onClick={() => navigate.back()}
                className="button-ctx mt-3 w-[25%]"
                disabled={loading}
              >
                Regresar
              </Button>
              <Button
                type="primary"
                htmlType="submit"
                className="button-ctx mt-3  "
                loading={loading}
              >
                Crear Administrador
              </Button>
            </div>
          )}
          {edit && (
            <div className="flex justify-end">
              {deleteButton && (
                <Button
                  type="primary"
                  className="button-ctx-delete mt-3 w-[25%] "
                  onClick={Delete}
                  disabled={disableDelete}
                >
                  Desactivar
                </Button>
              )}
              {back && (
                <Button
                  type="primary"
                  onClick={() => navigate.back()}
                  className="button-ctx mt-3 w-[25%]"
                  disabled={loading || disableDelete}
                >
                  Regresar
                </Button>
              )}
              <Button
                type="primary"
                htmlType="submit"
                className="button-ctx mt-3 w-[25%]"
                loading={loading}
              >
                {disableDelete ? 'Activar' : 'Editar'}
              </Button>
            </div>
          )}
        </Form.Item>
      </Form>
    </CardInfo>
  );
};
