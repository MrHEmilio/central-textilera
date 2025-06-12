import { addNewLetters } from '../../../services/newLetters/newLetters';
import { InoutNewLetter } from '../InputNewLetter';
import { toast } from 'react-toastify';
import { useState } from 'react';

export const NewLetters = () => {
  const [loading, setLoading] = useState(false);
  const onSearch = async (search: string, deleteMail: () => void) => {
    setLoading(true);
    const response = await addNewLetters(search, true);
    if (response?.status) {
      const mensaje = response?.data.message;
      toast.error(mensaje, { theme: 'colored' });
      deleteMail();
    } else {
      toast.success('Subscrito a nuestro Newsletter', { theme: 'colored' });
      deleteMail();
    }
    setLoading(false);
  };
  return (
    <div
      className="news-Letter  w-full"
      style={{ display: 'flex', justifyContent: 'center' }}
    >
      <div className="container flex flex-col justify-center align-top">
        <p className="text-5xl">Subscríbete</p>
        <div className="text-2xl" style={{ paddingTop: '1rem' }}>
          <span className="text-2xl">
            A nuestro newsletter y obtén ofertas y promociones exclusivas.
          </span>
        </div>

        <div className="container-newletters mx-auto">
          <InoutNewLetter
            onSearch={(e: string, deleteMail: () => void) =>
              onSearch(e, deleteMail)
            }
            loading={loading}
          />
        </div>
      </div>
    </div>
  );
};
