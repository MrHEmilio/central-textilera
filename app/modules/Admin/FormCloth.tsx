import { Button, Card, Col, Form, Modal, Row, Select, Steps } from 'antd';
import { useEffect, useState } from 'react';
import { PriceLabel } from '../shared';

import { DataFiber } from '../../interfaces/Response/Admin/Fiber';
import { DataTypeSale } from '../../interfaces/Response/Admin/typeSale';
import { getFiber } from '../../services/admin/fibers';
import { getTypeSale } from '../../services/admin/typeSale';

import { useRouter } from 'next/router';
import { toast } from 'react-toastify';
import { AddClothPost } from '../../interfaces/Request/Cloth/AddClothRequest';
import { DataCollection } from '../../interfaces/Response/Admin/Collection';
import { DataColors } from '../../interfaces/Response/Admin/Colors';
import { addClothService, editClothService } from '../../services/cloth';
import { CtxSelector } from '../../services/redux/store';
import { removeAccents } from '../../services/utils';
import { CollecionPicker } from './Cloth/CollecionPicker';
import ColorPickerForCloth from './Cloth/ColorPickerForCloth';
import { PricesGroup } from './Cloth/PricesGroup';
import { FormClothSection } from './FormClothSection';
import { FormClothSectionDimensions } from './FormClothSectionDimensions';
import { FormClothSectionSampler } from './FormClothSectionSampler';
import { ModalAddFiber } from './ModalAddFiber';
import { ModalAddSale } from './ModalAddSale';
const { Option } = Select;

interface FormValues {
  collections: string[];
  fiber: string;
  mainDescription: string;
  name: string;
  sale: string;
  uses: string[];
  variants: { color: string; amount: string }[];
  prices: Array<{
    price: number;
    firstAmountRange: number;
    lastAmountRange: number | undefined;
  }>;
  weight: number;
  width: number;
  wxw?: number;
  samplerAmount?: number;
  samplerDescription: string;
  samplerPrice: number;
  image?: any;
  imageSampler?: any;
  dimension: number;
  yieldPerKilo: number;
  descriptions?: ({ description: string } | undefined)[];
  averagePerRoll: number;
  billing: {
    unitCode: string;
    search: string;
    searchUnit: string;
    productCode: string;
  };
  billingSampler: {
    unitCode: string;
    search: string;
    searchUnit: string;
    productCode: string;
  };
}

export const FormCloth = () => {
  const [form] = Form.useForm();

  // eslint-disable-next-line no-unused-vars
  enum SectionsForm {
    // eslint-disable-next-line no-unused-vars
    aboutCloth = 0,
    // eslint-disable-next-line no-unused-vars
    salesFibers = 1,
    // eslint-disable-next-line no-unused-vars
    dimensions = 2,
    // eslint-disable-next-line no-unused-vars
    prices = 3,
    // eslint-disable-next-line no-unused-vars
    collectionsUses = 4,
    // eslint-disable-next-line no-unused-vars
    colors = 5,
    // eslint-disable-next-line no-unused-vars
    sampler = 6
  }
  const router = useRouter();
  const { isReady } = router;
  const clothToEdit = CtxSelector(s => s.clothForm);

  const [sale, setSale] = useState<DataTypeSale[]>([]);
  const [isEditing, setIsEditing] = useState(false);
  const [fiber, setFiber] = useState<DataFiber[]>([]);
  const [values, setValues] = useState<FormValues>();
  const [collectionsSelected, setCollectionsSelected] = useState<DataCollection[]>([]);
  const [saleSelected, setSaleSelected] = useState<DataTypeSale>();
  const [fiberSelected, setFiberSelected] = useState<DataFiber>();
  const [pricesLen, setPricesLen] = useState(0);
  const [colorLen, setColorLen] = useState(0);
  const [imgCloth, setImgCloth] = useState<string | undefined>();
  const [clothLoading, setClothLoading] = useState(false);
  const [colorsTotal, setColorsTotal] = useState<DataColors[] | undefined>();
  const [collectionTotal, setCollectionTotal] = useState<
    DataCollection[] | undefined
  >();
  const [modalFiber, setModalFiber] = useState(false);
  const [modalSale, setModalSale] = useState(false);
  const [currentStep, setCurrentStep] = useState(0);

  const handleCheck = (index: number) => {
    const formV = form.getFieldsValue();
    formV.prices[index] = {
      ...formV.prices[index],
      lastAmountRange: undefined
    };

    form.setFieldsValue(formV);
  };
  const nextBackBtns = (
    hiddeBack = false,
    names: any[],
    showSubmit = false
  ) => {
    const submitText = isEditing ? 'Guardar cambios' : 'Crear Tela';
    return (
      <Col>
        <Button
          hidden={hiddeBack}
          className="button-ctx clean-button float-left mr-0"
          onClick={() => setCurrentStep(currentStep - 1)}
        >
          {'<'} Regresar
        </Button>
        <Button
          htmlType={showSubmit ? 'submit' : 'button'}
          className="button-ctx float-right mr-0"
          loading={showSubmit ? clothLoading : false}
          onClick={async () => {
            if (showSubmit) return;
            let hasErrors = false;
            await form.validateFields(names);
            names.map(name => {
              if (form.getFieldError(name).length > 0) {
                hasErrors = true;
              }
            });
            if (hasErrors) return Promise.reject(hasErrors);
            setCurrentStep(currentStep + 1);
          }}
        >
          {showSubmit ? submitText : 'Continuar'}
        </Button>
      </Col>
    );
  };

  const onFinish = async (value: FormValues) => {
    if (!imgCloth) return;
    const req: AddClothPost = {
      descriptions: value.descriptions?.map(i => i?.description) as any,
      name: value.name,
      collections: value.collections,
      fiber: value.fiber,
      ...(!imgCloth.includes('https') && {
        image: imgCloth
      }),
      mainDescription: value.mainDescription,
      measure: {
        dimension: value.dimension,
        weight: value.weight,
        width: value.width * 100,
        ...(value.yieldPerKilo && { yieldPerKilo: value.yieldPerKilo }),
        averagePerRoll: value.averagePerRoll
      },
      prices: value.prices.map((i, index) => ({
        firstAmountRange: i.firstAmountRange,
        lastAmountRange: i.lastAmountRange || null,
        order: index + 1,
        price: i.price
      })),
      sale: value.sale,
      sampler: {
        price: value.samplerPrice,
        amount: value.samplerAmount,
        description: value.samplerDescription,
        billing: {
          unitCode: value.billingSampler.unitCode,
          unitLabel: value.billingSampler.searchUnit,
          productCode: value.billingSampler.productCode,
          productLabel: value.billingSampler.search
        }
      },
      uses: [],
      variants: value.variants.map(i => ({
        amount: Number(i.amount),
        color: i.color
      })),
      billing: {
        unitCode: value.billing.unitCode,
        unitLabel: value.billing.searchUnit,
        productCode: value.billing.productCode,
        productLabel: value.billing.search
      }
    };

    setClothLoading(true);

    if (isEditing && clothToEdit) {
      const pricesForm = req.prices;
      req.id = clothToEdit.id;
      req.measure.id = clothToEdit.measure?.id;
      req.sampler.id = clothToEdit.sampler.id;
      req.billing.id = clothToEdit.billing.id;
      req.sampler.billing.id = clothToEdit.sampler.billing.id;

      req.prices = req.prices.map((v, index) => ({
        firstAmountRange: req.prices[index].firstAmountRange,
        lastAmountRange: req.prices[index].lastAmountRange,
        order: req.prices[index].order,
        price: req.prices[index].price
      }));
      pricesForm.forEach(fm => {
        if (!req.prices.find(v => v.order)) {
          req.prices.push(fm);
        }
      });

      const resEdit = await editClothService(req);

      setClothLoading(false);
      if (!resEdit) return;

      toast.success(resEdit.message, {
        theme: 'colored'
      });

      router.replace('/admin/cloth');

      return;
    }

    const res = await addClothService(req);

    setClothLoading(false);

    if (res?.message) {
      toast.success(res.message, {
        theme: 'colored'
      });
      router.replace('/admin/cloth');
    }
  };

  const getSalesCloth = async () => {
    const response = await getTypeSale(true);
    if (response) {
      setSale(response.content);
    }
  };

  const getFiberCloth = async () => {
    const response = await getFiber(true);
    if (response) {
      setFiber(response.content);
    }
  };

  const formChanges = () => {
    let values: FormValues = form.getFieldsValue();
    if (values.weight && values.width) {
      values = { ...values, wxw: values.weight * values.width };
      form.setFieldsValue({ wxw: values.weight * values.width });
    }

    setValues(values);
  };
  useEffect(() => {
    const formV = form.getFieldsValue();
    const sal = sale;
    const fib = fiber;
    fib.map(fi => {
      if (fi.id === formV.fiber) {
        setFiberSelected(fi);
      }
    });
    sal.map(sa => {
      if (sa.id === formV.sale) {
        setSaleSelected(sa as DataTypeSale);
      }
    });
  }, [sale, fiber]);

  const setWidth = (width: number) => {
    const widtM = width / 100;
    return widtM;
  };

  useEffect(() => {
    if (collectionTotal && collectionTotal.length > 0 && values?.collections) {
      setCollectionsSelected(
        collectionTotal.filter(i =>
          values?.collections.find(val => val === i.id)
        )
      );
    }
  }, [collectionTotal]);

  useEffect(() => {
    if (!isReady) return;
    getSalesCloth();
    getFiberCloth();
    if (!clothToEdit?.id) return;
    setIsEditing(true);

    const fmVals: FormValues = {
      name: clothToEdit.name,
      mainDescription: clothToEdit.mainDescription,
      dimension: clothToEdit?.measure?.dimension || 0,
      width: setWidth(clothToEdit?.measure?.width || 0) || 0,
      weight: clothToEdit?.measure?.weight || 0,
      wxw:
        setWidth(clothToEdit?.measure?.width || 0) *
          (clothToEdit?.measure?.weight || 0) || 0,
      prices: clothToEdit.prices.map(i => ({
        firstAmountRange: i.firstAmountRange || 0,
        lastAmountRange: i.lastAmountRange || undefined,
        price: i.price || 0
      })),
      collections: clothToEdit.collections.map(i => i.id),
      uses: [],
      descriptions: clothToEdit.descriptions
        ?.filter(i => !i.automatic)
        .map(x => ({ description: x.name })),
      fiber: clothToEdit.fiber.id,
      sale: clothToEdit.sale.id,
      variants: clothToEdit.variants.map(i => ({
        color: i.color.id,
        amount: i.amount.toString()
      })),
      samplerDescription: clothToEdit.sampler.description,
      samplerPrice: clothToEdit.sampler.price,
      samplerAmount: clothToEdit.sampler.amount,
      imageSampler: [clothToEdit.sampler.image],
      image: [clothToEdit.image],
      yieldPerKilo: clothToEdit?.measure?.yieldPerKilo || 0,
      averagePerRoll: clothToEdit?.measure?.averagePerRoll || 0,
      billing: {
        productCode: clothToEdit.billing.productCode,
        search: clothToEdit.billing.productLabel,
        unitCode: clothToEdit.billing.unitCode,
        searchUnit: clothToEdit.billing.unitLabel
      },
      billingSampler: {
        productCode: clothToEdit.sampler.billing.productCode,
        search: clothToEdit.sampler.billing.productLabel,
        unitCode: clothToEdit.sampler.billing.unitCode,
        searchUnit: clothToEdit.sampler.billing.unitLabel
      }
    };
    form.setFieldsValue(fmVals);
    setImgCloth(clothToEdit.image);
    formChanges();
  }, [isReady]);

  const totalStep = 7;
  const percentStep = (currentStep / (totalStep - 1)) * 100;

  return (
    <Card className='border-t-[0.5px] border-graySeparation overflow-y-auto' style={{borderRadius: '0.7rem', boxShadow: '0.7rem 0.7rem 1.5rem rgba(0, 0, 0, 0.2)'}}>
      <Form
        layout="vertical"
        onChange={formChanges}
        onFinish={onFinish}
        autoComplete="off"
        className="grid grid-cols-5 gap-5 pt-4"
        form={form}
      >
        {/**Eliminé overflow-y-auto   D4893461*/}
        <Steps
          percent={percentStep}
          direction="vertical"
          className="col-span-1 max-h-auto border-r-[1px] border-r-graySeparation" 
          current={currentStep}
        > 
          <Steps.Step
            title="Tela"
            description={
              values?.name &&
              values?.mainDescription &&
              currentStep && (
                <>
                  {`${values?.name}`}
                  <br />
                  <img className="ml-3 mt-1 max-h-11 w-auto" src={imgCloth} />
                </>
              ) 
            }
          />
          <Steps.Step
            title="Venta y Fibras"
            description={
              <>
                {saleSelected &&
                  fiberSelected &&
                  currentStep != SectionsForm.salesFibers &&
                  `${saleSelected.name} - ${fiberSelected.name}`}
              </>
            }
          />

          <Steps.Step
            title="Dimensiones"
            description={
              <>
                {values?.dimension && currentStep !== SectionsForm.dimensions && (
                  <>
                    <p>Dimensión: {values.dimension} cm</p>
                    <p>Ancho: {values.width} m</p>
                    <p>
                      Peso: {values.weight} gr/m<sup>2</sup>
                    </p>
                  </>
                )}
              </>
            }
          />
          <Steps.Step
            title="Precios"
            description={
              values?.prices &&
              currentStep != SectionsForm.prices && (
                <>
                  {values.prices.map(i => (
                    <>
                      <div
                        className="border-t-[1px] border-graySeparation py-2 first-of-type:border-none"
                        key={
                          i.firstAmountRange.toString() +
                          i.price?.toString() +
                          'prices'
                        }
                      >
                        <p>
                          Precio: <PriceLabel coin price={i.price} />
                        </p>
                        <div>
                          <p>
                            De: {i.firstAmountRange}
                            <span className="ml-2">
                              A:{' '}
                              {!i.lastAmountRange
                                ? 'más unidades'
                                : i.lastAmountRange}
                            </span>
                          </p>
                        </div>
                      </div>
                    </>
                  ))}
                </>
              )
            }
          />
          <Steps.Step
            title="Usos"
            description={
              <>
                {collectionsSelected.length > 0 &&
                  currentStep != SectionsForm.collectionsUses && (
                    <p>
                      <strong>Usos</strong>:{' '}
                      {`${collectionsSelected?.map(i => i.name)} `}
                    </p>
                  )}
              </>
            }
          />
          <Steps.Step
            title="Colores"
            description={
              <>
                {values?.variants &&
                  currentStep !== SectionsForm.colors &&
                  values.variants.map(i => {
                    const color = colorsTotal?.find(c => c.id === i.color);
                    return (
                      <div
                        className="grid grid-cols-3 items-center"
                        key={i.color + 'colorSection'}
                      >
                        <p> {i.amount}</p>
                        <p>{color?.name}</p>
                        <div
                          className="h-3 w-8"
                          style={{
                            background: color?.code || 'gray'
                          }}
                        ></div>
                      </div>
                    );
                  })}
              </>
            }
          />
          <Steps.Step title="Muestrario" />
        </Steps>

        <Row className="col-span-4 max-h-[60vh] "> {/**Eliminé overflow-y-auto */}
          <div
            className={` w-full 
            ${SectionsForm.aboutCloth === currentStep ? '' : 'hidden'}`}
          >
            <FormClothSection
              imgCloth={imgCloth}
              clothToEdit={clothToEdit}
              handleImgChange={() => {
                const img = form.getFieldValue('image');
                const reader = new FileReader();

                if (!img[0]) {
                  setImgCloth(undefined);
                  return;
                }

                reader.readAsDataURL(img[0].originFileObj);
                reader.onload = () => {
                  setImgCloth(reader.result as string);
                };
              }}
            />
            {nextBackBtns(true, [
              'name',
              'mainDescription',
              'image',
              ['billing', 'search'],
              ['billing', 'productCode'],
              ['billing', 'searchUnit'],
              ['billing', 'unitCode']
            ])}
          </div>

          <div
            className={`grid w-full ${
              SectionsForm.dimensions === currentStep ? '' : 'hidden'
            }`}
          >
            <FormClothSectionDimensions saleSelected={saleSelected} />

            {/* <Col>
                <div className="mb-3">
                  <label>
                    <span className="font-serif text-[14px] text-[#ff4d4f] after:content-['*']"></span>{' '}
                    Información adicional
                  </label>
                </div>
                <DetailsCloth itemsLen={setDetailsLen} />
              </Col> */}

            <Col span={24} className="mt-auto">
              {nextBackBtns(
                false,
                [
                  'dimension',
                  'width',
                  'weight',
                  'yieldPerKilo',
                  'averagePerRoll',
                  'descriptions'
                  /* detailsLen > 0
                    ? [
                        form
                          .getFieldValue('descriptions')
                          .map((_: unknown, index: number) => [
                            'descriptions',
                            index,
                            'description'
                          ])
                      ].flatMap((n: unknown) => n)
                    : ['descriptions', 0, 'description'] */
                ].flatMap(n => n)
              )}
            </Col>
          </div>

          <div
            className={`w-full  ${
              SectionsForm.prices === currentStep ? '' : 'hidden'
            }`}
          >
            <Col>
              <PricesGroup
                priceLen={setPricesLen}
                handelCheck={handleCheck}
                formV={form.getFieldsValue()?.prices}
              />
            </Col>
            <Col className="mt-auto">
              {nextBackBtns(
                false,
                [
                  'prices',
                  pricesLen > 0
                    ? [
                        form
                          .getFieldValue('prices')
                          .map((_: unknown, index: number) => [
                            'prices',
                            index,
                            'price'
                          ])
                      ].flatMap((n: unknown) => n)
                    : [['price', 0, 'price']],
                  pricesLen > 0
                    ? [
                        form
                          .getFieldValue('prices')
                          .map((_: unknown, index: number) => [
                            'prices',
                            index,
                            'firstAmountRange'
                          ])
                      ].flatMap((n: unknown) => n)
                    : [['prices', 0, 'firstAmountRange']],
                  pricesLen > 0
                    ? [
                        form
                          .getFieldValue('prices')
                          .map((_: unknown, index: number) => [
                            'prices',
                            index,
                            'lastAmountRange'
                          ])
                      ].flatMap((n: unknown) => n)
                    : [['prices', 0, 'lastAmountRange']]
                ].flatMap(n => n)
              )}
            </Col>
          </div>

          <div
            className={`w-full ${
              SectionsForm.collectionsUses === currentStep ? '' : 'hidden'
            }`}
          >
            <Col>
              <CollecionPicker
                collectionList={setCollectionTotal}
                changeSelection={v => {
                  setCollectionsSelected(v as DataCollection[]);
                }}
              />
            </Col>

            {nextBackBtns(false, ['collections', 'uses'])}
          </div>

          <div
            className={`w-full ${
              SectionsForm.salesFibers === currentStep ? '' : 'hidden'
            }`}
          >
            <Col>
              <Form.Item
                label={
                  <div className="flex w-[800px] items-center justify-between">
                    <p>Unidad de medida de la venta</p>
                    <Button
                      type="primary"
                      className="button-ctx ml-8 h-[30px] w-[30%]"
                      onClick={() => setModalSale(true)}
                    >
                      Agregar Tipo de Venta
                    </Button>
                  </div>
                }
                name="sale"
                rules={[
                  {
                    required: true,
                    message: 'Seleccione una unidad'
                  }
                ]}
              >
                <Select
                  showSearch
                  filterOption={(input, option) => {
                    const opt = removeAccents(
                      (option?.children ?? '').toString().toLocaleLowerCase()
                    );
                    return opt.includes(
                      removeAccents(input.toString().toLowerCase())
                    );
                  }}
                  onChange={(v: string) => {
                    const sal = sale.find(s => s.id === v);
                    if (!sal) return;
                    setSaleSelected(sal as DataTypeSale);
                  }}
                  style={{ width: '100%' }}
                >
                  {sale.map(col => (
                    <Option key={col.id}>{col.name}</Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>

            <Col>
              <Form.Item
                label={
                  <div className="flex w-[800px] items-center justify-between">
                    <p>Composición de la tela</p>
                    <Button
                      type="primary"
                      className="button-ctx ml-8 h-[30px] w-[30%]"
                      onClick={() => setModalFiber(true)}
                    >
                      Agregar Fibra
                    </Button>
                  </div>
                }
                name="fiber"
                rules={[
                  { required: true, message: 'Escriba una fibra por favor' }
                ]}
              >
                <Select
                  showSearch
                  filterOption={(input, option) => {
                    const opt = removeAccents(
                      (option?.children ?? '').toString().toLocaleLowerCase()
                    );
                    return opt.includes(
                      removeAccents(input.toString().toLowerCase())
                    );
                  }}
                  onChange={(v: string) => {
                    const fibers = fiber.find(f => f.id === v);
                    if (!fibers) return;
                    setFiberSelected(fibers);
                  }}
                  style={{ width: '100%' }}
                >
                  {fiber.map(col => (
                    <Option key={col.id}>{col.name}</Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
            {nextBackBtns(false, ['sale', 'fiber'])}
          </div>

          <div
            className={`w-full ${
              SectionsForm.colors === currentStep ? '' : 'hidden'
            }`}
          >
            <Col span={24}>
              <ColorPickerForCloth
                colorLen={setColorLen}
                colorsTotal={setColorsTotal}
                colorsFilter={
                  form
                    .getFieldsValue()
                    ?.variants?.map((i: { color: string }) => i?.color) || []
                }
              />
            </Col>

            <Col span={24} className="mt-auto">
              <Form.Item>
                {nextBackBtns(
                  false,
                  [
                    'variants',
                    colorLen > 0
                      ? [
                          form
                            .getFieldValue('variants')
                            .map((_: unknown, index: number) => [
                              'variants',
                              index,
                              'color'
                            ])
                        ].flatMap((n: unknown) => n)
                      : [],
                    colorLen > 0
                      ? [
                          form
                            .getFieldValue('variants')
                            .map((_: unknown, index: number) => [
                              'variants',
                              index,
                              'amount'
                            ])
                        ].flatMap((n: unknown) => n)
                      : []
                  ].flatMap(n => n)
                )}
              </Form.Item>
            </Col>
          </div>

          <div
            className={`flex w-full flex-col ${
              SectionsForm.sampler === currentStep ? '' : 'hidden'
            }`}
          >
            <FormClothSectionSampler />
            <Col>{nextBackBtns(false, [], true)}</Col>
          </div>
        </Row>
      </Form>
      <Modal
        title="Agregar"
        visible={modalFiber}
        onCancel={() => setModalFiber(false)}
        footer={null}
        destroyOnClose={true}
      >
        <ModalAddFiber
          onSubmit={() => {
            getFiberCloth();
            setModalFiber(false);
          }}
        />
      </Modal>
      <Modal
        title="Agregar"
        visible={modalSale}
        onCancel={() => setModalSale(false)}
        footer={null}
      >
        <ModalAddSale
          onSubmit={() => {
            getSalesCloth();
            setModalSale(false);
          }}
        />
      </Modal>
    </Card>
  );
};
