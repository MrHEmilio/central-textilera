import { Col, Form } from 'antd';
import Input from 'antd/lib/input/Input';
import TextArea from 'antd/lib/input/TextArea';
import Image from 'next/image';
import { FC } from 'react';
import { Cloth } from '../../interfaces/Response/Cloth/Cloth';
import { InputUpload } from '../shared/inputUpload';
import { FormBillingSection } from './FormClothBillingSection';

type Props = {
  imgCloth: string | undefined;
  clothToEdit: Cloth | undefined;
  handleImgChange: () => void;
};
export const FormClothSection: FC<Props> = ({
  imgCloth,
  clothToEdit,
  handleImgChange
}) => {
  return (
    <>
      <Col>
        <Form.Item
          label="Nombre"
          name="name"
          rules={[
            {
              required: true,
              message: 'Escriba un nombre por favor'
            }
          ]}
        >
          <Input className="custom-input" size="large" />
        </Form.Item>
      </Col>

      <Col>
        <Form.Item
          label="Descripción"
          name="mainDescription"
          required
          rules={[
            {
              required: true,
              message: 'Escriba una descripción por favor'
            }
          ]}
        >
          <TextArea rows={3} />
        </Form.Item>
      </Col>
      <Col className="flex flex-col justify-center">
        {imgCloth && (
          <div className="flex h-[10rem] w-full justify-center">
            <div className="relative flex aspect-square max-h-[10rem] w-full justify-center ">
              <Image
                layout="fill"
                objectFit="contain"
                objectPosition="center"
                src={imgCloth || '/img/noImage.jpg'}
              />
            </div>
          </div>
        )}
          
          <InputUpload
            label={'Imagen'}
            classNameFormItem="image-cloth-form"
            required={clothToEdit?.id && clothToEdit.id.length > 0 ? false : true}
            handleChange={handleImgChange}
            classNameUpload=""
            name={'image'}
          />
      </Col>
      <FormBillingSection
        label={'Código de Producto'}
        nameSearch={['billing', 'search']}
        nameSelect={['billing', 'productCode']}
        urlEndpoint="/billing/product/code"
      />
      <FormBillingSection
        label={'Código de Unidad'}
        nameSearch={['billing', 'searchUnit']}
        nameSelect={['billing', 'unitCode']}
        urlEndpoint="/billing/unit/code"
      />
    </>
  );
};
