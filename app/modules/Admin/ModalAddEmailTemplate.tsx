import { CloseOutlined } from '@ant-design/icons';
import { Button, Form, Input, Modal } from 'antd';
import {
  forwardRef,
  useEffect,
  useImperativeHandle,
  useRef,
  useState
} from 'react';
import { REQUIRED_FIELD } from '../../models/constants';
import { EmailServices } from '../../services/admin/emailServices';
import { LoaderActionsHide } from '../../services/redux/actions';
import { rechargeTableAction } from '../../services/redux/actions/AdminTable';
import { CtxDispatch, CtxSelector } from '../../services/redux/store';
import EmailConstructorComponent, {
  EmailConstructorComponentRef
} from './EmailConstructor/EmailConstructorComponent';

export type ModalEmailTemplateRef = {
  // eslint-disable-next-line no-unused-vars
  toogleModal: (refresh?: boolean) => void;
  // eslint-disable-next-line no-unused-vars
  toogleVisible: (visible: boolean) => void;
};
interface EmailTemplateForm {
  name: string;
  subject: string;
}

const ModalAddEmailTemplate = forwardRef((_, ref) => {
  const [form] = Form.useForm();
  const emailTemplateSelection = CtxSelector(s => s.emailTemplateSelected);
  const emailServices = new EmailServices();
  const [formValue, setformValue] = useState<EmailTemplateForm>({
    name: emailTemplateSelection?.name || '',
    subject: ''
  });

  const dispatch = CtxDispatch();
  const [visibleModal, setVisibleModal] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const emailConstructorRef = useRef<EmailConstructorComponentRef>(null);

  const [isReadyEmailTemplate, setisReadyEmailTemplate] = useState(false);

  const toogleVisible = (visible: boolean) => setVisibleModal(visible);
  const toogleModal = (refresh = true) => {
    setVisibleModal(!visibleModal);
    dispatch(rechargeTableAction(refresh));
    form.resetFields();
    setformValue({ name: '', subject: '' });
  };

  const formChanges = (val: EmailTemplateForm) => {
    setformValue(val);
  };

  useImperativeHandle(ref, () => ({
    toogleModal,
    toogleVisible
  }));

  useEffect(() => {
    dispatch(LoaderActionsHide());
    setIsEdit(false);
    if (emailTemplateSelection) {
      const { name, subject } = emailTemplateSelection;
      form.setFieldsValue({ name, subject });
      setformValue({ name, subject });
      setIsEdit(true);
    }
  }, [emailTemplateSelection]);

  return (
    <div>
      <Button className="button-ctx" onClick={() => toogleModal()}>
        Agregar plantilla
      </Button>
      <Modal
        title="Agregar nueva plantilla"
        visible={visibleModal}
        onCancel={() => toogleModal()}
        closeIcon={<CloseOutlined />}
        centered
        destroyOnClose
        closable={true}
        footer={false}
        className="w-auto"
      >
        <div className="flex flex-col">
          <div>
            <Form
              onChange={() => {
                const value = form.getFieldsValue();
                formChanges(value);
              }}
              form={form}
              layout={'inline'}
              className="flex justify-around"
            >
              <Form.Item
                rules={[
                  {
                    required: true,
                    message: REQUIRED_FIELD('Nombre')
                  }
                ]}
                label="Nombre"
                name="name"
                required
              >
                <Input className="custom-input" />
              </Form.Item>
              <Form.Item
                rules={[
                  {
                    required: true,
                    message: REQUIRED_FIELD('Asunto')
                  }
                ]}
                label="Asunto"
                name="subject"
                required
              >
                <Input className="custom-input" />
              </Form.Item>
            </Form>
          </div>
          <div>
            <EmailConstructorComponent
              ref={emailConstructorRef}
              saveResult={d => {
                if (!d) return;
                toogleModal();
              }}
              changeReady={d => {
                setisReadyEmailTemplate(d);
              }}
              name={formValue.name}
              subject={formValue.subject}
            />
          </div>
          <div className='flex justify-end'>
            {!isEdit && (
              <div>
                <Button
                  className="button-ctx ml-3 mt-2 w-fit"
                  disabled={!isReadyEmailTemplate}
                  loading={emailTemplateSelection?.loading}
                  onClick={async () => {
                    await form.validateFields();
                    emailConstructorRef.current?.exportHtml();
                  }}
                >
                  Guardar plantilla
                </Button>
              </div>
            )}
            <div>
              {emailTemplateSelection?.id &&
                emailTemplateSelection?.active &&
                isEdit && (
                  <div>
                    <Button
                      className="button-ctx ml-3 mt-2 w-fit"
                      disabled={!isReadyEmailTemplate}
                      loading={emailTemplateSelection?.loading}
                      onClick={async () => {
                        await form.validateFields();
                        emailConstructorRef.current?.exportHtml();
                      }}
                    >
                      Editar plantilla
                    </Button>
                    <Button
                      loading={emailTemplateSelection.loading}
                      onClick={async () => {
                        await emailServices.deleteDeactivateEmailTemplate(
                          emailTemplateSelection.id
                        );
                        setisReadyEmailTemplate(false);
                        toogleModal();
                      }}
                      className="button-ctx-delete"
                    >
                      Desactivar
                    </Button>
                  </div>
                )}
              {emailTemplateSelection?.id && !emailTemplateSelection.active && (
                <div>
                  <Button
                    className="button-ctx ml-3 mt-2 w-fit"
                    disabled={!isReadyEmailTemplate}
                    loading={emailTemplateSelection?.loading}
                    onClick={async () => {
                      await emailServices.reactiveEmailTemplate(
                        emailTemplateSelection.id
                      );
                      setisReadyEmailTemplate(false);
                      toogleModal();
                    }}
                  >
                    Reactivar
                  </Button>
                </div>
              )}
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
});

ModalAddEmailTemplate.displayName = 'ModalAddEmailTemplate';

export default ModalAddEmailTemplate;
