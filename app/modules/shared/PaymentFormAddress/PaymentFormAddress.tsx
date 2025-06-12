import {
  Button,
  Checkbox,
  Col,
  Form,
  Input,
  Row,
  Select,
  Skeleton
} from 'antd';

import { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import { NewAddress } from '../../../interfaces/Request/Client/Address';
import { Address } from '../../../interfaces/Response/Client/NewDirection';
import { ZipCode } from '../../../interfaces/Response/Client/ZipCode';
import { getInfoZipCode } from '../../../services/Client/address/addressService';

import { useRouter } from 'next/router';
import { toast } from 'react-toastify';
import {
  NAME_REGEX,
  NUM_REGEX,
  ZIPCODE_REGEX
} from '../../../models/RegularExpression';
import { SetNotRegisterUserAddressValid } from '../../../services/redux/actions/NotRegisterUserActions';
import { CtxDispatch, CtxSelector } from '../../../services/redux/store';
import { getUserInfo } from '../../../services/utils';

export interface PaymentFormAddressRef {
  callSubmit: () => void;
}
interface Props {
  showSubmitBtn?: boolean;
  enterprise?: boolean;
  addDirection?: boolean;
  directionEdit?: Address;
  loading?: boolean;
  autoFill?: boolean;
  // eslint-disable-next-line no-unused-vars
  onSubmit: (arg0: NewAddress, arg1: string) => void;
  back?: boolean;
  simple?: boolean;
  clean?: boolean;
}

const { Option } = Select;

const PaymentFormAdress = forwardRef<PaymentFormAddressRef, Props>(
  (
    {
      enterprise = false,
      addDirection = false,
      onSubmit,
      directionEdit,
      autoFill,
      // eslint-disable-next-line no-unused-vars
      loading,
      back = true,
      simple = false,
      clean = false,
      showSubmitBtn = true
    },
    ref
  ) => {
    const navigate = useRouter();
    const { isReady } = navigate;
    const notRegisteredUserState = CtxSelector(s => s.notRegisterUser);
    const { validate, userAddress } = notRegisteredUserState;
    const [zipCodeLength, setzipCodeLength] = useState(false);
    const [zipCodeResponse, setZipCodeResponse] = useState<ZipCode[]>();
    const [loadingZipCode, setLoadingZipCode] = useState(false);
    const [selectSuburb, setSelectSuburb] = useState('');
    const [trySubmit, settrySubmit] = useState(false);
    const dispatch = CtxDispatch();
    // eslint-disable-next-line no-unused-vars
    // const [idSuburb, setiIdSuburb] = useState('');

    const [form] = Form.useForm();

    const goBack = () => {
      navigate.back();
    };

    const onFinish = (value: NewAddress) => {
      delete value?.state;
      // delete value?.alcaldia;

      const data = { ...value, suburb: selectSuburb };
      onSubmit(data, selectSuburb);
      setSelectSuburb('');
    };

    useImperativeHandle(ref, () => ({
      callSubmit() {
        form.submit();
      }
    }));

    useEffect(() => {
      if (!isReady || !userAddress || clean) return;
      form.setFieldsValue(userAddress);
      searchZipCode({ target: { value: userAddress.zipCode as any} });
      setSelectSuburb(userAddress.suburb || '');
    }, [isReady, userAddress, clean]);

    useEffect(() => {
      if (autoFill) {
        const info = getUserInfo();
        if (!info) return;
        // formAddress.setFieldsValue({ ...formAddress.getFieldsValue, name: info.name })
      }
      if (directionEdit?.address) {
        selectColonia(directionEdit.address.zipCode);
      }
    }, []);

    useEffect(() => {
      if (!validate) return;
      form
        .validateFields()
        .then(() => {
          dispatch(SetNotRegisterUserAddressValid(true));
          settrySubmit(true);
          form.submit();
        })
        .catch(() => {
          // dispatch(SetNotRegisterUserValidateStatus(false));
          dispatch(SetNotRegisterUserAddressValid(false));
        });
    }, [validate]);

    useEffect(() => {
      if (simple && selectSuburb !== '') {
        settrySubmit(true);
        form.submit();
      }
    }, [selectSuburb]);

    const selectColonia = async (zipCode: string) => {
      const response = await getInfoZipCode(zipCode, false);
      if (response) {
        setZipCodeResponse([response]);
        let idNew = '';
        response.suburbs.map(su => {
          su.name === directionEdit?.address?.name ? (idNew = su.id) : 'prueba';
        });
        // setiIdSuburb(idNew);
        setTimeout(() => {
          form.setFieldsValue({
            suburb: idNew
          });
          setSelectSuburb(idNew);
        }, 200);
      }
    };

    const searchZipCode = async (e: { target: { value: string } }) => {
      const zipRegGex = new RegExp(ZIPCODE_REGEX);
      const zipCode = e.target.value;
      const formA = form.getFieldsValue();
      if (zipCode.length === 5 && zipRegGex.test(zipCode)) {
        setSelectSuburb('');
        setzipCodeLength(true);
        setLoadingZipCode(true);
        const response = await getInfoZipCode(zipCode, true).catch(r => {
          toast.error(r.data.message, { theme: 'colored', toastId: 'err-100' });
          return;
        });

        if (response?.status) {
          const mensaje = response?.data.message;
          toast.error(mensaje, { theme: 'colored' });
        } else {
          setZipCodeResponse(response ? [response] : []);
          setTimeout(() => {
            form.setFieldsValue({
              state: response?.state,
              alcaldia: response?.municipality
            });
            setLoadingZipCode(false);
          }, 200);
          // form.setFieldsValue({ ...formA, suburb: 'selectOption' });
        }
      } else {
        setLoadingZipCode(zipCodeLength);
        setSelectSuburb('');
        form.setFieldsValue({ ...formA, suburb: 'selectOption' });
        setZipCodeResponse([]);
        setTimeout(() => {
          form.setFieldsValue({
            state: '',
            alcaldia: '',
            suburb: ''
          });
          setLoadingZipCode(false);
        }, 400);
      }
    };

    const onChangeFormInfo = () => {
      if (!trySubmit || !simple) return;

      form.submit();
    };

    return (
      <div>
        <p className="font-famBold text-lg text-redalert">
          {' '}
          Este componente está deprecado, usar PaymentFormAdressGoogle en vez de
          este{' '}
        </p>
        {!enterprise && !clean && back && (
          <p className="mb-3 text-lg">Dirección de envío</p>
        )}
        <Form
          onFinish={onFinish}
          onChange={onChangeFormInfo}
          layout="vertical"
          form={form}
        >
          <Row className="contact-adress" gutter={[16, 8]}>
            {!simple && (
              <Col sm={24} md={24}>
                <Form.Item
                  initialValue={directionEdit ? directionEdit.name : ''}
                  name="name"
                  label="Nombre"
                  required
                  rules={[
                    {
                      required: true,
                      message: 'El nombre es obligatorio'
                    },
                    {
                      pattern: new RegExp(NAME_REGEX),
                      message: 'Escriba un nombre válido'
                    }
                  ]}
                >
                  <Input
                    size="large"
                    className="custom-input"
                    placeholder="Nombre"
                    value={'abel'}
                  />
                </Form.Item>
              </Col>
            )}

            <Col sm={24} md={12}>
              <Form.Item
                initialValue={directionEdit ? directionEdit.streetName : ''}
                name="streetName"
                label="Calle"
                required
                rules={[
                  {
                    required: true,
                    message: 'La calle es obligatoria.'
                  }
                ]}
              >
                <Input
                  size="large"
                  className="custom-input"
                  placeholder="Calle"
                />
              </Form.Item>
            </Col>
            <Col sm={24} md={6}>
              <Form.Item
                initialValue={directionEdit ? directionEdit.numExt : ''}
                name="numExt"
                label="Número Ext"
                required
                rules={[
                  {
                    required: true,
                    message: 'El número ext. es obligatorio.'
                  },
                  {
                    pattern: new RegExp(NUM_REGEX),
                    message: 'Escriba un número válido'
                  }
                ]}
              >
                <Input
                  size="large"
                  className="custom-input"
                  placeholder="Ext"
                  maxLength={10}
                />
              </Form.Item>
            </Col>
            <Col sm={24} md={6}>
              <Form.Item
                initialValue={directionEdit ? directionEdit.numInt : ''}
                name="numInt"
                label="Número Int"
              >
                <Input className="custom-input" placeholder="Int" />
              </Form.Item>
            </Col>
            {enterprise && (
              <Col sm={24} md={24}>
                <Form.Item
                  name="company"
                  label="Empresa"
                  initialValue={directionEdit ? directionEdit.company : ''}
                >
                  <Input
                    size="large"
                    className="custom-input"
                    placeholder="Empresa"
                  />
                </Form.Item>
              </Col>
            )}
            <Col sm={24} md={8}>
              <Form.Item
                initialValue={
                  directionEdit ? directionEdit.address?.zipCode : ''
                }
                label="Código postal"
                name="zipCode"
                required
                rules={[
                  {
                    required: true,
                    message: 'El código postal es obligatorio.'
                  },
                  {
                    pattern: new RegExp(ZIPCODE_REGEX),
                    message: 'Escriba un número válido'
                  }
                ]}
              >
                <Input
                  size="large"
                  className="custom-input"
                  placeholder="Código postal"
                  maxLength={5}
                  onChange={searchZipCode}
                />
              </Form.Item>
            </Col>
            <Col sm={24} md={8}>
              <Form.Item
                initialValue={directionEdit ? directionEdit.address?.state : ''}
                label="Estado"
                name="state"
                className={` ${loadingZipCode ? 'hidden' : 'block'}`}
                required
                rules={[
                  {
                    required: true,
                    message: 'El Estado es obligatorio.'
                  }
                ]}
              >
                <Input
                  size="large"
                  className={`custom-input ${
                    loadingZipCode ? 'hidden' : 'block'
                  }`}
                  placeholder="Estado"
                  disabled
                  // value={zipCodeResponse?.length ? zipCodeResponse[0].state : ''}
                />
              </Form.Item>
              <Form.Item
                label="Estado"
                required
                className={` ${!loadingZipCode ? 'hidden' : 'block'}`}
              >
                <div>
                  <Skeleton.Input active={true} size={'large'} block={false} />
                </div>
              </Form.Item>
            </Col>
            <Col sm={24} md={24}>
              <Form.Item
                className={` ${loadingZipCode ? 'hidden' : 'block'}`}
                label="Colonia"
                name="suburb"
                required
                rules={[
                  {
                    required: true,
                    message: 'La colonia es obligatorio.'
                  }
                ]}
              >
                <Select
                  placeholder="Colonia"
                  size="large"
                  className={`w-full rounded-md ${
                    loadingZipCode ? 'hidden' : 'block'
                  }`}
                  disabled={zipCodeResponse?.length ? false : true}
                  onChange={e => {
                    setSelectSuburb(e);
                  }}
                  value={selectSuburb}
                >
                  <Option value={''}>Seleccione una opción</Option>
                  {zipCodeResponse?.length &&
                    zipCodeResponse[0].suburbs.map(suburb => (
                      <Option value={suburb.id} key={suburb.id}>
                        {suburb.name}
                      </Option>
                    ))}
                </Select>
              </Form.Item>
              <Form.Item
                label="Colonia"
                required
                className={` ${!loadingZipCode ? 'hidden' : 'block'}`}
              >
                <div>
                  <Skeleton.Input active={true} size={'large'} block={true} />
                </div>
              </Form.Item>
            </Col>
            <Col sm={24} md={24}>
              <Form.Item
                label="Alcaldía/Municipio"
                required
                className={` ${loadingZipCode ? 'hidden' : 'block'}`}
                name="alcaldia"
                initialValue={
                  directionEdit ? directionEdit.address?.municipality : ''
                }
                rules={[
                  {
                    required: true,
                    message: 'La Alcaldía/Municipio es obligatorio.'
                  }
                ]}
              >
                <Input
                  size="large"
                  className={`custom-input ${
                    loadingZipCode ? 'hidden' : 'block'
                  }`}
                  placeholder="Alcaldía/Municipio"
                  disabled
                />
              </Form.Item>
              <Form.Item
                label="Alcaldía/Municipio"
                required
                className={` ${!loadingZipCode ? 'hidden' : 'block'}`}
              >
                <div>
                  <Skeleton.Input active={true} size={'large'} block={true} />
                </div>
              </Form.Item>
            </Col>

            {addDirection ? (
              <>
                <Col sm={24} md={24}>
                  <Form.Item
                    name="predetermined"
                    valuePropName="checked"
                    initialValue={
                      directionEdit ? directionEdit.predetermined : false
                    }
                  >
                    <Checkbox>
                      Establecer como mi dirección predeterminada
                    </Checkbox>
                  </Form.Item>
                </Col>
                <Col sm={24} md={24}>
                  <Form.Item
                    name="billingAddress"
                    valuePropName="checked"
                    initialValue={
                      directionEdit ? directionEdit.billingAddress : false
                    }
                  >
                    <Checkbox>
                      Establecer como mi dirección de facturación
                    </Checkbox>
                  </Form.Item>
                </Col>
              </>
            ) : (
              !simple && (
                <Col sm={24} md={24}>
                  <Form.Item name="predetermined" valuePropName="checked">
                    <Checkbox>Guardar dirección como predeterminado.</Checkbox>
                  </Form.Item>
                </Col>
              )
            )}
          </Row>
          <div className="container-buttonaddDirection mb-8">
            {back && (
              <Button
                type="primary"
                className={`button-ctx clean-button`}
                onClick={goBack}
              >
                Regresar
              </Button>
            )}

            {showSubmitBtn && (
              <Button
                loading={loading}
                htmlType="submit"
                className="button-ctx w-40 bg-main text-white"
              >
                Guardar
              </Button>
            )}
          </div>
        </Form>
      </div>
    );
  }
);

PaymentFormAdress.displayName = 'PaymentFormAdress';
export default PaymentFormAdress;
