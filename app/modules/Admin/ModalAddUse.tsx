/* eslint-disable no-unused-vars */
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Button, Form, Input, Modal } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { DataUse } from '../../interfaces/Response/Admin/Use';
import { newUse, editUse, deleteUse } from '../../services/admin/uses';

const { confirm } = Modal;
interface Props {
  onSubmit: () => void;
  use?: DataUse;
  onResponse?: (arg0: any) => void;
}

export const ModalAddUse: FC<Props> = ({ use, onSubmit, onResponse }) => {
  const [form] = Form.useForm();
  const [edit, setEdit] = useState(false);
  const [loading, setLoading] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [disableDelete, setDisableDelete] = useState(false);

  const onFinish = async (value: any) => {
    setLoading(true);
    if (edit) {
      editModal(value);
      return;
    }
    const response = await newUse(value);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
      if (onResponse) {
        onResponse(response.data);
      }
    }

    setLoading(false);
  };

  const deleteModal = async () => {
    setDeleteLoading(true);
    confirm({
      icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
      content: <h1>¿Desea eliminar este uso?</h1>,
      cancelText: 'Cancelar',
      okButtonProps: { type: 'primary', className: 'bg-[#1890ff]' },
      async onOk() {
        const response = await deleteUse(use?.id || '');
        if (response) {
          toast.success(`${response.message}`, {
            theme: 'colored'
          });
          onSubmit();
        }
        setDeleteLoading(false);
      },
      onCancel() {
        setDeleteLoading(false);
      }
    });
  };

  const editModal = async (value: any) => {
    const { name } = value;
    const payload = { name, id: use?.id || '' };
    const response = await editUse(payload);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }
    setLoading(false);
  };

  useEffect(() => {
    if (use) {
      if (!use.active) {
        setDisableDelete(true);
      } else {
        setDisableDelete(false);
      }
      form.resetFields();
      form.setFieldsValue({ name: use.name });

      setEdit(true);
    }
  }, [use]);
  return (
    <Form form={form} onFinish={onFinish}>
      <Form.Item
        // initialValue={modal?.description}
        label="Uso"
        name="name"
        required
        className=" mb-4 w-full"
        rules={[{ required: true, message: 'Escriba un uso por favor' }]}
      >
        <Input size="small" />
      </Form.Item>
      <div className="mt-4 flex justify-end">
        {!edit && (
          <Button
            htmlType="submit"
            type="primary"
            className="button-ctx mr-0 mt-2"
            loading={loading}
          >
            Agregar
          </Button>
        )}
        {edit && (
          <div className="">
            <Button
              type="primary"
              className="button-ctx-delete"
              onClick={deleteModal}
              loading={deleteLoading}
              disabled={disableDelete}
            >
              Desactivar
            </Button>
            <Button
              type="primary"
              className="button-ctx mr-0 mt-2"
              htmlType="submit"
              loading={loading}
            >
              {disableDelete ? 'Activar' : 'Editar'}
            </Button>
          </div>
        )}
      </div>
    </Form>
  );
};
