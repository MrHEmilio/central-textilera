import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Upload } from 'antd';
import { RcFile, UploadChangeParam, UploadFile } from 'antd/lib/upload';
import React, { FC } from 'react';
import { toast } from 'react-toastify';

interface Props {
  classNameUpload?: string;
  classNameFormItem?: string;
  classNameForPreviewImg?: string;
  label: string;
  name: string;
  required?: boolean;
  // eslint-disable-next-line no-unused-vars
  handleChange?: (file: UploadChangeParam<UploadFile<any>>) => void;
}

export const InputUpload: FC<Props> = ({
  label,
  name,
  classNameUpload = 'ml-8 w-full',
  classNameFormItem = 'uploadInput mb-4 w-full',
  classNameForPreviewImg = '',
  handleChange,
  required = true
}) => {
  const getFile = (e: any) => {
    if (Array.isArray(e)) {
      return e;
    }

    onPreview(e);
    return e && e.fileList;
  };

  const beforeUpload = (file: any) => {
    const arrayImages = ['image/png', 'image/jpg', 'image/jpeg'];
    const typeFile = file.type;
    if (!arrayImages.includes(typeFile)) {
      toast.error(`La imagen debe ser .png .jpg o .jepg`, { theme: 'colored' });

      return arrayImages.includes(typeFile) || Upload.LIST_IGNORE;
    }
  };

  const onPreview = async (e: any) => {
    const { file, fileList } = e;
    let src = file.url as string;

    if (fileList.length === 0) {
      const elemento = document.getElementById('container-imgbanner');
      const elementoChild = document.getElementById('image-preview');
      elemento?.removeChild(elementoChild as any);
      return;
    }

    if (!src) {
      src = await new Promise(resolve => {
        const reader = new FileReader();
        reader.readAsDataURL(file.originFileObj as RcFile);
        reader.onload = () => resolve(reader.result as string);
      });
    }
    const image = new Image();
    image.src = src;
    const elemento = document.getElementById('container-imgbanner');
    if (elemento && image) {
      image.id = 'image-preview';
      image.className = classNameForPreviewImg;
      elemento.innerHTML = image.outerHTML || '<img />';
    }
  };

  return (
    <Form.Item
      label={label}
      valuePropName="fileList"
      name={name}
      required
      getValueFromEvent={getFile}
      rules={
        required
          ? [{ required: true, message: 'Seleccione una imagen por favor' }]
          : []
      }
      className={classNameFormItem}
    >
      <Upload
        // multiple={false}
        className={classNameUpload}
        onChange={v => {
          if (handleChange) {
            handleChange(v);
          }
        }}
        maxCount={1}
        listType="picture"
        beforeUpload={beforeUpload}
        // onPreview={onPreview}
        customRequest={({ onSuccess }) =>
          setTimeout(() => {
            if (onSuccess) {
              onSuccess('ok', undefined);
            }
          }, 0)
        }
      >
        <div>
          <Button className="w-full" icon={<UploadOutlined />}>
            Elige una imagen
          </Button>
        </div>
      </Upload>
    </Form.Item>
  );
};
