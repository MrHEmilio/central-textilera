import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Button, Form, Input, InputNumber, Modal } from 'antd';
import { FC, useEffect, useState } from 'react';
import { ChromePicker } from 'react-color';
import { toast } from 'react-toastify';
import boxIcon from '../../../public/icons/cajas.svg';
import { LASTNAME_REGEX, NUM_REGEX_TWO_DECIMALS } from '../../models/RegularExpression';
import {
  deleteBox,
  editBox,
  newBox,
  reactivateBox
} from '../../services/admin/box';
const { confirm } = Modal;

interface Props {
  onSubmit: () => void;
  modal?: any;
}

export const ModalAddBox: FC<Props> = ({ onSubmit, modal }) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [edit, setEdit] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [disableDelete, setDisableDelete] = useState(false);
  const [color, setColor] = useState('#48B18D');

  const onFinish = async (value: any) => {
    setLoading(true);
    value.colorCode = color;
    if (edit && modal?.active) {
      editModal(value);
      return;
    }
    if (edit && !modal?.active) {
      reactivateModal(modal?.id || '');
      return;
    }
    const response = await newBox(value);
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
    const { name, width, height, depth, colorCode } = value;
    const payload = {
      name,
      width,
      height,
      depth,
      colorCode,
      id: modal?.id || ''
    };
    const response = await editBox(payload);
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
      content: <h1>¿Desea desactivar esta caja?</h1>,
      async onOk() {
        const response = await deleteBox(modal?.id || '');
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
    await reactivateBox(id);
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
        width: modal.width,
        height: modal.height,
        depth: modal.depth,
        colorCode: modal.colorCode
      });
      setColor(modal.colorCode);
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
        rules={[
          { required: true, message: 'Escriba el nombre de la caja' },
          {
            pattern: new RegExp(LASTNAME_REGEX),
            message: 'Escriba un nombre válido'
          }
        ]}
      >
        <Input size="small" maxLength={30}/>
      </Form.Item>
      <Form.Item
        label="Ancho"
        name="width"
        required
        className=" mb-4 w-full"
        rules={[
          { required: true, message: 'Escriba el ancho de la caja' },
          {
            pattern: new RegExp(NUM_REGEX_TWO_DECIMALS),
            message: 'Ingrese un número válido con hasta dos decimales'
          }
        ]}
      >
        <InputNumber min={1} size="small" className="w-full" maxLength={10} />
      </Form.Item>
      <Form.Item
        label="Altura"
        name="height"
        required
        className=" mb-4 w-full"
        rules={[
          { required: true, message: 'Escriba la altura de la caja' },
          {
            pattern: new RegExp(NUM_REGEX_TWO_DECIMALS),
            message: 'Ingrese un número válido con hasta dos decimales'
          }
        ]}
      >
        <InputNumber min={1} size="small" className="w-full" maxLength={10}/>
      </Form.Item>
      <Form.Item
        label="Profundidad"
        name="depth"
        required
        className=" mb-4 w-full"
        rules={[
          { required: true, message: 'Escriba la profundidad de la caja' },
          {
            pattern: new RegExp(NUM_REGEX_TWO_DECIMALS),
            message: 'Ingrese un número válido con hasta dos decimales'
          }
        ]}
      >
       <InputNumber min={1} size="small" className="w-full" maxLength={10}/>
      </Form.Item>
      <Form.Item label="Color" name="colorCode" required className="ml-2 mb-4">
        <div className=" row-auto flex pt-3">
          <img
            src={boxIcon.src}
            alt="caja"
            className="blo relative left-[-50px] top-[-65px] hidden sm:block"
          />
          <ChromePicker
            className="ml-12"
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
