import { Button, Form, Modal, Select } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { DataCollection } from '../../../interfaces/Response/Admin/Collection';
import { getCollections } from '../../../services';
import { ModalAddColection } from '../ModalAddColection';
import { removeAccents } from '../../../services/utils';
const { Option } = Select;

interface Props {
  // eslint-disable-next-line no-unused-vars
  changeSelection: (collection: Array<DataCollection | undefined>) => void;
  // eslint-disable-next-line no-unused-vars
  collectionList?: (list: DataCollection[]) => void;
}

export const CollecionPicker: FC<Props> = ({
  changeSelection,
  collectionList
}) => {
  const [collection, setCollection] = useState<DataCollection[]>([]);
  const [showModalColection, setShowModalColection] = useState(false);

  const getCollection = async () => {
    const response = await getCollections(1, 1000, true);
    if (response) {
      if (collectionList) {
        collectionList(response.content as DataCollection[]);
      }
      setCollection(response.content as DataCollection[]);
    }
  };

  useEffect(() => {
    getCollection();
  }, []);

  return (
    <>
      <Form.Item
        label={
          <div className="flex w-[800px] items-center justify-between">
            <p>Usos</p>
            <Button
              type="primary"
              className="button-ctx ml-8 h-[30px] w-[30%]"
              onClick={() => setShowModalColection(true)}
            >
              Agregar Usos
            </Button>
          </div>
        }
        name="collections"
        rules={[{ required: true, message: 'Escriba una colección por favor' }]}
      >
        <Select
          mode="multiple"
          showSearch
          filterOption={(input, option) =>{
            const opt = removeAccents((option?.children ?? '').toString().toLocaleLowerCase());
              return opt.includes(removeAccents(input.toString().toLowerCase()))}
          }
          // size={size}
          // placeholder="Please select"
          // defaultValue={['a10', 'c12']}
          onChange={(values: string[]) => {
            changeSelection(
              values.map(id => collection.find(col => col.id === id))
            );
          }}
          style={{ width: '100%' }}
        >
          {collection.map(col => (
            <Option key={col.id}>{col.name}</Option>
          ))}
        </Select>
      </Form.Item>
      <Modal
        title="Agregar"
        visible={showModalColection}
        onCancel={() => setShowModalColection(false)}
        footer={null}
      >
        <ModalAddColection onSubmit={() => {setShowModalColection(false); getCollection()}} />
      </Modal>
    </>
  );
};
