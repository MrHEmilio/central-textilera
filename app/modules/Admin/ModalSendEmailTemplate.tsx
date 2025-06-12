/* eslint-disable no-unused-vars */
import { CloseOutlined } from '@ant-design/icons';
import { Radio } from 'antd';
import Button from 'antd/lib/button';
import Modal from 'antd/lib/modal/Modal';
import Tooltip from 'antd/lib/tooltip';
import { FC, useState } from 'react';
import { SingleEmailTemplate } from '../../interfaces/Response/Admin/EmailServicesResponses';
import { EmailServices } from '../../services/admin/emailServices';
import { GoogleIcon } from '../shared';

enum SendTo {
  news = 'NEWSLETTERS',
  clients = 'CLIENTS',
  everybody = 'EVERYBODY'
}

type Props = {
  template: SingleEmailTemplate;
};
export const ModalSendEmailTemplate: FC<Props> = ({ template }) => {
  const emailServices = new EmailServices();
  const [sendToValue, setsendToValue] = useState<string>(SendTo.news);
  const [visible, setvisible] = useState(false);
  const [isLoading, setisLoading] = useState(false);
  const toogleVisible = () => setvisible(!visible);
  const changeSendToValue = (sendTo: string) => setsendToValue(sendTo);
  const onSendEmail = async () => {
    setisLoading(true);
    await emailServices.postSendTemplate({
      sendTo: sendToValue,
      emailNewsletterTemplate: template.id
    });
    setisLoading(false);
  };
  return (
    <div onClick={e => e.stopPropagation()}>
      <Tooltip title="Enviar correo" placement="bottom" className="w-5">
        <Button
          className="
            mx-auto grid
            aspect-square
            w-5 place-content-center rounded-full
            bg-main
            "
          onClick={e => {
            e.stopPropagation();
            toogleVisible();
          }}
        >
          <GoogleIcon icon="send" className="text-sm text-white" />
        </Button>
      </Tooltip>
      <Modal
        title="Enviar correo"
        visible={visible}
        onCancel={e => {
          e.stopPropagation();
          toogleVisible();
        }}
        closeIcon={<CloseOutlined />}
        centered
        footer={false}
      >
        <p className="mb-8">Enviar correo a:</p>
        <Radio.Group
          className="grid grid-cols-3 justify-between"
          defaultValue={SendTo.news}
          onChange={({ target }) => {
            changeSendToValue(target.value);
          }}
        >
          <Radio className="flex justify-center" value={SendTo.news}>
            Noticias
          </Radio>
          <Radio className="flex justify-center" value={SendTo.clients}>
            Clientes
          </Radio>
          <Radio className="flex justify-center" value={SendTo.everybody}>
            Todos
          </Radio>
        </Radio.Group>
        <br />
        <div className="mt-4 flex w-full justify-end ">
          <Button
            className="button-ctx"
            onClick={onSendEmail}
            loading={isLoading}
          >
            Enviar
          </Button>
        </div>
      </Modal>
    </div>
  );
};
