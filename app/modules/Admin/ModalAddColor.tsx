import { Button, Form, Input, Modal } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { ChromePicker } from 'react-color';
import { toast } from 'react-toastify';
import {
  newColor,
  editColor,
  deleteColor,
  reactivateColor
} from '../../services/admin/colors';
import { DataColors } from '../../interfaces/Response/Admin/Colors';
import { ExclamationCircleOutlined } from '@ant-design/icons';

const { confirm } = Modal;
interface Props {
  onSubmit: () => void;
  modal?: DataColors;
}

export const ModalAddColor: FC<Props> = ({ onSubmit, modal }) => {
  const [form] = Form.useForm();
  const [color, setColor] = useState('#48B18D');
  const [disableDelete, setDisableDelete] = useState(false);
  const [edit, setEdit] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);

  const [loading, setLoading] = useState(false);

  const onFinish = async (value: any) => {
    setLoading(true);
    value.code = color;
    if (edit && modal?.active) {
      editModal(value);
      return;
    }
    if (edit && !modal?.active) {
      reactivateModal(modal?.id || '');
      return;
    }
    const response = await newColor(value);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }

    setLoading(false);
  };

  const onChangeColor = (e: any) => {
    setColor(e.hex);
  };
  const editModal = async (value: any) => {
    const { name, code } = value;
    const payload = {
      name,
      code,
      id: modal?.id || ''
    };
    const response = await editColor(payload);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }
    setLoading(false);
  };

  const reactivateModal = async (id: string) => {
    await reactivateColor(id);
    onSubmit();
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
      setColor(modal.code);

      setEdit(true);
    }
  }, [modal]);

  const deleteModal = async () => {
    setDeleteLoading(true);
    confirm({
      icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
      content: <h1>¿Desea eliminar este color</h1>,
      async onOk() {
        const response = await deleteColor(modal?.id || '');
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

  return (
    <Form onFinish={onFinish} form={form}>
      <Form.Item
        label="Nombre"
        name="name"
        required
        className=" mb-4 w-full"
        rules={[{ required: true, message: 'Escriba un nombre por favor' }]}
      >
        <Input size="middle" className=" " />
      </Form.Item>
      <Form.Item label="Color" name="code" required className="ml-2 mb-4">
        <div className="flex justify-center pt-3">
          <ChromePicker
            disableAlpha={true}
            onChange={onChangeColor}
            color={color}
          />
        </div>
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
              className="button-ctx-delete "
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
