import { ExclamationCircleOutlined, LeftOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, List, Radio, Select } from 'antd';
import { CheckboxChangeEvent } from 'antd/lib/checkbox';
import confirm from 'antd/lib/modal/confirm';
import { NextPage } from 'next';
import Link from 'next/link';
import { useRouter } from 'next/router';
import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { NewAddress } from '../../app/interfaces/Request/Client/Address';
import { UserInfo } from '../../app/interfaces/Response/User/UserInfo';
import { CartState } from '../../app/interfaces/State/Cart';
import {
  NotRegisterUserAddress,
  NotRegisterUserState
} from '../../app/interfaces/State/NotRegisterUserInterface';
import { LOCATION_STORE } from '../../app/models/constants';
import { DirectionCard } from '../../app/modules/Directions';
import { BillingCfdiSelect } from '../../app/modules/payment/BillingCfdiSelection';
import { PaymentLayout } from '../../app/modules/PaymentLayout';
import { GoogleIcon } from '../../app/modules/shared';
import PaymentFormAddressGoogle from '../../app/modules/shared/PaymentFormAddress/PaymentFormAddressGoogle';
import { closeSesionService } from '../../app/services/auth/authService';
import { CfdiCatalogResponse } from '../../app/services/Billing/billing-responses';
import { BillingService } from '../../app/services/Billing/billing-service';
import {
  getAllClientAddress,
  newAddressClient
} from '../../app/services/Client/address/addressService';
import { LoaderActionsHide } from '../../app/services/redux/actions';
import {
  saveCartOnStorage,
  setRequiresTax,
  setTaxLocationState
} from '../../app/services/redux/actions/CartActions';
import {
  ClearNotRegisterUserTaxAddress,
  SetNotRegisterUserTaxAddress,
  SetNotRegisterUserValidateStatus
} from '../../app/services/redux/actions/NotRegisterUserActions';
import { LogOut } from '../../app/services/redux/actions/SessionActions';
import {
  CtxDispatch,
  CtxSelector,
  ReduxStore
} from '../../app/services/redux/store';
import {
  getNotUserRegisterStorage,
  getShoppingCart,
  getUserInfo
} from '../../app/services/utils';

interface ItemInfoProps {
  itemName: string;
  info: string;
  action: () => void;
}

const Billing: NextPage = () => {
  const billingService = new BillingService();
  const dispatch = CtxDispatch();
  const router = useRouter();
  const { isReady } = router;
  const [taxDir, setTaxDir] = useState<'same' | 'dif' | string>('same');
  const [cfdiSelected, setcfdiSelected] = useState<CfdiCatalogResponse | null>(null);
  const [cartInfo, setCartInfo] = useState<CartState | undefined>();
  const [userInfo, setUserInfo] = useState<UserInfo>();
  const [saveAdLoading, setSaveAdLoading] = useState(false);
  const [directions, setDirections] = useState<NewAddress[]>([]);
  const [addNew, setAddNew] = useState(false);
  const [taxLocationFull, setTaxLocationFull] = useState<NewAddress>();
  const [taxLocation, setTaxLocation] = useState<string>();
  const [selectedLocation, setSelectedLocation] = useState<NewAddress>();
  const [showBilling, setShowBilling] = useState(false);
  const [notRegisterUserData, setNotRegisterUserData] =
    useState<NotRegisterUserState>();
  const userNotRegisterState = CtxSelector(s => s.notRegisterUser);

  /* eslint-disable-next-line @typescript-eslint/no-extra-non-null-assertion */
  const cartState: CartState = CtxSelector(s => s.cart!);

  /* eslint-disable-next-line @typescript-eslint/no-extra-non-null-assertion */
  const sessionState = CtxSelector(s => s.session!);
  const [defaultBillingLocation, setdefaultBillingLocation] = useState<string | null>(null);
  const { userContact, userAddress } = userNotRegisterState;
  const { deliveryMethod } = cartState;

  useEffect(() => {
    if (isReady) {
      dispatch(LoaderActionsHide());
      dispatch(setRequiresTax(false));
      dispatch(SetNotRegisterUserValidateStatus(false));
      const userI = getUserInfo();
      if (!userNotRegisterState?.userContact && !userI) {
        router.replace('/payment');
        return;
      }
      const cartInfo: CartState = getShoppingCart();
      setNotRegisterUserData(getNotUserRegisterStorage());
      if (Object.getOwnPropertyNames(cartInfo).length == 0) {
        router.replace('/');
        return;
      }
      setCartInfo(cartInfo);
      if (cartInfo.deliveryMethod === 'PICK_UP') {
        setTaxDir('dif');
      }
      dispatch(SetNotRegisterUserValidateStatus(false));
      if (cartInfo.location?.id) {
        setSelectedLocation(cartInfo.location);
        dispatch(setTaxLocationState(cartInfo.location));
        dispatch(saveCartOnStorage());
        setTaxLocation(cartInfo.location.id);
      }
      if (userNotRegisterState.userContact) {
        setTaxDir('dif');
      }
      setUserInfo(userI?.info);
      getAddresses();
    } else return;

  }, [isReady]);

  useEffect(() => {
    if (directions && taxLocation) {
      setTaxLocationFull(directions.find(dir => dir.id === taxLocation));
    } else return;
  }, [directions, taxLocation]);

  const getAddresses = async () => {
    const { notRegisterUser, cart } = ReduxStore.getState();
    if (cart) {
      const userI = getUserInfo();
      if (!userI || notRegisterUser.userContact) return;

    const res = await getAllClientAddress();
    if (res?.content ) {
      const loc = res.content.find(i => (i.id = cart.location?.id));
      if (loc) {
        dispatch(setTaxLocationState(loc));
        dispatch(saveCartOnStorage());
        setTaxLocation(loc?.id);
      }

      setdefaultBillingLocation(
        res.content.find(x => x.billingAddress)?.id || null
      );

      setDirections(res.content);
    }
  }
}

  const checkBilling = (e: CheckboxChangeEvent) => {
    setShowBilling(e.target.checked);
    dispatch(setRequiresTax(e.target.checked));
    dispatch(saveCartOnStorage());
    if (e.target.checked) {
      const userinfo = getUserInfo()?.info;
      if (!userinfo || !userinfo.rfc) return;
      billingService.getCfdiUse(userinfo.rfc);
    }
  };

  const closeSession = async () => {
    dispatch(LogOut());
    await closeSesionService();
    localStorage.removeItem('info');
    localStorage.removeItem('session');
    router.push('/auths/login');
  };

  const ItemInfo = ({ itemName, info, action }: ItemInfoProps) => {
    return (
      <List.Item className="pl-2">
        <div className="grid w-full grid-cols-4">
          <div className='font-["Mon-Bold"] text-xs sm:text-sm'>{itemName}</div>
          {/*<div className="col-span-2 max-w-[160px] overflow-hidden text-ellipsis sm:max-w-none text-xs">*/}
          <div className="col-span-2 max-w-[2000px] overflow-hidden text-ellipsis text-xs sm:text-sm">
            {info}
          </div>
          <div className="text-right ">
            <Button className="border-none text-main text-xs sm:text-sm" onClick={action}>
              Cambiar
            </Button>
          </div>
        </div>
      </List.Item>
    );
  };

  const openModal = () => {

    if (userContact) {
      router.push('/payment');
      return;
    }
    confirm({
      icon: <ExclamationCircleOutlined className="text-yellow-400" />,
      title: 'Está a punto de cerrar sesión',
      content: <h1>¿Desea continuar?</h1>,
      okText: 'Si',
      cancelText: 'Cancelar',
      okButtonProps: { type: 'primary', className: 'bg-[#1890ff]' },
      onOk() {
        closeSession();
      }
    });
  };

    return (
      <PaymentLayout>
        <div className="rounded-2xl border-2 border-graySeparation p-2">
          <List>
            <ItemInfo
              itemName="Nombre: "
              info={
                userInfo?.email ||
                `${userContact?.name} ${userContact?.lastName} ${userContact?.secondLastName || ''}`
              }
              action={openModal}
            />
            {cartInfo?.deliveryMethod == 'PICK_UP' && (
              <ItemInfo
                itemName="Recolección: "
                info={LOCATION_STORE}
                action={() => router.push('/payment')}
              />
            )}
            {selectedLocation && cartInfo?.deliveryMethod === 'SHIPPING' && (
              <ItemInfo
                itemName="Enviar a "
                info={
                  `${selectedLocation?.streetName || userAddress?.streetName}, ` +
                  `${selectedLocation?.name}, ` +
                  `${selectedLocation?.municipality}, ` +
                  `${selectedLocation?.zipCode || userAddress?.zipCode}, ` +
                  `${selectedLocation?.state}`
                }
                action={() => router.push('/payment')}
              />
            )}
          </List>
        </div>
        <div className="mt-9">
          <Checkbox onChange={checkBilling}>Requiero factura</Checkbox>
        </div>

        {showBilling && (
          <div className="mt-9">
            <h4 className="text-2xl font-bold">Dirección de facturación</h4>
            <p className="mb-6">
              Selecciona la dirección que coincida con tu tarjeta o forma de pago.
            </p>
            <div className="mt-6 rounded-2xl border-2 border-graySeparation p-3">
              <Radio.Group
                onChange={({ target: { value } }) => {
                  if (value === 'same') {
                    dispatch(ClearNotRegisterUserTaxAddress());
                  }
                  setTaxDir(value);
                }}
                value={taxDir}
                defaultValue={
                  cartInfo?.deliveryMethod === 'SHIPPING' ? 'same' : 'dif'
                }
                className="grid grid-cols-1"
              >
                {cartInfo?.deliveryMethod === 'SHIPPING' &&
                  (!!userNotRegisterState.userAddress || !!cartInfo.location) && (
                    <>
                      <Radio value={'same'}>
                        <>
                          La misma dirección de envío
                          {taxDir !== 'dif' && taxLocationFull && (
                            <div className="mt-3 w-full">
                              <DirectionCard
                                controls={false}
                                className="border-none"
                                direction={taxLocationFull}
                              />
                            </div>
                          )}
                        </>
                      </Radio>
                      <hr className="my-3" />
                    </>
                  )}
                <Radio value={'dif'} className="radio-simple">
                  <div className="ml-auto w-full">
                    Usar una dirección de facturación distinta
                  </div>
                </Radio>
              </Radio.Group>

              {directions.length > 0 && taxDir != 'same' && (
                <div className="mt-10 grid grid-cols-1 gap-6">
                  <BillingCfdiSelect
                    rfc={userInfo?.rfc}
                    cfdiSelectionChange={v => setcfdiSelected(v)}
                  />
                  <div className={`${!addNew ? 'block' : 'hidden'}`}>
                    <p>Seleccione la dirección</p>
                    <Form>
                      <Form.Item>
                        <Select
                          defaultValue={defaultBillingLocation}
                          value={taxLocation}
                          onChange={c => {
                            setTaxLocation(c);
                            const loc = directions.filter(r => r.id == c);
                            dispatch(setTaxLocationState(loc[0]));
                            dispatch(saveCartOnStorage());
                          }}
                          className="selection"
                        >
                          {directions.map(d => (
                            <Select.Option value={d.id} key={d.id}>
                              <DirectionCard
                                controls={false}
                                direction={d}
                                className="border-none"
                              />
                            </Select.Option>
                          ))}
                        </Select>
                      </Form.Item>
                    </Form>
                  </div>
                  <div>
                    <Button
                      onClick={() => setAddNew(!addNew)}
                      className="button-ctx float-right mt-5 mr-0 flex items-center gap-4"
                    >
                      {addNew ? 'Cancelar' : 'Agregar nueva dirección'}
                      {!addNew && (
                        <span className="mt-1">
                          <GoogleIcon className="text-xl" icon={'add'} />
                          <GoogleIcon className="text-xl" icon={'home'} />
                        </span>
                      )}
                    </Button>
                  </div>
                  {taxDir !== 'same' && addNew && sessionState.auth && (
                    <div className="mt-5">
                      {
                        <PaymentFormAddressGoogle
                          back={false}
                          onSubmit={value => {
                            const req = value;
                            setSaveAdLoading(true);
                            newAddressClient(req)
                              .then(d => {
                                setSaveAdLoading(false);
                                if (!d) return;
                                getAddresses();
                                setTaxLocation(d.data.id);
                                dispatch(setTaxLocationState(d.data));
                                toast.success(d.message, { theme: 'colored' });
                              })
                              .catch(() => setSaveAdLoading(false));
                          }}
                        />
                      }
                    </div>
                  )}
                </div>
              )}
            </div>
            {taxDir === 'same' && directions.length > 0 && (
              <BillingCfdiSelect
                rfc={userInfo?.rfc}
                cfdiSelectionChange={v => setcfdiSelected(v)}
              />
            )}
            {/*start directions*/}
            {directions.length === 0 && sessionState.auth && (
              <div className="mt-6">
                <PaymentFormAddressGoogle
                  loading={saveAdLoading}
                  back={false}
                  onSubmit={value => {
                    const req = { ...value };
                    setSaveAdLoading(true);
                    if (userNotRegisterState?.userContact) {
                      setSaveAdLoading(false);
                      const req2 = req as unknown;
                      dispatch(
                        SetNotRegisterUserTaxAddress(
                          req2 as NotRegisterUserAddress
                        )
                      );
                      return;
                    }
                    newAddressClient(req)
                      .then(d => {
                        setSaveAdLoading(false);
                        if (!d?.data.id) return;
                        getAddresses();
                        setTaxLocation(d.data.id);
                        dispatch(setTaxLocationState(d.data));
                        toast.success(d.message, { theme: 'colored' });
                      })
                      .catch(() => setSaveAdLoading(false));
                  }}
                />
                <BillingCfdiSelect
                  rfc={userInfo?.rfc}
                  cfdiSelectionChange={v => setcfdiSelected(v)}
                />
              </div>
            )}
            {/* for no user registered */
              !sessionState.auth && (
                <>
                  {taxDir !== 'same' && (
                    <div className="mt-6">
                      <PaymentFormAddressGoogle
                        showSubmitButton={false}
                        loading={saveAdLoading}
                        back={false}
                        onSubmit={value => {
                          const req = { ...value };
                          if (notRegisterUserData?.userContact) {
                            const r = req as unknown;
                            dispatch(
                              SetNotRegisterUserTaxAddress(
                                r as NotRegisterUserAddress
                              )
                            );
                            return;
                          }
                          setSaveAdLoading(true);
                          newAddressClient(req)
                            .then(d => {
                              setSaveAdLoading(false);
                              if (!d?.data.id) return;
                              getAddresses();
                              setTaxLocation(d.data.id);
                              dispatch(setTaxLocationState(d.data));
                              toast.success(d.message, { theme: 'colored' });
                            })
                            .catch(() => setSaveAdLoading(false));
                        }}
                      />
                    </div>
                  )}
                  <BillingCfdiSelect
                    rfc={userInfo?.rfc}
                    cfdiSelectionChange={v => setcfdiSelected(v)}
                  />
                </>
              )
            }
          </div>
        )}
        <div className="mt-[4rem] flex place-content-between">
          {deliveryMethod === 'PICK_UP' && (
            <Link href="/payment" className="hover:text-enphasis">
              <a className="flex items-center gap-2">
                <LeftOutlined /> Volver a Cuenta
              </a>
            </Link>
          )}
          {deliveryMethod === 'SHIPPING' && (
            <Link href="/payment/shippment" className="hover:text-enphasis">
              <a className="flex items-center gap-2">
                <LeftOutlined /> Volver a Dirección de envio
              </a>
            </Link>
          )}
          <Button
            disabled={
              ((!cfdiSelected || !userInfo?.companyName) &&
                showBilling &&
                !!userInfo) ||
              (!userInfo &&
                ((!userNotRegisterState.taxAddress &&
                  taxDir != 'same' &&
                  !!userNotRegisterState.userAddress) ||
                  !userNotRegisterState.fiscalRegimen ||
                  !userNotRegisterState.billingInfo?.rfc ||
                  !userNotRegisterState.validatedUser ||
                  !cfdiSelected ||
                  !userNotRegisterState.billingInfo?.companyName) &&
                showBilling)
            }
            onClick={() => router.push('choose')}
            className="button-ctx"
          >
            Continuar
          </Button>
        </div>
      </PaymentLayout>
    );
  };
  export default Billing;
