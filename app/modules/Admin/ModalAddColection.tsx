import { Button, Form, Input, Modal } from 'antd';
import Error from '/public/img/noImage.jpg';
import React, { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { InputUpload } from '../shared/inputUpload';
import {
  deleteCollection,
  editCollection,
  newCollections,
  reactiveCollection
} from '../../services/collections/collectionService';
import { DataCollection } from '../../interfaces/Response/Admin/Collection';
import { ExclamationCircleOutlined } from '@ant-design/icons';

const { confirm } = Modal;

interface Props {
  onSubmit: () => void;
  modal?: DataCollection;
}

export const ModalAddColection: FC<Props> = ({ onSubmit, modal }) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [disableDelete, setDisableDelete] = useState(false);
  const [edit, setEdit] = useState(false);

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
    const { name, image } = value;

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
      const payload = { name, image: rawLog };
      const response = await newCollections(payload);
      if (response) {
        toast.success(`${response.message}`, {
          theme: 'colored'
        });
        onSubmit();
      }
      setLoading(false);
    }, 800);
  };

  const reactivateModal  = async(id: string) =>{
    const response = await reactiveCollection(id);
    if (response) {
      onSubmit();
    }
    setLoading(false);
  }

  const editModal = async (value: any) => {
    const { name } = value;
    const payload = {
      name,

      id: modal?.id || ''
    };
    const response = await editCollection(payload);
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

  const deleteModal = async () => {
    setDeleteLoading(true);
    confirm({
      icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
      content: <h1>¿Desea eliminar esta Colección?</h1>,
      async onOk() {
        const response = await deleteCollection(modal?.id || '');
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
      <div className="mb-6 flex justify-center " id="container-imgbanner">
        {edit && (
          <img
            src={modal?.image}
            alt=""
            className="w-[236px]"
            id="image-preview-edit"
            onError={e => {
              e.currentTarget.onerror = null;
              e.currentTarget.src = Error.src;
            }}
          />
        )}
      </div>
      {!edit && (
        <InputUpload
          label="Imagen"
          name={'image'}
          classNameUpload="ml-2 w-full"
          classNameFormItem="mb-4 w-full colectionUpload-img"
          classNameForPreviewImg="w-[236px]"
        />
      )}

      <Form.Item
        // initialValue={modal?.description}
        label="Nombre"
        name="name"
        required
        className=" mb-4 w-full"
        rules={[{ required: true, message: 'Escriba una colección por favor' }]}
      >
        <Input size="middle" className="ml-[5px] w-[93%]" />
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
