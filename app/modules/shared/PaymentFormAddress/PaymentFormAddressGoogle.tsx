import { Loader } from '@googlemaps/js-api-loader';
import { Button, Checkbox, Form, Input, Select } from 'antd';
import { useRouter } from 'next/router';
import {
  forwardRef,
  useEffect,
  useImperativeHandle,
  useState
} from 'react';

import { NewAddress } from '../../../interfaces/Request/Client/Address';
import { ZIPCODE_REGEX } from '../../../models/RegularExpression';
import { CtxSelector } from '../../../services/redux/store';
import {
  PartialLocationGoogle,
  PaymentFormAddressGoogleT,
} from './PaymentFormAddressGoogle.types';

import type { SelectProps } from 'antd';
import { toast } from 'react-toastify';

type Props = {
  showEnterpriseField?: boolean;
  showSubmitButton?: boolean;
  enterprise?: boolean;
  addDirection?: boolean;
  directionEdit?: NewAddress;
  loading?: boolean;
  autoFill?: boolean;
  // eslint-disable-next-line no-unused-vars
  onSubmit: (newAddress: NewAddress) => void;
  back?: boolean;
  simple?: boolean;
  clean?: boolean;
  hideEnterprise?: boolean;
};

const PaymentFormAddressGoogle = forwardRef<PaymentFormAddressGoogleT, Props>((props, ref) => {
  const { showEnterpriseField,
     back,  showSubmitButton = true, onSubmit,
     // eslint-disable-next-line no-unused-vars
     directionEdit, 
     loading, hideEnterprise = false } = props;

  const [form] = Form.useForm();
  const router = useRouter();
  
  const userInfo: { auth: boolean } = CtxSelector(s => s.session!);

  const [colonias, setColonias] = useState<string[]>([]);
  const [ciudades, setCiudades] = useState<string[]>([]);
  const [estado, setEstado] = useState<string | null>(null);
  const [googleLoaded, setGoogleLoaded] = useState(false);
  const [partialLocation, setpartialLocation] = useState<PartialLocationGoogle>({
    location: { lat: '', lng: '' },
    country: '',
    streetName: ''
  });

  const [anadir, setAnadir] = useState(false);

  const [options, setOptions] = useState<SelectProps["options"]>([]);
  const [optCity, setOptCity] = useState<SelectProps["options"]>([]);

  const goBack = () => {
    router.back();
  };

  useImperativeHandle(ref, () => ({
    onFinish: () => {
      form.submit();
    }
  }));


  const onFinish = (values: {
    name: string;
    location: string;
    numInt: string;
    enterprise: string;
    predetermined: boolean;
    billingAddress: boolean;
    municipality: string;
    references: string;
    city: string;
    state: string;
    zipCode: string;
    numExt: string;
    suburb: string;
    streetName: string;
  }) => {
    const {
      name,
      predetermined,
      billingAddress,
      numInt,
      enterprise,
      // eslint-disable-next-line no-unused-vars
      municipality,
      references,
      zipCode,
      city,
      state,
      numExt,
      suburb,
      streetName
    } = values;

    if (!partialLocation) return;

    const {
      country,
      location // missing pendiente,
    } = partialLocation;
    const newAddress: NewAddress = {
      name,
      predetermined,
      billingAddress,
      numExt,
      city,
      state,
      zipCode: zipCode,
      suburb,
      municipality: city.length > 0 ? city : suburb,
      streetName,
      numInt,
      company: enterprise,
      latitude: Number(location.lat),
      longitude: Number(location.lng),
      country,
      references
    };

    onSubmit(newAddress);
  };

  useEffect(() => {
    const initGoogleMaps = async () => {
      try {
        const loader = new Loader({
          apiKey: process.env.NEXT_PUBLIC_MAPS_KEY || '',
          version: 'weekly',
          libraries: ['places'],
        });

        await loader.importLibrary('maps');
        await loader.importLibrary('places');
        setGoogleLoaded(true);
        console.log('✅ Google Maps API cargado correctamente');
      } catch (error) {
        console.error('❌ Error al cargar Google Maps:', error);
      }
    };

    initGoogleMaps();
  }, []);

  const handleZipCodeChange = async (zipCode: string) => {
    setAnadir(false);
    {/*form.setFieldsValue([{ suburb: '' }]);*/ }
    form.resetFields(['suburb', 'city', 'state', 'streetName', 'numExt', 'numInt', 'references']);

    if (!new RegExp(ZIPCODE_REGEX).test(zipCode)) {
      console.warn('⚠️ Código postal no válido');
      setColonias([]);
      setCiudades([]);
      setEstado(null);
      return;
    }

    if (!googleLoaded) {
      console.warn('⚠️ Google Maps API aún no está listo');
      return;
    }

    const geocoder = new window.google.maps.Geocoder();
    geocoder.geocode(
      { address: zipCode, componentRestrictions: { country: 'MX' } },
      (results, status) => {
        if (status === 'OK' && results && results.length > 0) {
          const result = results[0];
          const components = results[0].address_components;

          const colonias = results[0].postcode_localities || [];
          const suburbs: string[] = [];
          const cities: string[] = components.filter(c =>
            c.types.includes('locality')).map(c => c.long_name);
          const state = components.find(c =>
            c.types.includes('administrative_area_level_1'))?.long_name || null;

          // Buscamos 'neighborhood' y 'sublocality' para obtener colonias
          components.forEach((component) => {
            if (component.types.includes('neighborhood') ||
              component.types.includes('postcode_localities') ||
              component.types.includes('sublocality')) {
              suburbs.push(component.long_name);
            }
          });

          const country = components.find(c =>
            c.types.includes('country'))?.long_name || '';

          // Extraer nombre de calle
          const streetName = components.find(c =>
            c.types.includes('route'))?.long_name || '';

          // Extraer coordenadas (latitud y longitud)
          const location = result.geometry.location;

          //location:JSON.parse(JSON.stringify(location)), (segunda opción)
          setpartialLocation({
            location: { lat: String(location.lat()), lng: String(location.lng()) },
            country,
            streetName
          });

          if (suburbs.length <= 1) {
            setColonias(colonias)
          } else {
            setColonias(suburbs);
          }

          setCiudades(cities);
          setEstado(state);

          if (cities.length === 1) {
            form.setFieldsValue({ city: cities[0] });
          } else {
            setCiudades(colonias);
            form.setFieldsValue({ city: colonias[0] });
          }

          if (state) {
            form.setFieldsValue({ state });
          }

          if (colonias.length > 0) {
            form.setFieldsValue({ suburb: colonias[0] });
          } else {
            form.setFieldsValue({ suburb: suburbs[0] });
          }

        } else {
          toast.warning('⚠️ No se encontraron resultados para el código postal ingresado');
          setColonias([]);
          setCiudades([]);
          suburbs: [];
          setEstado(null);
        }
      }
    );
  };

  useEffect(() => {
    const newOptions = colonias.map((colonia, index) => ({
      value: colonia,
      label: colonia,
      key: index,
    }));
    newOptions.push({ value: '', label: 'Añadir otra ...', key: colonias.length + 1 });
    setOptions(newOptions);
  }, [colonias]);

  useEffect(() => {
    const newCidties = ciudades.map((ciudad, index) => ({
      value: ciudad,
      label: ciudad,
      key: index,
    }));
    setOptCity(newCidties);
  }
    , [ciudades]);

  const handleChangeAnadir = async (value: string) => {
    if (value === '') {
      setAnadir(true);
    }
  }

  const handleStreetChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setpartialLocation(prev => ({
      ...prev,
      streetName: e.target.value
    }));
  };


  return (
    <>
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        onChange={() => {
          if (showSubmitButton === false) {
            setTimeout(() => {
              form.submit();
            }, 300);
          }
        }}
      >
        <div className="grid-cols-1 gap-4 md:grid md:grid-cols-2">

          <Form.Item
            label="Ingresa tu código postal"
            name="zipCode"
            rules={[{ required: true, message: 'Código postal es requerido!' },
            { pattern: new RegExp(ZIPCODE_REGEX), message: 'Estructura de CP: 06060' }]}
          >
            <Input size="large" placeholder="Ejemplo: 06060" onChange={(e) => handleZipCodeChange(e.target.value)} className='custom-input' />

          </Form.Item>
          {userInfo.auth && (
            <Form.Item
              label={'Nombre de la dirección'}
              name="name"
              rules={[{ required: true, message: 'Este campo es requerido' }]}
            >
              <Input size="large" placeholder=" " className="custom-input" />
            </Form.Item>
          )}
          <Form.Item label="Estado/Ciudad" name="state" rules={[{ required: true, message: 'Estado/Ciudad es requerido' }]}>
            <Input className="custom-input" size="large" value={estado || ''} onChange={(e) => setEstado(e.target.value)} disabled />
          </Form.Item>

        </div>
        <div className="grid-cols-1 gap-4 md:grid md:grid-cols-2">
          <Form.Item label="Localidad/Municipio" name="city" rules={[{ required: true, message: 'Localidad/Municipio es requerido' }]}>
            <Select
              id='city'
              size="large"
              showSearch
              allowClear
              disabled={optCity?.length === 0}
              options={optCity}
            />
          </Form.Item>

          <Form.Item
            label="Colonia/Localidad"
            name="suburb"
            rules={[{ required: true, message: 'Colonia/Localidad es requerido' }]}
          >
            {!anadir ? (
              <Select
                id='suburb'
                size="large"
                placeholder="o escriba una colonia"
                options={options}
                showSearch
                allowClear
                disabled={colonias?.length === 0}
                onChange={handleChangeAnadir}
              />
            ) :
              (
                <Input
                  size="large"
                  placeholder="Colonia/Localidad"
                  className="custom-input"
                  onChange={(e) => {
                    form.setFieldsValue({ suburb: e.target.value });
                  }}
                />
              )}
          </Form.Item>
        </div>


        <div className="grid-cols-1 gap-4 md:grid md:grid-cols-2">
          <Form.Item label="Calle" name="streetName" rules={[{ required: true, message: 'El nombre de la calle es requerido!⚠️' }]}>
            <Input size="large" placeholder="Ejemplo: Leona Vicario" className="custom-input"
              value={partialLocation.streetName} // Mantiene sincronizado el estado
              onChange={handleStreetChange}
            />
          </Form.Item>
          <div className="grid-cols-1 gap-4 md:grid md:grid-cols-2">
            <Form.Item label="Número Exterior" name="numExt" rules={[{ required: true, message: 'Número exterior requerido!⚠️' }]}>
              <Input size="large" placeholder="Ejemplo: 110" className="custom-input" />
            </Form.Item>

            <Form.Item label="Número Interior" name="numInt">
              <Input size="large" placeholder="Ejemplo: 110-C" className="custom-input" />
            </Form.Item>
          </div>
        </div>

        {!hideEnterprise && (
            <Form.Item
              hidden={showEnterpriseField}
              label={'Empresa:'}
              name="enterprise"
              className="grid grid-cols-1 gap-4"
            >
              <Input
                size="large"
                className="custom-input"
                disabled={!partialLocation}
              />
            </Form.Item>
          )}

{/*
        <Form.Item label="Referencias extra para encontrar tu domicilio" name="references">
          <Input.TextArea placeholder="Ingrese detalladamente las indicaciones para encontrar tu domicilio" />
        </Form.Item>
*/}

        <Form.Item
            name="predetermined"
            hidden={!userInfo.auth}
            valuePropName="checked"
          >
            <Checkbox>Establecer como mi dirección predeterminada</Checkbox>
          </Form.Item>
          <Form.Item
            name="billingAddress"
            hidden={!userInfo.auth}
            valuePropName="checked"
          >
            <Checkbox>Establecer como mi dirección de facturación</Checkbox>
          </Form.Item>
          <Form.Item>
            <p className="font-famBold text-main">
              Verifique su información antes de continuar
            </p>
          </Form.Item>
          <div className="flex w-full justify-end">
            {back && (
              <Button
                type="primary"
                className={`button-ctx clean-button`}
                onClick={goBack}
              >
                Regresar
              </Button>
            )}
            <Form.Item hidden={!showSubmitButton}>
              <Button
                htmlType="submit"
                loading={loading}
                disabled={!partialLocation || loading}
                className="button-ctx w-40 "
              >
                Guardar dirección
              </Button>
            </Form.Item>
          </div>
      </Form>
    </>
  );
});

PaymentFormAddressGoogle.displayName = 'PaymentFormAddressGoogle';
export default PaymentFormAddressGoogle;
