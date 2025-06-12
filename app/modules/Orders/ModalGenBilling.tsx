import { Button, Form, Input, Select } from 'antd';
import { FC, useEffect, useRef, useState } from 'react';
import { toast } from 'react-toastify';
import { useGetRequest } from '../../Hooks/useGetRequest';
import { CreateUser } from '../../interfaces/Request/Client/CreateUser';
import { RFC_REGEX } from '../../models/RegularExpression';
import { BillingRegimenFisalResponse } from '../../services/Billing/billing-responses';
import { BillingService } from '../../services/Billing/billing-service';
import { verifyClientExists } from '../../services/Client';
import PaymentFormAddressGoogle from '../shared/PaymentFormAddress/PaymentFormAddressGoogle';
import { PaymentFormAddressGoogleT } from '../shared/PaymentFormAddress/PaymentFormAddressGoogle.types';
import { getAllClientAddress } from '../../services/Client/address/addressService';
import { NewAddress } from '../../interfaces/Request/Client/Address';
import { DirectionCard } from '../Directions';

interface Props {
  rfc?: string;
  order: string;
  onSubmit: () => void;
  client?: CreateUser & {
    id: string;
    countryCode: { code: string; id: string; name: string };
    phone: string;
  };
}

export const ModalGenBilling: FC<Props> = ({
  rfc,
  order,
  onSubmit,
  client
}) => {
  const addressComponentRef = useRef<PaymentFormAddressGoogleT | null>(null);
  const [form] = Form.useForm();
  const billingService = new BillingService();
  const [rfcs, setRfc] = useState(rfc);
  const [loading, setLoading] = useState(false);
  const [address, setaddress] = useState<any>();
  const [addressList, setAddressList] = useState<NewAddress[]>([]);
  const [locationSelected, setLocationSelected] = useState<NewAddress>();
  const [cfdi, loadingCfdi] = useGetRequest<any>(
    `/billing/cfdi/use`,
    !!rfcs,
    `rfc=${rfcs}`
  );
  const [fiscalRegimens, fiscalRegimensLoading] = useGetRequest<
    BillingRegimenFisalResponse[]
  >('billing/fiscal/regimen');

  const verifyUserInfo = async (rfc: string) => {
    if (!client) return;
    const { email, phone, countryCode } = client;
    const r = await verifyClientExists(email, countryCode.id, phone, rfc);
    if (!r) return;
    if (!r.ok) {
      toast.error(
        `No podrá continuar el proceso porque los siguientes datos se encuentran registrados en otra cuenta: ${r.dataWrong.join(
          ', '
        )}`,
        { theme: 'colored', autoClose: false, closeButton: true }
      );
      return false;
    }
    return true;
  };

  const onFinish = async (values: any) => {
    addressComponentRef.current?.onFinish();
    setLoading(true);
    if (client) {
      delete values?.state;
      delete values?.alcaldia;
      form.validateFields(['rfc', 'companyName', 'fiscalRegimen', 'cfdiUse']);
      const formV = form.getFieldsValue();
      const r = await verifyUserInfo(formV.rfc);
      if (!r) {
        setLoading(false);
        return;
      }
      const {
        city,
        country,
        latitude,
        longitude,
        municipality,
        numExt,
        numInt,
        references,
        state,
        streetName,
        suburb,
        zipCode
      } = address;
      const req = {
        order: order,
        cfdiUse: formV.cfdiUse,
        rfc: formV.rfc,
        fiscalRegimen: formV.fiscalRegimen,
        companyName: formV.companyName,
        billingAddress: {
          streetName: streetName,
          numExt: numExt,
          numInt: numInt,
          suburb: suburb,
          zipCode: zipCode,
          municipality: municipality,
          city: city,
          state: state,
          country: country,
          references: references,
          latitude: latitude,
          longitude: longitude
        }
      };

      const response = await billingService.BillingWithoutAccount(req);
      if (response) {
        onSubmit();
      } else {
        setLoading(false);
      }
    } else {
      if(locationSelected){
        const response = await billingService.generateBilling({
          order: order,
          cfdiUse: values.cfdiUse,
          // address: locationSelected
        });
        if (response) {
          onSubmit();
        }
        setLoading(false);
      }
    }
  };

  const getCfdi = async () => {
    await form.validateFields(['rfc']);
    const formV = form.getFieldsValue();
    setRfc(formV.rfc);
  };

  const getAddress = (address: any) => {
    setaddress(address);
  };

  const getClientAddresses = async () => {
    const res = await getAllClientAddress();
    if (!res) return;
    setAddressList(res.content);
    const defaultDir = res.content.find(i => i.predetermined == true) || undefined;
    form.setFieldsValue({
      ...form.getFieldsValue,
      addressSelection: defaultDir?.id || null
    });
    const location = res.content.find(x => x.id === defaultDir?.id);
    setLocationSelected(location);
  };
  useEffect(() => {
    if (rfc) {
      getClientAddresses();
    }
  }, []);

  return (
    <Form
      form={form}
      onFinish={onFinish}
      scrollToFirstError
      layout="vertical"
      className="max-h-[50vh] overflow-y-auto "
    >
      {client && (
        <div>
          <Form.Item
            label="RFC"
            name="rfc"
            rules={[
              {
                pattern: new RegExp(RFC_REGEX),
                message: 'Este RFC no es válido',
                required: true
              }
            ]}
          >
            <Input
              className="custom-input uppercase"
              size="large"
              onBlur={getCfdi}
            />
          </Form.Item>
          <Form.Item
            label="Denominación/razón social"
            name="companyName"
            rules={[
              {
                required: true,
                message:
                  'Se necesita su Denominación/razón social para poder continuar'
              }
            ]}
          >
            <Input className="custom-input " size="large" />
          </Form.Item>
          <Form.Item
            label="Régimen fiscal"
            name="fiscalRegimen"
            rules={[
              {
                required: true,
                message: 'Se necesita su Régimen fiscal para poder continuar'
              }
            ]}
          >
            <Select disabled={fiscalRegimensLoading}>
              {fiscalRegimens?.map(reg => (
                <Select.Option key={reg.Value} value={reg.Value}>
                  {reg.Name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
        </div>
      )}
      <Form.Item
        name={'cfdiUse'}
        rules={[
          {
            required: true,
            message: 'Se necesita CFDI para continuar'
          }
        ]}
        label={'Uso de CFDI'}
      >
        <Select
          size="large"
          loading={loadingCfdi}
          disabled={rfcs ? false : true}
        >
          {cfdi?.map((fr: any) => (
            <Select.Option key={fr.Name + 'fr'} value={fr.Value}>
              {fr.Name}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      {!client && (
        <Form.Item name="addressSelection" className="w-full pt-5">
          <Select
            className={`selection`}
            onChange={idLocation => {
              const location = addressList.find(x => x.id === idLocation);
              setLocationSelected(location);
            }}
            placeholder="Seleccione una dirección"
          >
            <Select.Option value={null}>
              <div className="flex h-full items-center justify-center">
                <p>Seleccione una dirección</p>
              </div>
            </Select.Option>
            {addressList.map(i => (
              <Select.Option value={i.id} key={i.id}>
                <DirectionCard
                  className="border-none"
                  controls={false}
                  direction={i}
                />
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      )}
      {client && (
        <PaymentFormAddressGoogle
          ref={addressComponentRef}
          showSubmitButton={false}
          back={false}
          onSubmit={value => {
            getAddress(value);
          }}
          hideEnterprise={true}
        />
      )}

      <div>
        <div className="flex justify-center">
          <Button
            type="primary"
            className="button-ctx mr-0 mt-2"
            htmlType="submit"
            loading={loading}
          >
            Aceptar
          </Button>
        </div>
      </div>
    </Form>
  );
};
