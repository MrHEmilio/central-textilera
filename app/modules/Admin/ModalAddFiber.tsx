import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Button, Form, Input, Modal } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import {
  newFiber,
  editFiber,
  deleteFiber,
  reactivateFiber
} from '../../services/admin/fibers';

const { confirm } = Modal;

interface Props {
  onSubmit: () => void;
  modal?: any;
}

export const ModalAddFiber: FC<Props> = ({ onSubmit, modal }) => {
  const [form] = Form.useForm();
  const [edit, setEdit] = useState(false);
  const [loading, setLoading] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [disableDelete, setDisableDelete] = useState(false);

  const onFinish = async (value: any) => {
    setLoading(true);
    if (edit && modal?.active) {
      editModal(value);
      return;
    }
    if (edit && !modal?.active) {
      reactivateModal(modal.id || '');
      return;
    }
    const response = await newFiber(value);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }

    setLoading(false);
  };

  const deleteModal = async () => {
    setDeleteLoading(true);
    confirm({
      icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
      content: <h1>¿Desea eliminar esta fibra?</h1>,
      async onOk() {
        const response = await deleteFiber(modal?.id || '');
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
      },
      cancelText: 'Cancelar'
    });
  };

  const reactivateModal = async (id: string) => {
    await reactivateFiber(id);
    onSubmit();
    setLoading(false);
  };

  const editModal = async (value: any) => {
    const { name } = value;
    const payload = { name, id: modal?.id || '' };
    const response = await editFiber(payload);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }
    setLoading(false);
  };

  useEffect(() => {
    if (modal) {
      if (!modal.active) {
        setDisableDelete(true);
      } else {
        setDisableDelete(false);
      }
      form.resetFields();
      form.setFieldsValue({ name: modal.name });

      setEdit(true);
    }
  }, [modal]);

  return (
    <Form form={form} onFinish={onFinish}>
      <Form.Item
        // initialValue={modal?.description}
        label="Fibra"
        name="name"
        required
        className=" mb-4 w-full"
        rules={[{ required: true, message: 'Escriba el nombre de la fibra' }]}
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
