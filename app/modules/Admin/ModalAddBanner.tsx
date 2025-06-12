import { Button, Form, InputNumber, Modal } from 'antd';
import TextArea from 'antd/lib/input/TextArea';
import React, { FC, useEffect, useState } from 'react';
import { InputUpload } from '../shared/inputUpload';
import { toast } from 'react-toastify';
import { DataBanner } from '../../interfaces/Response/Admin/Banner';
import Error from '/public/img/noImage.jpg';
import {
  deleteBanner,
  editBanner,
  newBanner,
  reactiveBanner
} from '../../services/banners/bannersService';
import { ExclamationCircleOutlined } from '@ant-design/icons';

const { confirm } = Modal;
interface Props {
  onSubmit: () => void;
  modal?: DataBanner;
}

export const ModalAddBanner: FC<Props> = ({ onSubmit, modal }) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [disableDelete, setDisableDelete] = useState(false);
  const [edit, setEdit] = useState(false);
  const [imgPath, setImgPath] = useState('');

  const onFinish = async (value: any) => {
    let rawLog: any;
    setLoading(true);
    if (edit && modal?.active) {
      editModal(value);
      return;
    }
    if (edit && !modal?.active){
      reactivateModal(modal?.id || '')
      return;
    }
    const { description, image, waitTime } = value;
    const reader = new FileReader();
    reader.onload = e => {
      if (e.target) {
        image[0].base64 = e.target.result;
      }
    };
    reader.readAsDataURL(image[0].originFileObj);

    reader.onload = function () {
      rawLog = reader.result;
    };
    setTimeout(async () => {
      const payload = { description, image: rawLog, waitTime };
      const response = await newBanner(payload);
      if (response) {
        toast.success(`${response.message}`, {
          theme: 'colored'
        });
        onSubmit();
      }
      setLoading(false);
    }, 800);
  };

  const deleteModal = async () => {
    setDeleteLoading(true);
    confirm({
      icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
      content: <h1>¿Desea eliminar este banner?</h1>,
      async onOk() {
        const response = await deleteBanner(modal?.id || '');
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

  const editModal = async (value: any) => {
    const { description, waitTime } = value;
    const payload = {
      description,
      id: modal?.id || '',
      waitTime
    };
    const response = await editBanner(payload);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      onSubmit();
    }

    setLoading(false);
  };

  const reactivateModal = async (id: string) => {
    await reactiveBanner(id);
    onSubmit();
    setLoading(false);
  }

  useEffect(() => {
    if (modal) {
      if (!modal.active) {
        setDisableDelete(true);
      } else {
        setDisableDelete(false);
      }
      form.resetFields();
      form.setFieldsValue({
        description: modal.description,
        waitTime: modal.waitTime
      });
      setImgPath(modal.image);
      setEdit(true);
    }
  }, [modal]);

  const onError = () => {
    setImgPath(Error.src);
  };

  return (
    <Form onFinish={onFinish} form={form}>
      <div className="mb-6 flex justify-center" id="container-imgbanner">
        {edit && (
          <img src={imgPath} alt="" onError={onError} className="max-h-60" />
        )}
      </div>
      {!edit && <InputUpload label="Banner" name={'image'} />}
      <Form.Item
        // initialValue={modal?.description}
        label="Descripción"
        name="description"
        required
        rules={[
          { required: true, message: 'Escriba una descripción por favor' }
        ]}
      >
        <TextArea rows={4} />
      </Form.Item>
      <Form.Item
        label="Tiempo (s)"
        name="waitTime"
        required
        className="ml-[8px]"
        rules={[{ required: true, message: 'Ingrese un valor por favor' }]}
      >
        <InputNumber min={1} max={30} />
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
