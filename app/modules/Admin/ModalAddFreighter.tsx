import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Button, Form, Input, Modal } from 'antd';
import { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { deleteFreighter, editFreighter, newFreighter, reactivateFreighter } from '../../services/admin/freighter';
import { InputUpload } from '../shared/inputUpload';
const { confirm } = Modal;

interface Props {
  onSubmit: () => void;
  modal?: any;
}

export const ModalAddFreighter: FC<Props> = ({ onSubmit, modal }) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [edit, setEdit] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [disableDelete, setDisableDelete] = useState(false);

  const onFinish = async (value: any) => {
    setLoading(true);
    if (edit && modal?.active) {
      editModal(value);
      return;
    }
    if (edit && !modal?.active) {
      reactivateModal(modal?.id || '');
      return;
    }
    const { name, image, costPerDistance, costPerWeight } = value;
    const payload = {
      name,
      image: image[0].thumbUrl,
      costPerDistance,
      costPerWeight
    };
    const response = await newFreighter(payload);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }
    setLoading(false);
  };

  const editModal = async (value: any) => {
    const { name, costPerDistance, costPerWeight } = value;
    const payload = {
      name,
      costPerDistance,
      costPerWeight,
      id: modal?.id || ''
    };
    const response = await editFreighter(payload);
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
      content: <h1>¿Desea desactivar esta fletera?</h1>,
      async onOk() {
        const response = await deleteFreighter(modal?.id || '');
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
    await reactivateFreighter(id);
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
        form.setFieldsValue({
          name: modal.name,
          costPerDistance: modal.costPerDistance,
          costPerWeight: modal.costPerWeight
        });
      setEdit(true);
    }
  }, [modal]);

  return (
    <Form form={form} onFinish={onFinish}>
      <Form.Item
        label="Nombre"
        name="name"
        required
        className=" mb-4 w-full"
        rules={[{ required: true, message: 'Escriba el nombre de la fletera' }]}
      >
        <Input size="small" />
      </Form.Item>
      <Form.Item
        label="Costo por distancia (Km)"
        name="costPerDistance"
        required
        className=" mb-4 w-full"
        rules={[{ required: true, message: 'Escriba el Costo por distancia' }]}
      >
        <Input size="small" />
      </Form.Item>
      <Form.Item
        label="Costo por peso (Kg)" 
        name="costPerWeight"
        required
        className=" mb-4 w-full"
        rules={[
          { required: true, message: 'Escriba el Costo por peso' }
        ]}
      >
        <Input size="small" />
      </Form.Item>
      {!edit && <InputUpload label="Imagen" name={'image'} />}
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
