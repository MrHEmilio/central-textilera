/* eslint-disable no-unused-vars */
import { FC, useState } from 'react';
import { Input, Button } from 'antd';
import { toast } from 'react-toastify';

interface Props {
  onSearch: (e: string, deleteMail: () => void) => void;
  loading: boolean;
}

export const InoutNewLetter: FC<Props> = ({ onSearch, loading }) => {
  const [search, setSearch] = useState('');
  const onClick = () => {
    const regex = /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;

    if (!regex.test(search)) {
      toast.error('Correo Inválido', { theme: 'colored' });
      // onSearch('correoIncalido');
    } else {
      onSearch(search, deleteMail);
    }
  };
  const deleteMail = () => {
    setSearch('');
  };
  const onChange = (e: any) => {
    setSearch(e.target.value);
  };
  return (
    <div style={{ display: 'flex' }}>
      <Input
        className="input-newletters"
        onChange={onChange}
        value={search}
        placeholder="Ingresa tu correo electrónico"
      />
      <Button className="button-newletters" onClick={onClick} loading={loading}>
        Subscríbete
      </Button>
    </div>
  );
};
