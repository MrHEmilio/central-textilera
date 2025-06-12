import { Button, Form, Modal, Select } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { DataUse } from '../../../interfaces/Response/Admin/Use';
import { getUse } from '../../../services/admin/uses';
import { ModalAddUse } from '../ModalAddUse';
const { Option } = Select;

interface Props {
  // eslint-disable-next-line no-unused-vars
  changeSelection: (use: Array<DataUse | undefined>) => void;
  // eslint-disable-next-line no-unused-vars
  usesList: (uses: DataUse[]) => void;
}

export const UsesPicker: FC<Props> = ({ changeSelection, usesList }) => {
  const [showModalUse, setShowModalUse] = useState(false);
  const [uses, setUses] = useState<DataUse[]>([]);
  const [defaultValue, setDefaultValue] = useState<string[]>([
    '9dbf397b-2e39-4ff8-9d71-35b87f5a64fb'
  ]);

  const getUsesCloth = async () => {
    const response = await getUse(true);
    if (response) {
      if (usesList) {
        usesList(response.content);
      }
      setUses([...response.content]);
    }
  };

  useEffect(() => {
    getUsesCloth();
  }, []);

  const onSubmit = () => {
    setShowModalUse(false);
    getUsesCloth();
  };

  const newUse = (use: DataUse) => {
    if (use) {
      setDefaultValue([...defaultValue, use.id]);
    }
  };

  return (
    <>
      <Form.Item
        label={
          <div className="flex w-[800px] items-center justify-between">
            <p>Usos</p>
            <Button
              type="primary"
              className="button-ctx  right-0 z-50 ml-8 h-[30px] w-[30%]"
              style={{ top: '-20' }}
              onClick={() => setShowModalUse(true)}
            >
              Agregar Uso
            </Button>
          </div>
        }
        name="uses"
        // initialValue={defaultValue}
        rules={[{ required: true, message: 'Escriba un uso por favor' }]}
      >
        <Select
          showSearch
          filterOption={(input, option) =>
            (option?.children ?? '').toString().toLowerCase().includes(input)
          }
          mode="multiple"
          // size={size}
          // placeholder="Please select"
          onChange={(ids: string[]) => {
            changeSelection(ids.map(id => uses.find(use => use.id === id)));
          }}
          style={{ width: '100%' }}
          value={defaultValue}
        >
          {uses.map(col => (
            <Option key={col.id}>{col.name}</Option>
          ))}
        </Select>
      </Form.Item>
      <Modal
        title="Agregar"
        visible={showModalUse}
        onCancel={() => setShowModalUse(false)}
        footer={null}
      >
        <ModalAddUse onSubmit={onSubmit} onResponse={newUse} />
      </Modal>
    </>
  );
};
