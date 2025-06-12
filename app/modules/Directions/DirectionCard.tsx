import {
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons';
import { FC, HTMLAttributes } from 'react';
import { Address } from '../../interfaces/Response/Client/NewDirection';
import Direction from './../../../public/img/iconoUbicacion.svg';
import { deleteAddressService } from '../../services/Client/address/addressService';
import { toast } from 'react-toastify';
import { useRouter } from 'next/router';
import { CtxDispatch } from '../../services/redux/store';
import { EditDirectionsActions } from '../../services/redux/actions/EditDirections';
import { Modal } from 'antd';
import { NewAddress } from '../../interfaces/Request/Client/Address';

const { confirm } = Modal;

interface Props {
  direction: NewAddress;
  controls?: boolean;
  selected?: boolean;
  callAdress?: () => void;
}

export const DirectionCard: FC<Props & HTMLAttributes<HTMLDivElement>> = ({
  className,
  direction,
  selected = false,
  controls = true,
  callAdress
}) => {
  const dispatch = CtxDispatch();
  const navigate = useRouter();

  const editAddress = () => {
    dispatch(EditDirectionsActions(direction));
    navigate.push('directions/editDirection');
  };

  const deleteAddress = async () => {
    confirm({
      icon: <ExclamationCircleOutlined twoToneColor={'red'} />,
      content: <h1>¿Desea eliminar esta dirección?</h1>,
      async onOk() {
        const response = await deleteAddressService(direction.id!);
        if (response) {
          toast.success(`${response.message}`, { theme: 'colored' });
          if (callAdress) {
            callAdress();
          }
        }
      },
      cancelText: 'Cancelar'
    });
  };

  return (
    <div
      className={`container-directioncard ${
        selected && 'border-4 border-main'
      } ${className}`}
    >
      <div className="img-directioncard">
        <img src={Direction.src} alt="" />
      </div>
      <div className="body-directioncard">
        <h1 className="font-bold">{`${direction.name} ${
          direction.predetermined ? '  (Dirección predeterminada)' : ''
        }`}</h1>
        {direction && (
          <p className="">
            <span>{direction.streetName}</span> <span>{direction.numExt}</span>,{' '}
            <span>{direction.zipCode}</span>, <span>{direction.state}</span>,{' '}
            <span>{direction.municipality}</span>
          </p>
        )}
      </div>
      {controls && (
        <div className="actions-directioncard">
          <EditOutlined
            className="editicon-directioncard"
            onClick={editAddress}
          />
          <DeleteOutlined
            className="deleteicon-directioncard"
            onClick={deleteAddress}
          />
        </div>
      )}
    </div>
  );
};
