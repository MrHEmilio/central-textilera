import { Form, Input, Select } from 'antd';
import { useForm } from 'antd/lib/form/Form';
import Link from 'next/link';
import React, { FC, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useGetRequest } from '../../Hooks/useGetRequest';
import { useReady } from '../../Hooks/useReady';
import { RFC_REGEX } from '../../models/RegularExpression';
import {
  BillingRegimenFisalResponse,
  CfdiCatalogResponse
} from '../../services/Billing/billing-responses';
import { verifyClientExists } from '../../services/Client';
import {
  saveCartOnStorage,
  setCfdiCart
} from '../../services/redux/actions/CartActions';
import {
  SetNotRegisterUserBillingInof,
  SetNotRegisterUserFiscalRegimen,
  setValidatedUser
} from '../../services/redux/actions/NotRegisterUserActions';
import { CtxDispatch, ReduxStore } from '../../services/redux/store';
import { debounceInputChange, getUserInfo } from '../../services/utils';

type Props = {
  rfc?: string;
  // eslint-disable-next-line no-unused-vars
  cfdiSelectionChange: (cfdi: CfdiCatalogResponse) => void;
};
export const BillingCfdiSelect: FC<Props> = ({ rfc, cfdiSelectionChange }) => {
  const dispatch = CtxDispatch();
  const [fiscalRegimens, fiscalRegimensLoading] = useGetRequest<
    BillingRegimenFisalResponse[]
  >('billing/fiscal/regimen', true);
  const [localrfc, setlocalrfc] = useState<string>();
  const [companyNameS, setcompanyNameS] = useState('');
  const [form] = useForm();
  const isready = useReady();
  const userInfo = getUserInfo();
  const [cfdiCat, loadingCfdi] = useGetRequest<CfdiCatalogResponse[]>(
    `/billing/cfdi/use`,
    isready,
    localrfc
  );

  const rfcOnBlur = async () => {
    const userInfo = getUserInfo();
    if (userInfo) return;
    await form.validateFields(['rfc']);
    const { notRegisterUser } = ReduxStore.getState();
    const { userContact } = notRegisterUser;
    const val = form.getFieldValue('rfc');
    if (userInfo) return;
    setlocalrfc(`rfc=${val}`);

    dispatch(SetNotRegisterUserBillingInof(val, companyNameS));

    if (!userContact) return;
    const validation = await verifyClientExists(
      userContact.email,
      userContact.countryCode,
      userContact.phone,
      val
    );

    if (!validation) return;
    dispatch(setValidatedUser(validation.ok));
    if (!validation.ok) {
      toast.error(
        `Los datos ya se encuentran registrados en otra cuenta: ${validation.dataWrong.join(
          ', '
        )}`,
        { theme: 'colored', autoClose: false, closeButton: true }
      );
    }
  };
  const companyNameOnBlur = async () => {
    await form.validateFields(['companyName']).catch(() => {
      Promise.reject('El campo razón social es obligatorio');
    });
    const val = form.getFieldValue('companyName');

    if (userInfo) return;
    setcompanyNameS(val);
    dispatch(
      SetNotRegisterUserBillingInof(localrfc?.replace('rfc=', '') || '', val)
    );
  };

  const handleInputRfc = (e: React.ChangeEvent<HTMLInputElement>)=>{
    const {name, value} = (e.target);
    inputChange(name,value);
    debounceInputChange(1000, () => rfcOnBlur())
  };

  const handleInputCompany = (e: React.ChangeEvent<HTMLInputElement>)=>{
    const {name, value} = (e.target);
    inputChange(name,value);
    debounceInputChange(1000, () => companyNameOnBlur())
  }

  const inputChange = (name: string, value: string)=>{
    form.setFieldsValue({[name]: value.toUpperCase()})
  }

  useEffect(() => {
    if (!isready) return;
    if (!rfc) return;

    setlocalrfc(`rfc=${rfc}`);
  }, [rfc, isready]);

  useEffect(() => {
    if (!isready) return;
    if (isready && userInfo?.info?.rfc) {
      form.setFieldsValue({
        rfc: userInfo.info.rfc,
        companyName: userInfo.info.companyName
      });
      setlocalrfc(`rfc=${userInfo.info.rfc}`);
      setcompanyNameS(userInfo.info.companyName || '');
    }
  }, [isready]);

  if (!rfc && userInfo != undefined) {
    return (
      <div className="my-4">
        <p className="text-lg text-red-400">
          No tenemos registrada su información fiscal (RFC, Razón social, CFDI)
        </p>
        <p>
          lo puede registrar{' '}
          <Link className="text-main underline" href={'/fiscal-info'}>
            aquí
          </Link>
        </p>
      </div>
    );
  }
  return (
    <>
      <Form form={form}>
        <h4 className="text-2xl font-bold">Información de facturación</h4>
        <div className="grid">
          <p>
            <span className="text-red-400">*</span> RFC
          </p>
          <Form.Item
            name={'rfc'}
            rules={[
              {
                required: true,
                message: 'Se necesita su RFC para poder continuar'
              },
              {
                pattern: new RegExp(RFC_REGEX),
                message: 'Verifique su RFC'
              }
            ]}
            
          >
            <Input
              name='rfc'
              disabled={!!userInfo?.info?.rfc}
              onChange={handleInputRfc}
              size="large"
              required
              
            />
          </Form.Item>
        </div>
        <div>
          <p>
            <span className="text-red-400">*</span> Denominación/razón social
          </p>
          <Form.Item
            name={'companyName'}
            rules={[
              {
                required: true,
                message: 'Se necesita su razón social para poder continuar'
              }
            ]}
          >
            <Input
              name='companyName'
              disabled={!!userInfo?.info?.companyName}
              onChange={handleInputCompany}
              size="large"
              required
            />
          </Form.Item>
        </div>
        {!userInfo && (
          <div>
            <p>
              <span className="text-red-400">*</span> Régimen Fiscal
            </p>
            <Form.Item
              name={'fiscalRegimen'}
              rules={[
                {
                  required: true,
                  message: 'Se necesita su razón social para poder continuar'
                }
              ]}
            >
              <Select
                size="large"
                onSelect={(v: string) => {
                  dispatch(SetNotRegisterUserFiscalRegimen(v));
                }}
                loading={fiscalRegimensLoading}
              >
                {fiscalRegimens?.map(fr => (
                  <Select.Option key={fr.Name + 'fr'} value={fr.Value}>
                    {fr.Name}
                  </Select.Option>
                ))}
              </Select>
            </Form.Item>
          </div>
        )}
      </Form>

      <Form.Item required>
        <p className="mb-2">
          <span className="text-red-400">*</span> Seleccione su uso de CFDI
        </p>
        <Select
          size="large"
          onChange={v => {
            const r = cfdiCat.find(x => x.Value === v);
            if (!r) return;
            dispatch(setCfdiCart(r));
            dispatch(saveCartOnStorage());
            cfdiSelectionChange(r);
          }}
          loading={loadingCfdi}
        >
          {cfdiCat?.map(cfdi => (
            <Select.Option
              value={cfdi.Value}
              key={'cfdi-selector' + cfdi.Value}
            >
              {cfdi.Name}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
    </>
  );
};
