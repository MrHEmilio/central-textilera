import { Button, Col, Form, Row } from 'antd';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { CreateRequestAdmin } from '../../app/interfaces/Request/Admin/AdminResponseAdmin';
import { InfoAdmin } from '../../app/interfaces/Response/Admin/AdminResposeAdmin';
import { ProtectAdmin } from '../../app/modules/Admin';
import { FormAdmin } from '../../app/modules/Admin/FormAdmin';
import { CardInfo, InputPassword } from '../../app/modules/shared';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { editSelfAdmin } from '../../app/services/admin/adminServices';
import {
  LoaderActionsHide,
  LoaderActionsShow
} from '../../app/services/redux/actions';
import { newInfoAdmin } from '../../app/services/redux/actions/SessionActions';
import { CtxDispatch, CtxSelector } from '../../app/services/redux/store';
import { changePassLoged } from '../../app/services/security/security';

const profile = () => {
  const [form] = Form.useForm();
  const dispatch = CtxDispatch();

  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const userInfo = CtxSelector((state: { session: any }) => state.session);
  const [infoUser, setInfoUser] = useState<InfoAdmin | null>();
  const [loading, setLoading] = useState(false);

   /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const onFinish = async (values: any) => {
    setLoading(true);
    dispatch(LoaderActionsShow());
    const res = await changePassLoged(values.oldPassword, values.newPassword);
    if (res) {
      form.resetFields();
      toast.success(res.message, { theme: 'colored' });
    }
    setLoading(false);
    dispatch(LoaderActionsHide());
  };

   /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  const onEditAdmin = async (value: CreateRequestAdmin, setLoading: any) => {
    dispatch(LoaderActionsShow());
    delete value.email;
    value.id = infoUser?.id;
    const response = await editSelfAdmin(value);
    if (response) {
      toast.success(`${response.message}`, {
        theme: 'colored'
      });
      const data = { ...response.data, role: userInfo.role };
      dispatch(newInfoAdmin(response.data));
      localStorage.setItem('info', JSON.stringify(data));
    }
    setLoading(false);
    dispatch(LoaderActionsHide());
  };

  useEffect(() => {
    const { info } = userInfo;
    setInfoUser(info);
  }, [userInfo]);

  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Perfil</h1>
          </div>
          <div className="flex justify-center">
            <div className="w-[85%]">
              <Row className="continer-login" gutter={[24, 24]}>
                <Col
                  xs={{ span: 24 }}
                  lg={{ span: 12 }}
                  className="container-col"
                >
                  <FormAdmin
                    onSubmit={onEditAdmin}
                    admin={infoUser}
                    back={false}
                    deleteButton={false}
                    //   onDelete={onDelete}
                  />
                </Col>
                <Col xs={{ span: 24 }} lg={{ span: 12 }}>
                  <CardInfo>
                    <Form
                      form={form}
                      layout="vertical"
                      autoComplete="off"
                      onFinish={onFinish}
                    >
                      <InputPassword
                        label={'Contraseña actual'}
                        name={'oldPassword'}
                        errorLabel={'Introduzca la contraseña actual'}
                      />
                      <InputPassword
                        label={'Nueva contraseña'}
                        name={'newPassword'}
                        errorLabel={'Introduzca la nueva constraseña'}
                      />
                      <InputPassword
                        label={'Confirmar nueva contraseña'}
                        name={'confirmPassword'}
                        confirmPass={'newPassword'}
                        errorLabel={'Introduzca confirmar contraseña'}
                      />
                      <div className="h-[5.5rem]"></div>
                      <Form.Item>
                        <Button
                          type="primary"
                          htmlType="submit"
                          className="button-ctx mt-3 w-full"
                          loading={loading}
                        >
                          Cambiar contraseña
                        </Button>
                      </Form.Item>
                    </Form>
                  </CardInfo>
                </Col>
              </Row>
            </div>
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default profile;
