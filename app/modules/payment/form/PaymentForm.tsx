import { LeftOutlined } from '@ant-design/icons';
import { Button, Form, Radio, Select } from 'antd';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { FC, useEffect, useRef, useState } from 'react';
import { toast } from 'react-toastify';
import { NewAddress } from '../../../interfaces/Request/Client/Address';
import { UserInfo } from '../../../interfaces/Response/User/UserInfo';
import { CartState } from '../../../interfaces/State/Cart';
import { NotRegisterUserAddress } from '../../../interfaces/State/NotRegisterUserInterface';
import {
  getAllClientAddress,
  newAddressClient
} from '../../../services/Client/address/addressService';
import {
  clearShippingInfo,
  saveCartOnStorage,
  setCartLocation,
  setDeliveryMethod
} from '../../../services/redux/actions/CartActions';
import {
  notRegisterUserClearUserAddress,
  SetNotRegisterUserAddress,
  SetNotRegisterUserAddressValid,
  SetNotRegisterUserContactValid,
  SetNotRegisterUserValidateStatus
} from '../../../services/redux/actions/NotRegisterUserActions';
import { setTypeDelivery } from '../../../services/redux/actions/PaymentActions';
import { ClearNewsLetterState } from '../../../services/redux/reducers/NewsLetterReducer';
import {
  CtxDispatch,
  CtxSelector,
  ReduxStore
} from '../../../services/redux/store';
import { getUserInfo } from '../../../services/utils';
import { DirectionCard } from '../../Directions';
import { GoogleIcon, Location } from '../../shared';
import PaymentFormAddressGoogle from '../../shared/PaymentFormAddress/PaymentFormAddressGoogle';
import { PaymentFormAddressGoogleT } from '../../shared/PaymentFormAddress/PaymentFormAddressGoogle.types';
import Image from 'next/image';

export const PaymentForm: FC = () => {
  const addressComponentRef = useRef<PaymentFormAddressGoogleT | null>(null);
  const [sending, setSending] = useState<'SHIPPING' | 'PICK_UP'>('SHIPPING');
  const [addressList, setAddressList] = useState<NewAddress[]>([]);
  const [saveAdLoading, setSaveAdLoading] = useState<boolean>(false);
  const [showAdForm, setShowAddForm] = useState(false);
  const [locationSelected, setLocationSelected] = useState<NewAddress>();
  const [userInfo, setUserInfo] = useState<UserInfo | undefined>();
  const router = useRouter();
  const { isReady } = router;

  const dispatch = CtxDispatch();
  const { products }: CartState = CtxSelector(s => s.cart!);

  const onContinueNotREgisterUser = () => {
    const { notRegisterUser } = ReduxStore.getState();

    const { userAddress, userContact, isContactValid, validate } =
      notRegisterUser;
    if (sending === 'PICK_UP' && validate && isContactValid) {
      if (userContact) {
        router.push('/payment/billing');
        return;
      }
    }
    if (sending === 'SHIPPING') {
      if (!!userAddress && !!userContact) {
        dispatch(SetNotRegisterUserValidateStatus(false));
        router.push('/payment/shippment');
        return;
      }
    }
  };

  useEffect(() => {
    dispatch(SetNotRegisterUserAddressValid(false));
    dispatch(SetNotRegisterUserContactValid(false));
    dispatch(ClearNewsLetterState());
  }, []);

  const [form] = Form.useForm();

  const getAddressList = async (defId?: string) => {
    const urserInfoS = getUserInfo();
    if (!urserInfoS) return;
    const res = await getAllClientAddress();
    if (!res) return;
    setAddressList(res.content);
    let defaultDir: NewAddress | undefined;
    if (defId) defaultDir = res.content.find(i => i.id! == defId) || undefined;
    if (!defId)
      defaultDir = res.content.find(i => i.predetermined == true) || undefined;
    form.setFieldsValue({
      ...form.getFieldsValue,
      addressSelection: defaultDir?.id || null
    });
    const location = res.content.find(x => x.id === defaultDir?.id);
    setLocationSelected(location);
  };
  useEffect(() => {
    dispatch(clearShippingInfo());
    if (!isReady) return;
    setUserInfo(getUserInfo()?.info || undefined);
    dispatch(setDeliveryMethod('SHIPPING'));
    setTimeout(() => {
      form.setFieldsValue({
        ...form.getFieldsValue,
        addressSelection: null
      });
      getAddressList();
    }, 100);
  }, [isReady]);

  const onChange = (e: any) => {
    const val = e.target.value;
    setShowAddForm(val === 'SHIPPING');
    setSending(e.target.value);
    if (val === 'PICK_UP') {
      dispatch(notRegisterUserClearUserAddress());
    }

    dispatch(setDeliveryMethod(e.target.value));
    dispatch(saveCartOnStorage());
  };
  return (
    <div>
      <div className="radioGrup-payment">
        <Radio.Group
          onChange={onChange}
          size="small"
          value={sending}
          className="w-full"
        >
          {
            <>
              <div className="radio-payment-send">
                <Radio className="flex items-center p-4" value={'SHIPPING'}>
                  <div className="flex items-center ">
                    <div className="flex items-center">
                    <Image src="/img/cp3.png" alt="ShippingCTx" width={45} height={45} />
                    <Image src="/img/cp3.png" alt="ShippingCTx" width={45} height={45} />
                    <Image src="/img/cp3.png" alt="ShippingCTx" width={45} height={45} />
                  </div >
                    <p>Envío a domicilio</p>
                  </div>
                </Radio>
              </div>

              <div className="radio-payment-send p-2">
                <Radio className="flex items-center p-3" value={'PICK_UP'}>
                  <div className="flex items-center gap-4">
                    <Image src="/img/ClickPick.png" alt="Click&Pick CTx" width={100} height={24} />

                    <p>Recoger en tienda</p>

                  </div>
                </Radio>
              </div>
            </>
          }
        </Radio.Group>
      </div>
      {sending !== 'SHIPPING' && (
        <div className={`mt-7 min-h-[14rem]`}>
          <Location height={14} />
        </div>
      )}
      <div className={sending === 'SHIPPING' ? 'block' : 'hidden'}>
        <div className="my-4">
          <h4 className="mt-5 text-2xl font-bold">Dirección de envío</h4>
          {addressList.length > 0 && (
            <div>
              <p>Seleccione la dirección</p>
              <div className="flex justify-center sm:justify-end">
                <Form.Item className="w-full sm:w-1/2">
                  <Button
                    onClick={() => setShowAddForm(!showAdForm)}
                    className="button-ctx mt-5 mr-0 flex w-full items-center justify-center"
                  >
                    {showAdForm ? 'Cancelar' : 'Agregar nueva dirección'}
                    {!showAdForm && (
                      <span className="mt-1  text-center ">
                        <GoogleIcon className="text-xl" icon={'add'} />
                        <GoogleIcon className="text-xl" icon={'home'} />
                      </span>
                    )}
                  </Button>
                </Form.Item>
              </div>
            </div>
          )}
          <Form form={form} className={`${!showAdForm ? 'block' : 'hidden'}`}>
            {addressList.length > 0 && (
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
          </Form>
          {(showAdForm || addressList.length === 0) && (
            <>
              <PaymentFormAddressGoogle
                showSubmitButton={false}
                autoFill
                ref={addressComponentRef}
                back={false}
                loading={saveAdLoading}
                onSubmit={value => {
                  const req = { ...value };
                  setSaveAdLoading(true);
                  setShowAddForm(false);
                  if (userInfo?.id) {
                    newAddressClient(req)
                      .then(d => {
                        setSaveAdLoading(false);
                        getAddressList(d?.data.id);
                        toast.success(d?.message);
                      })
                      .catch(() => setSaveAdLoading(false));
                  } else {
                    setSaveAdLoading(false);
                    const r = req as unknown;
                    dispatch(
                      SetNotRegisterUserAddress(r as NotRegisterUserAddress)
                    );
                  }
                }}
              />
            </>
          )}
        </div>
        <div className="mt-4 grid grid-cols-2 items-center">
          <Link href="/cart" className="hover:text-enphasis">
            <a className="flex items-center gap-2">
              <LeftOutlined /> Volver al carrito
            </a>
          </Link>
          {userInfo ? (
            <Button
              htmlType="button"
              loading={saveAdLoading}
              onClick={() => {
                if (addressList.length === 0 || showAdForm) {
                  addressComponentRef.current?.onFinish();
                  return;
                }
                if (!locationSelected) return;
                dispatch(setCartLocation(locationSelected));
                dispatch(saveCartOnStorage());
                dispatch(setTypeDelivery(sending));
                router.push('/payment/shippment');
              }}
              disabled={
                !form.getFieldValue('addressSelection') &&
                addressList.length > 0
              }
              className="button-ctx m-0"
            >
              {addressList.length === 0 ? 'Guardar' : 'Continuar'}
            </Button>
          ) : (
            <Button
              className="button-ctx m-0"
              loading={saveAdLoading}
              disabled={saveAdLoading}
              onClick={() => {
                dispatch(SetNotRegisterUserValidateStatus(true));
                addressComponentRef.current?.onFinish();
                setTimeout(() => {
                  const { notRegisterUser } = ReduxStore.getState();
                  const { userAddress, userContact } = notRegisterUser;

                  if (userContact && sending === 'PICK_UP') {
                    router.push('/payment/billing');
                    return;
                  }
                  if (userContact && sending === 'SHIPPING') {
                    if (!!userAddress && !!userContact) {
                      dispatch(SetNotRegisterUserValidateStatus(false));
                      router.push('/payment/shippment');
                      return;
                    }
                  }
                }, 400);
              }}
            >
              {
                // Sin registro
              }
              Continuar
            </Button>
          )}
        </div>
      </div>
      {/* for al pickup chances */}
      <div className={`${sending !== 'SHIPPING' ? 'block' : 'hidden'}`}>
        <div className="mt-4 grid grid-cols-2 items-center">
          <Link href="/cart" className="hover:text-enphasis">
            <a className="flex items-center gap-2">
              <LeftOutlined /> Volver al carrito
            </a>
          </Link>
          <Button
            htmlType="button"
            onClick={() => {
              if (sending === 'PICK_UP') {
                if (!userInfo) {
                  dispatch(SetNotRegisterUserValidateStatus(true));
                  dispatch(saveCartOnStorage());
                  setTimeout(() => {
                    onContinueNotREgisterUser();
                  }, 300);
                  return;
                }
                router.push('/payment/billing');
                return;
              }
              if (!locationSelected && sending === 'SHIPPING') return;
              if (locationSelected) {
                dispatch(setCartLocation(locationSelected));
              }
              dispatch(setTypeDelivery(sending));
              dispatch(saveCartOnStorage());
              router.push('/payment/shippment');
            }}
            disabled={
              !form.getFieldValue('addressSelection') && sending !== 'PICK_UP'
            }
            className="button-ctx"
          >
            Continuar
          </Button>
        </div>
      </div>
    </div>
  );
};
